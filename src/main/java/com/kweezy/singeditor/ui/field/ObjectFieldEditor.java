package com.kweezy.singeditor.ui.field;

import com.kweezy.singeditor.ui.GenericObjectEditorPanel;
import com.kweezy.singeditor.ui.util.SubtypeFactory;

import javax.swing.*;
import java.awt.*;

public class ObjectFieldEditor extends AbstractFieldEditor {
    private Object value;
    private final String fieldName;
    private final Class<?> fieldType;
    private final Component parent;
    private final JButton editBtn;
    private final JButton removeBtn;

    public ObjectFieldEditor(String fieldName, Class<?> fieldType, Component parent) {
        super(new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)));
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.parent = parent;
        this.editBtn = new JButton("Edit...");
        this.removeBtn = new JButton("Remove");
        ((JPanel) this.component).add(editBtn);
        ((JPanel) this.component).add(removeBtn);

        editBtn.addActionListener(e -> onEdit());
        removeBtn.addActionListener(e -> onRemove());
        updateRemoveVisibility();
    }

    private void updateRemoveVisibility() {
        removeBtn.setVisible(value != null);
        component.revalidate();
        component.repaint();
    }

    private void onEdit() {
        try {
            Object inst = (value != null) ? value : SubtypeFactory.instantiateSubtype(fieldType, parent);
            if (inst == null) return;
            GenericObjectEditorPanel<?> sub = new GenericObjectEditorPanel<>(inst.getClass());
            sub.setObject(inst);
            JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(parent), fieldName, Dialog.ModalityType.APPLICATION_MODAL);
            dlg.getContentPane().add(new JScrollPane(sub), BorderLayout.CENTER);
            final boolean[] saved = { false };
            JButton save = new JButton("Save");
            save.addActionListener(ev -> { saved[0] = true; dlg.dispose(); });
            dlg.getContentPane().add(save, BorderLayout.SOUTH);
            dlg.pack(); dlg.setLocationRelativeTo(parent); dlg.setVisible(true);
            if (saved[0]) {
                value = sub.getObject();
                updateRemoveVisibility();
                markDirty();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Error editing object: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onRemove() {
        value = null;
        updateRemoveVisibility();
        markDirty();
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
        updateRemoveVisibility();
    }

    @Override
    public Object getValue() {
        return value;
    }
}
