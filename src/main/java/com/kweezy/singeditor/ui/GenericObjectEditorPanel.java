package com.kweezy.singeditor.ui;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Generic editor panel for any Java object using reflection.
 * Supports boolean, String, Number, nested object, and List<Object>.
 */
public class GenericObjectEditorPanel<T> extends JPanel {
    private final Class<T> clazz;
    private T object;
    private final java.util.List<Field> fields = new java.util.ArrayList<>();
    private final java.util.List<JComponent> editors = new java.util.ArrayList<>();

    public GenericObjectEditorPanel(Class<T> clazz) {
        this.clazz = clazz;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            fields.add(f);
            add(createFieldEditor(f));
        }
    }

    private JComponent createFieldEditor(Field field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(field.getName());
        panel.add(label);
        Class<?> type = field.getType();
        JComponent editor;
        if (type == boolean.class || type == Boolean.class) {
            editor = new JCheckBox();
        } else if (type == String.class) {
            editor = new JTextField(20);
        } else if (type == int.class || type == Integer.class) {
            editor = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        } else if (type == long.class || type == Long.class) {
            editor = new JSpinner(new SpinnerNumberModel(0L, Long.MIN_VALUE, Long.MAX_VALUE, 1L));
        } else if (type == double.class || type == Double.class) {
            editor = new JSpinner(new SpinnerNumberModel(0.0, null, null, 0.1));
        } else if (java.util.List.class.isAssignableFrom(type)) {
            editor = createListEditor(field);
        } else {
            JButton btn = new JButton("Edit...");
            btn.addActionListener(e -> {
                Object sub;
                try {
                    sub = field.get(object);
                    if (sub == null) {
                        sub = field.getType().getDeclaredConstructor().newInstance();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                GenericObjectEditorPanel subPanel = new GenericObjectEditorPanel(field.getType());
                subPanel.setObject(sub);
                JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), field.getName(), Dialog.ModalityType.APPLICATION_MODAL);
                dialog.getContentPane().add(new JScrollPane(subPanel), BorderLayout.CENTER);
                JButton ok = new JButton("OK");
                ok.addActionListener(ev -> {
                    try {
                        Object newSub = subPanel.getObject();
                        field.set(object, newSub);
                        dialog.dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                dialog.getContentPane().add(ok, BorderLayout.SOUTH);
                dialog.pack();
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            });
            editor = btn;
        }
        panel.add(editor);
        editors.add(editor);
        return panel;
    }

    private JComponent createListEditor(Field field) {
        JPanel panel = new JPanel(new BorderLayout());
        // prepare list and model
        DefaultListModel<Object> model = new DefaultListModel<>();
        JList<Object> list = new JList<>(model);
        list.setVisibleRowCount(5);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setPreferredSize(new Dimension(200, 100));
        panel.add(scroll, BorderLayout.CENTER);
        // edit item on double-click
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int idx = list.locationToIndex(e.getPoint());
                    if (idx >= 0) {
                        Object item = model.getElementAt(idx);
                        // simple types: edit via input dialog
                        if (item instanceof String || item instanceof Number) {
                            String input = JOptionPane.showInputDialog(GenericObjectEditorPanel.this,
                                "Edit value:", item.toString());
                            if (input != null) {
                                model.set(idx, item instanceof Integer ? Integer.parseInt(input)
                                    : item instanceof Long ? Long.parseLong(input)
                                    : item instanceof Double ? Double.parseDouble(input)
                                    : input);
                            }
                        } else {
                            // complex object: nested editor
                            Class<?> concrete = item.getClass();
                            GenericObjectEditorPanel<?> subPanel = new GenericObjectEditorPanel<>(concrete);
                            subPanel.setObject(item);
                            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(panel), field.getName(), Dialog.ModalityType.APPLICATION_MODAL);
                            dialog.getContentPane().add(new JScrollPane(subPanel), BorderLayout.CENTER);
                            JButton ok = new JButton("OK");
                            ok.addActionListener(ev -> {
                                try {
                                    Object newItem = subPanel.getObject();
                                    model.set(idx, newItem);
                                    dialog.dispose();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });
                            dialog.getContentPane().add(ok, BorderLayout.SOUTH);
                            dialog.pack();
                            dialog.setLocationRelativeTo(panel);
                            dialog.setVisible(true);
                        }
                    }
                }
            }
        });
        JPanel btns = new JPanel();
        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton remove = new JButton("Remove");
        btns.add(add);
        btns.add(edit);
        btns.add(remove);
        panel.add(btns, BorderLayout.EAST);
        edit.addActionListener(e -> {
            int sel = list.getSelectedIndex();
            if (sel >= 0) {
                // reuse double-click logic
                Object item = model.getElementAt(sel);
                Class<?> concrete = item.getClass();
                GenericObjectEditorPanel<?> subPanel = new GenericObjectEditorPanel<>(concrete);
                subPanel.setObject(item);
                JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(panel), field.getName(), Dialog.ModalityType.APPLICATION_MODAL);
                dialog.getContentPane().add(new JScrollPane(subPanel), BorderLayout.CENTER);
                JButton okBtn = new JButton("OK");
                okBtn.addActionListener(ev -> {
                    try {
                        Object newItem = subPanel.getObject();
                        model.set(sel, newItem);
                        dialog.dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                dialog.getContentPane().add(okBtn, BorderLayout.SOUTH);
                dialog.pack();
                dialog.setLocationRelativeTo(panel);
                dialog.setVisible(true);
            }
        });
        add.addActionListener(e -> {
            try {
                // get element type for adding
                ParameterizedType addPt = (ParameterizedType) field.getGenericType();
                Class<?> addElem = (Class<?>) addPt.getActualTypeArguments()[0];
                if (addElem.isInterface() || Modifier.isAbstract(addElem.getModifiers())) {
                    JsonSubTypes subTypesAnnotation = addElem.getAnnotation(JsonSubTypes.class);
                    if (subTypesAnnotation != null && subTypesAnnotation.value().length > 0) {
                        JsonSubTypes.Type[] types = subTypesAnnotation.value();
                        String[] typeNames = new String[types.length];
                        for (int i = 0; i < types.length; i++) {
                            typeNames[i] = (types[i].name() != null && !types[i].name().isEmpty())
                                    ? types[i].name()
                                    : types[i].value().getSimpleName();
                        }

                        String selectedTypeName = (String) JOptionPane.showInputDialog(
                                GenericObjectEditorPanel.this,
                                "Select the type of " + addElem.getSimpleName() + " to add:",
                                "Add Element",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                typeNames,
                                typeNames[0]);

                        if (selectedTypeName != null) {
                            Class<?> selectedClass = null;
                            for (JsonSubTypes.Type type : types) {
                                String currentTypeName = (type.name() != null && !type.name().isEmpty())
                                        ? type.name()
                                        : type.value().getSimpleName();
                                if (selectedTypeName.equals(currentTypeName)) {
                                    selectedClass = type.value();
                                    break;
                                }
                            }

                            if (selectedClass != null) {
                                Object inst = selectedClass.getDeclaredConstructor().newInstance();
                                model.addElement(inst);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(GenericObjectEditorPanel.this,
                                "Cannot add element for abstract/interface type: " + addElem.getSimpleName() + ". No subtypes defined.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    Object inst = addElem.getDeclaredConstructor().newInstance();
                    model.addElement(inst);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GenericObjectEditorPanel.this,
                    "Error adding element: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        remove.addActionListener(e -> {
            int idx = list.getSelectedIndex(); if (idx >= 0) model.remove(idx);
        });
        return panel;
    }

    public void setObject(Object obj) {
        this.object = clazz.cast(obj);
        for (int i = 0; i < fields.size(); i++) {
            Field f = fields.get(i);
            JComponent editor = editors.get(i);
            try {
                Object val = f.get(obj);
                if (editor instanceof JCheckBox cb) {
                    cb.setSelected(val != null && (Boolean) val);
                } else if (editor instanceof JTextField tf) {
                    tf.setText(val != null ? val.toString() : "");
                } else if (editor instanceof JSpinner sp) {
                    sp.setValue(val != null ? val : sp.getValue());
                } else if (editor instanceof JPanel listPanel && java.util.List.class.isAssignableFrom(f.getType())) {
                    JScrollPane sp = (JScrollPane) listPanel.getComponent(0);
                    JList<?> list = (JList<?>) sp.getViewport().getView();
                    DefaultListModel<Object> model = (DefaultListModel<Object>) list.getModel();
                    model.clear();
                    if (val instanceof java.util.List<?> vals) {
                        for (Object e : vals) model.addElement(e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public T getObject() {
        try {
            if (object == null) object = clazz.getDeclaredConstructor().newInstance();
            for (int i = 0; i < fields.size(); i++) {
                Field f = fields.get(i);
                JComponent editor = editors.get(i);
                Object value = null;
                if (editor instanceof JCheckBox cb) {
                    value = cb.isSelected();
                } else if (editor instanceof JSpinner sp) {
                    value = sp.getValue();
                } else if (editor instanceof JTextField tf) {
                    String txt = tf.getText();
                    if (f.getType() == String.class) value = txt;
                    else if (f.getType() == int.class || f.getType() == Integer.class) value = Integer.parseInt(txt);
                    else if (f.getType() == long.class || f.getType() == Long.class) value = Long.parseLong(txt);
                    else if (f.getType() == double.class || f.getType() == Double.class) value = Double.parseDouble(txt);
                } else if (editor instanceof JPanel listPanel && java.util.List.class.isAssignableFrom(f.getType())) {
                    JScrollPane sp = (JScrollPane) listPanel.getComponent(0);
                    JList<?> list = (JList<?>) sp.getViewport().getView();
                    java.util.List<Object> newList = new java.util.ArrayList<>();
                    DefaultListModel<?> model = (DefaultListModel<?>) list.getModel();
                    for (int idx = 0; idx < model.getSize(); idx++) {
                        newList.add(model.getElementAt(idx));
                    }
                    value = newList;
                }
                if (value != null) {
                    f.set(object, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
