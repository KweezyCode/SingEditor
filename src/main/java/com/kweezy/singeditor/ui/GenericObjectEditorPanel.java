package com.kweezy.singeditor.ui;

import com.kweezy.singeditor.ui.field.FieldEditor;
import com.kweezy.singeditor.ui.field.FieldEditorFactory;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
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

    public GenericObjectEditorPanel(Class<T> clazz) {
        this.clazz = clazz;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            fields.add(f);
            add(createRow(f));
        }
    }

    private JComponent createRow(Field field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(field.getName()));
        FieldEditor fe = FieldEditorFactory.create(field, this);
        panel.add(fe.getComponent());
        editors.put(field, fe);
        return panel;
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
            out = clazz.getDeclaredConstructor().newInstance();
            for (Field f : fields) {
                FieldEditor fe = editors.get(f);
                if (fe == null) continue;
                Object value = fe.getValue();
                boolean isList = java.util.List.class.isAssignableFrom(f.getType());
                if (isList) {
                    // Всегда применяем правило: пустые списки -> null
                    try { f.set(out, value); } catch (Exception ignore) {}
                } else if (fe.isDirty()) {
                    try { f.set(out, value); } catch (Exception ignore) {}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return object; // fallback: вернуть последний объект
        }
        return out;
    }
}
