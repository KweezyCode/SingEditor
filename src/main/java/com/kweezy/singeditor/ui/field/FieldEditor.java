package com.kweezy.singeditor.ui.field;

import javax.swing.*;

public interface FieldEditor {
    JComponent getComponent();
    void setValue(Object value);
    Object getValue();
    boolean isDirty();
    void clearDirty();
}

