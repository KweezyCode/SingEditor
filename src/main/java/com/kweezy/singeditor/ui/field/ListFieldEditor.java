package com.kweezy.singeditor.ui.field;

import com.kweezy.singeditor.ui.GenericObjectEditorPanel;
import com.kweezy.singeditor.ui.util.SubtypeFactory;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class ListFieldEditor extends AbstractFieldEditor {
    private final DefaultListModel<Object> model = new DefaultListModel<>();
    private final JList<Object> list = new JList<>(model);
    private final JPanel panel = new JPanel(new BorderLayout());
    private final Field field;
    private final Component parent;

    public ListFieldEditor(Field field, Component parent) {
        super(new JPanel(new BorderLayout()));
        this.field = field;
        this.parent = parent;
        JScrollPane scroll = new JScrollPane(list);
        list.setVisibleRowCount(5);
        scroll.setPreferredSize(new Dimension(200, 100));
        ((JPanel) this.component).add(scroll, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton remove = new JButton("Remove");
        btns.add(add); btns.add(edit); btns.add(remove);
        ((JPanel) this.component).add(btns, BorderLayout.EAST);

        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) editSelected();
            }
        });
        edit.addActionListener(e -> editSelected());
        add.addActionListener(e -> onAdd());
        remove.addActionListener(e -> onRemove());
    }

    private Class<?> elementClass() {
        try {
            ParameterizedType pt = (ParameterizedType) field.getGenericType();
            return (Class<?>) pt.getActualTypeArguments()[0];
        } catch (Exception e) { return Object.class; }
    }

    private void onAdd() {
        try {
            Class<?> elem = elementClass();
            Object inst = SubtypeFactory.instantiateSubtype(elem, parent);
            if (inst == null) return;
            if (!(inst instanceof String) && !(inst instanceof Number)) {
                GenericObjectEditorPanel<?> sub = new GenericObjectEditorPanel<>(inst.getClass());
                sub.setObject(inst);
                JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(parent), field.getName(), Dialog.ModalityType.APPLICATION_MODAL);
                dlg.getContentPane().add(new JScrollPane(sub), BorderLayout.CENTER);
                JButton ok = new JButton("OK"); ok.addActionListener(ev -> { dlg.dispose(); });
                dlg.getContentPane().add(ok, BorderLayout.SOUTH);
                dlg.pack(); dlg.setLocationRelativeTo(parent); dlg.setVisible(true);
                inst = sub.getObject();
            }
            model.addElement(inst);
            markDirty();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Error adding element: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelected() {
        int sel = list.getSelectedIndex();
        if (sel < 0) return;
        Object item = model.getElementAt(sel);
        if (item instanceof String || item instanceof Number) {
            String input = JOptionPane.showInputDialog(parent, "Edit value:", item.toString());
            if (input != null) {
                Object newVal = (item instanceof Integer ? Integer.parseInt(input)
                        : item instanceof Long ? Long.parseLong(input)
                        : item instanceof Double ? Double.parseDouble(input)
                        : input);
                model.set(sel, newVal);
                markDirty();
            }
        } else {
            GenericObjectEditorPanel<?> sub = new GenericObjectEditorPanel<>(item.getClass());
            sub.setObject(item);
            JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(parent), field.getName(), Dialog.ModalityType.APPLICATION_MODAL);
            dlg.getContentPane().add(new JScrollPane(sub), BorderLayout.CENTER);
            JButton ok = new JButton("OK"); ok.addActionListener(ev -> { dlg.dispose(); });
            dlg.getContentPane().add(ok, BorderLayout.SOUTH);
            dlg.pack(); dlg.setLocationRelativeTo(parent); dlg.setVisible(true);
            model.set(sel, sub.getObject());
            markDirty();
        }
    }

    private void onRemove() {
        int idx = list.getSelectedIndex();
        if (idx >= 0) { model.remove(idx); markDirty(); }
    }

    @Override
    public void setValue(Object value) {
        try {
            updating = true;
            model.clear();
            if (value instanceof List<?> l) { for (Object e : l) model.addElement(e); }
        } finally { updating = false; }
    }

    @Override
    public Object getValue() {
        List<Object> out = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) out.add(model.getElementAt(i));
        return out.isEmpty() ? null : out;
    }
}

