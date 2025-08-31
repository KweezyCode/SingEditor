package com.kweezy.singeditor.ui;

import com.kweezy.singeditor.ui.field.FieldEditor;
import com.kweezy.singeditor.ui.field.FieldEditorFactory;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.HashMap;

/**
 * Generic editor panel for any Java object using reflection.
 */
public class GenericObjectEditorPanel<T> extends JPanel {
    private final Class<T> clazz;
    private T object;
    private final java.util.List<Field> fields = new java.util.ArrayList<>();
    private final Map<Field, FieldEditor> editors = new HashMap<>();
    private int maxLabelWidth = 0;

    public GenericObjectEditorPanel(Class<T> clazz) {
        this.clazz = clazz;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Prepare: collect fields and compute maximum label width
        Field[] decl = clazz.getDeclaredFields();
        JLabel probe = new JLabel();
        for (Field f : decl) {
            if (Modifier.isStatic(f.getModifiers())) continue; // не редактируем static
            f.setAccessible(true);
            fields.add(f);
            probe.setText(f.getName());
            Dimension d = probe.getPreferredSize();
            if (d.width > maxLabelWidth) maxLabelWidth = d.width;
        }
        // Create rows
        for (int i = 0; i < fields.size(); i++) {
            add(createRow(fields.get(i), i));
        }
    }

    private static Color zebra(Color base, int row) {
        // Slight darkening/lightening for zebra striping
        float factor = (row % 2 == 0) ? 1.0f : 0.96f;
        int r = Math.min(255, Math.round(base.getRed() * factor));
        int g = Math.min(255, Math.round(base.getGreen() * factor));
        int b = Math.min(255, Math.round(base.getBlue() * factor));
        return new Color(r, g, b);
    }

    private JComponent createRow(Field field, int rowIndex) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        Color base = UIManager.getColor("Panel.background");
        if (base == null) base = row.getBackground();
        row.setBackground(zebra(base, rowIndex));
        row.setOpaque(true);

        JLabel label = new JLabel(field.getName());
        Dimension pref = label.getPreferredSize();
        // Fixed width for all labels
        Dimension fixed = new Dimension(maxLabelWidth + 8, pref.height);
        label.setPreferredSize(fixed);
        label.setMinimumSize(fixed);
        label.setMaximumSize(fixed);

        FieldEditor fe = FieldEditorFactory.create(field, this);

        row.add(label);
        row.add(fe.getComponent());
        row.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        editors.put(field, fe);
        return row;
    }

    public void setObject(Object obj) {
        this.object = clazz.cast(obj);
        for (Field f : fields) {
            try {
                Object val = f.get(obj);
                FieldEditor fe = editors.get(f);
                if (fe != null) {
                    fe.setValue(val);
                    fe.clearDirty();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public T getObject() {
        T out;
        try {
            // 1. Создаём новый экземпляр
            out = clazz.getDeclaredConstructor().newInstance();
            // 2. Копируем исходные значения (если есть исходный объект)
            if (object != null) {
                for (Field f : fields) {
                    try {
                        Object originalVal = f.get(object);
                        f.set(out, originalVal);
                    } catch (Exception ignore) {}
                }
            }
            // 3. Применяем изменения из редакторов
            for (Field f : fields) {
                FieldEditor fe = editors.get(f);
                if (fe == null) continue;
                Object value = fe.getValue();
                boolean isList = java.util.List.class.isAssignableFrom(f.getType());
                if (isList) {
                    // Всегда устанавливаем (логика: пустой список -> null внутри FieldEditor, если так реализовано)
                    try { f.set(out, value); } catch (Exception ignore) {}
                } else if (fe.isDirty()) {
                    try { f.set(out, value); } catch (Exception ignore) {}
                }
            }
            // 4. Обновляем текущий объект ссылкой на новую версию
            object = out;
        } catch (Exception e) {
            e.printStackTrace();
            return object; // fallback: return the last object
        }
        return out;
    }

    // Return current backing object (create if null) without recomputing from editors
    public T getMutableObject() {
        try {
            if (object == null) {
                object = clazz.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return object;
    }
}
