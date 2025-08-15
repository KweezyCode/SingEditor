package com.kweezy.singeditor.ui.field;

import javax.swing.*;

public class NumberFieldEditor extends AbstractFieldEditor {
    private final JSpinner spinner;
    private final Class<?> numberType;

    public NumberFieldEditor(Class<?> numberType) {
        super(new JSpinner());
        this.spinner = (JSpinner) super.component;
        this.numberType = numberType;
        if (numberType == Integer.class || numberType == int.class) {
            spinner.setModel(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        } else if (numberType == Long.class || numberType == long.class) {
            spinner.setModel(new SpinnerNumberModel(0L, Long.MIN_VALUE, Long.MAX_VALUE, 1L));
        } else {
            spinner.setModel(new SpinnerNumberModel(0.0, null, null, 0.1));
        }
        spinner.addChangeListener(e -> markDirty());
    }

    @Override
    public void setValue(Object value) {
        try {
            updating = true;
            if (value != null) spinner.setValue(value);
        } finally { updating = false; }
    }

    @Override
    public Object getValue() {
        Object v = spinner.getValue();
        if (numberType == Integer.class || numberType == int.class) return ((Number) v).intValue();
        if (numberType == Long.class || numberType == long.class) return ((Number) v).longValue();
        return ((Number) v).doubleValue();
    }
}

