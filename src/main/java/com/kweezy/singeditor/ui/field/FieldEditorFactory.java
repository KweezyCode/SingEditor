package com.kweezy.singeditor.ui.field;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public final class FieldEditorFactory {
    private FieldEditorFactory() {}

    public static FieldEditor create(Field field, Component parent) {
        Class<?> type = field.getType();
        if (type == Boolean.class || type == boolean.class) {
            return new BooleanFieldEditor();
        }
        if (type == String.class) {
            return new TextFieldEditor();
        }
        if (type == Integer.class || type == int.class ||
            type == Long.class || type == long.class ||
            type == Double.class || type == double.class) {
            return new NumberFieldEditor(type);
        }
        if (java.util.List.class.isAssignableFrom(type)) {
            return new ListFieldEditor(field, parent);
        }
        return new ObjectFieldEditor(field.getName(), type, parent);
    }
}

