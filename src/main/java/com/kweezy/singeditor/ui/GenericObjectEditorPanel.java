package com.kweezy.singeditor.ui;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.HashMap;
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
    private final Map<Field, Object> initialValues = new HashMap<>();
    // Track which fields were actually modified by user
    private final Map<Field, Boolean> dirtyFields = new HashMap<>();
    private boolean updating = false;

    public GenericObjectEditorPanel(Class<T> clazz) {
        this.clazz = clazz;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            fields.add(f);
            add(createFieldEditor(f));
        }
    }

    private void markDirty(Field field) {
        if (!updating) {
            dirtyFields.put(field, Boolean.TRUE);
        }
    }

    private JComponent createFieldEditor(Field field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(field.getName());
        panel.add(label);
        Class<?> type = field.getType();
        JComponent editor;
        if (type == boolean.class || type == Boolean.class) {
            JCheckBox cb = new JCheckBox();
            cb.addActionListener(e -> markDirty(field));
            editor = cb;
        } else if (type == String.class) {
            JTextField tf = new JTextField(20);
            tf.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) { markDirty(field); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { markDirty(field); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { markDirty(field); }
            });
            editor = tf;
        } else if (type == int.class || type == Integer.class) {
            JSpinner sp = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
            sp.addChangeListener(e -> markDirty(field));
            editor = sp;
        } else if (type == long.class || type == Long.class) {
            JSpinner sp = new JSpinner(new SpinnerNumberModel(0L, Long.MIN_VALUE, Long.MAX_VALUE, 1L));
            sp.addChangeListener(e -> markDirty(field));
            editor = sp;
        } else if (type == double.class || type == Double.class) {
            JSpinner sp = new JSpinner(new SpinnerNumberModel(0.0, null, null, 0.1));
            sp.addChangeListener(e -> markDirty(field));
            editor = sp;
        } else if (java.util.List.class.isAssignableFrom(type)) {
            editor = createListEditor(field);
        } else {
            JButton editBtn = new JButton("Edit...");
            editBtn.addActionListener(e -> {
                Object sub;
                try {
                    sub = instantiateFieldInstance(field);
                    if (sub == null) return;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                GenericObjectEditorPanel<?> subPanel = new GenericObjectEditorPanel<>(sub.getClass());
                subPanel.setObject(sub);
                JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), field.getName(), Dialog.ModalityType.APPLICATION_MODAL);
                dialog.getContentPane().add(new JScrollPane(subPanel), BorderLayout.CENTER);
                JButton ok = new JButton("OK");
                ok.addActionListener(ev -> {
                    try {
                        Object newSub = subPanel.getObject();
                        field.set(object, newSub);
                        markDirty(field);
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
            JButton removeBtn = new JButton("Remove");
            removeBtn.addActionListener(e -> {
                try {
                    if (object != null) {
                        field.set(object, null);
                        markDirty(field);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            btnPanel.add(editBtn);
            btnPanel.add(removeBtn);
            editor = btnPanel;
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
                                Object newVal = (item instanceof Integer ? Integer.parseInt(input)
                                    : item instanceof Long ? Long.parseLong(input)
                                    : item instanceof Double ? Double.parseDouble(input)
                                    : input);
                                model.set(idx, newVal);
                                markDirty(field);
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
                                    markDirty(field);
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
                        markDirty(field);
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
                ParameterizedType addPt = (ParameterizedType) field.getGenericType();
                Class<?> addElem = (Class<?>) addPt.getActualTypeArguments()[0];
                Object inst = instantiateSubtype(addElem);
                if (inst != null) {
                    model.addElement(inst);
                    markDirty(field);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GenericObjectEditorPanel.this,
                    "Error adding element: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        remove.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx >= 0) {
                model.remove(idx);
                markDirty(field);
            }
        });
        return panel;
    }

    public void setObject(Object obj) {
        this.object = clazz.cast(obj);
        initialValues.clear();
        dirtyFields.clear();
        updating = true;
        for (int i = 0; i < fields.size(); i++) {
            Field f = fields.get(i);
            JComponent editor = editors.get(i);
            try {
                Object val = f.get(obj);
                initialValues.put(f, val);
                dirtyFields.put(f, Boolean.FALSE);
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
        updating = false;
    }

    public T getObject() {
        try {
            if (object == null) object = clazz.getDeclaredConstructor().newInstance();
            for (int i = 0; i < fields.size(); i++) {
                Field f = fields.get(i);
                JComponent editor = editors.get(i);
                boolean dirty = Boolean.TRUE.equals(dirtyFields.get(f));
                Object value = null;
                if (editor instanceof JCheckBox cb) {
                    if (dirty) value = cb.isSelected();
                } else if (editor instanceof JSpinner sp) {
                    if (dirty) value = sp.getValue();
                } else if (editor instanceof JTextField tf) {
                    String txt = tf.getText();
                    if (dirty) {
                        if (f.getType() == String.class) {
                            value = txt.isEmpty() ? null : txt;
                        } else if (f.getType() == int.class || f.getType() == Integer.class) {
                            value = txt.isEmpty() ? null : Integer.parseInt(txt);
                        } else if (f.getType() == long.class || f.getType() == Long.class) {
                            value = txt.isEmpty() ? null : Long.parseLong(txt);
                        } else if (f.getType() == double.class || f.getType() == Double.class) {
                            value = txt.isEmpty() ? null : Double.parseDouble(txt);
                        }
                    }
                } else if (editor instanceof JPanel listPanel && java.util.List.class.isAssignableFrom(f.getType())) {
                    JScrollPane sp = (JScrollPane) listPanel.getComponent(0);
                    JList<?> list = (JList<?>) sp.getViewport().getView();
                    java.util.List<Object> newList = new java.util.ArrayList<>();
                    DefaultListModel<?> model = (DefaultListModel<?>) list.getModel();
                    for (int idx = 0; idx < model.getSize(); idx++) {
                        newList.add(model.getElementAt(idx));
                    }
                    // Empty lists should be saved as null
                    value = newList.isEmpty() ? null : newList;
                    // For lists, we always set (to ensure [] -> null as required)
                    f.set(object, value);
                    continue;
                }
                if (value != null || (editor instanceof JCheckBox || editor instanceof JSpinner || editor instanceof JTextField)) {
                    // Only apply for dirty simple fields; value may be null intentionally (e.g., empty text -> null)
                    if (dirty) {
                        f.set(object, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    // Helper to instantiate a subtype for interfaces/abstracts
    private Object instantiateSubtype(Class<?> baseType) throws Exception {
        // default placeholder for String elements
        if (baseType == String.class) {
            return "Your value here";
        }
        if (baseType.isInterface() || Modifier.isAbstract(baseType.getModifiers())) {
            JsonSubTypes stAnn = baseType.getAnnotation(JsonSubTypes.class);
            if (stAnn == null || stAnn.value().length == 0) {
                JOptionPane.showMessageDialog(this, "No subtypes available for " + baseType.getSimpleName(), "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            JsonSubTypes.Type[] types = stAnn.value();
            String[] names = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                names[i] = (types[i].name() != null && !types[i].name().isEmpty())
                        ? types[i].name() : types[i].value().getSimpleName();
            }
            String sel = (String) JOptionPane.showInputDialog(
                    this, "Select type of " + baseType.getSimpleName(),
                    "Subtype", JOptionPane.PLAIN_MESSAGE,
                    null, names, names[0]);
            if (sel == null) return null;
            for (JsonSubTypes.Type t : types) {
                String tn = (t.name() != null && !t.name().isEmpty()) ? t.name() : t.value().getSimpleName();
                if (sel.equals(tn)) {
                    return t.value().getDeclaredConstructor().newInstance();
                }
            }
            return null;
        }
        return baseType.getDeclaredConstructor().newInstance();
    }

    private Object instantiateFieldInstance(Field field) throws Exception {
        // ensure parent object exists
        if (object == null) {
            object = clazz.getDeclaredConstructor().newInstance();
        }
        Object current = field.get(object);
        if (current != null) return current;
        return instantiateSubtype(field.getType());
    }
}
