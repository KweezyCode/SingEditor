package com.kweezy.singeditor.ui.field;

import javax.swing.*;

public class TextFieldEditor extends AbstractFieldEditor {
    private final JTextField textField;

    public TextFieldEditor() {
        super(new JTextField(20));
        this.textField = (JTextField) super.component;
        this.textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { markDirty(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { markDirty(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { markDirty(); }
        });
    }

    @Override
    public void setValue(Object value) {
        try {
            updating = true;
            textField.setText(value == null ? "" : String.valueOf(value));
        } finally { updating = false; }
    }

    @Override
    public Object getValue() {
        String txt = textField.getText();
        return txt == null || txt.isEmpty() ? null : txt;
    }
}

