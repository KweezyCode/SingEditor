package com.kweezy.singeditor.ui.util;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import javax.swing.*;
import java.awt.*;

public final class SubtypeFactory {
    private SubtypeFactory() {}

    public static Object instantiateSubtype(Class<?> baseType, Component parent) throws Exception {
        if (baseType == String.class) return "";
        if (!baseType.isInterface() && !java.lang.reflect.Modifier.isAbstract(baseType.getModifiers())) {
            return baseType.getDeclaredConstructor().newInstance();
        }
        JsonSubTypes stAnn = baseType.getAnnotation(JsonSubTypes.class);
        if (stAnn == null || stAnn.value().length == 0) {
            JOptionPane.showMessageDialog(parent, "No subtypes available for " + baseType.getSimpleName(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        JsonSubTypes.Type[] types = stAnn.value();
        String[] names = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            names[i] = (types[i].name() != null && !types[i].name().isEmpty())
                ? types[i].name() : types[i].value().getSimpleName();
        }
        String sel = (String) JOptionPane.showInputDialog(
                parent, "Select type of " + baseType.getSimpleName(),
                "Subtype", JOptionPane.PLAIN_MESSAGE,
                null, names, names[0]);
        if (sel == null) return null;
        for (JsonSubTypes.Type t : types) {
            String tn = (t.name() != null && !t.name().isEmpty()) ? t.name() : t.value().getSimpleName();
            if (sel.equals(tn)) return t.value().getDeclaredConstructor().newInstance();
        }
        return null;
    }
}

