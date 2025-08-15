package com.kweezy.singeditor.ui.field;

import javax.swing.*;

public abstract class AbstractFieldEditor implements FieldEditor {
    protected final JComponent component;
    private boolean dirty = false;
    protected boolean updating = false;

    protected AbstractFieldEditor(JComponent component) {
        this.component = component;
    }

    @Override
    public JComponent getComponent() {
        return component;
    }

    protected void markDirty() {
        if (!updating) dirty = true;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void clearDirty() {
        dirty = false;
    }
}

