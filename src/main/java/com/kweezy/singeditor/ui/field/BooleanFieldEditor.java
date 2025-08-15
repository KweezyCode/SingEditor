package com.kweezy.singeditor.ui.field;

import javax.swing.*;

public class BooleanFieldEditor extends AbstractFieldEditor {
    private final JCheckBox checkBox;

    public BooleanFieldEditor() {
        super(new JCheckBox());
        this.checkBox = (JCheckBox) super.component;
        this.checkBox.addActionListener(e -> markDirty());
    }

    @Override
    public void setValue(Object value) {
        try {
            updating = true;
            checkBox.setSelected(value instanceof Boolean && (Boolean) value);
        } finally { updating = false; }
    }

    @Override
    public Object getValue() {
        return checkBox.isSelected();
    }
}

