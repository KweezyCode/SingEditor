package com.kweezy.singeditor.ui.field;

import com.kweezy.singeditor.ui.GenericObjectEditorPanel;
import com.kweezy.singeditor.ui.util.SubtypeFactory;
import com.kweezy.singeditor.ui.util.ScrollUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class ListFieldEditor extends AbstractFieldEditor {
    private final DefaultListModel<Object> model = new DefaultListModel<>();
    private final JList<Object> list = new JList<>(model);
    private final JPanel panel = new JPanel(new BorderLayout());
    private final Field field;
    private final Component parent;
    private final JScrollPane scroll;

    public ListFieldEditor(Field field, Component parent) {
        super(new JPanel(new BorderLayout()));
        this.field = field;
        this.parent = parent;
        this.scroll = new JScrollPane(list);
        ScrollUtil.configureScrollPane(this.scroll);
        list.setVisibleRowCount(5);
        // make list items visually consistent and render empty values
        list.setFixedCellHeight(26);
        list.setCellRenderer(new EmptyAwareRenderer());

        ((JPanel) this.component).add(scroll, BorderLayout.CENTER);

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setOrientation(JToolBar.VERTICAL);
        
        JButton addBtn = new JButton("+");
        JButton removeBtn = new JButton("-");
        JButton upBtn = new JButton("↑");
        JButton downBtn = new JButton("↓");
        
        Dimension btnSize = new Dimension(24, 24);
        for (JButton b : new JButton[]{addBtn, removeBtn, upBtn, downBtn}) {
            b.setPreferredSize(btnSize);
            b.setMinimumSize(btnSize);
            b.setMaximumSize(btnSize);
            b.setMargin(new Insets(0,0,0,0));
        }

        toolbar.add(addBtn);
        toolbar.add(removeBtn);
        toolbar.addSeparator();
        toolbar.add(upBtn);
        toolbar.add(downBtn);
        
        ((JPanel) this.component).add(toolbar, BorderLayout.EAST);

        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) editSelected();
            }
        });

        // Delete key support when list has focus
        list.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
        list.getActionMap().put("delete", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { onRemove(); }
        });

        addBtn.addActionListener(e -> onAdd());
        removeBtn.addActionListener(e -> onRemove());
        upBtn.addActionListener(e -> onMove(-1));
        downBtn.addActionListener(e -> onMove(1));

        updateListRows();
    }

    private void onMove(int delta) {
        int idx = list.getSelectedIndex();
        if (idx == -1) return;
        int newIdx = idx + delta;
        if (newIdx < 0 || newIdx >= model.getSize()) return;
        
        Object item = model.remove(idx);
        model.add(newIdx, item);
        list.setSelectedIndex(newIdx);
        markDirty();
    }

    private void updateListRows() {
        int size = model.getSize();
        int rows = Math.min(8, Math.max(1, size + 1)); // 0->1, 1->2, ..., cap at 8
        list.setVisibleRowCount(rows);

        Dimension d = list.getPreferredScrollableViewportSize();
        int width = Math.max(100, Math.min(400, d.width));
        scroll.setPreferredSize(new Dimension(width, d.height));
        list.revalidate();
        scroll.revalidate();
        component.revalidate();
    }

    private Class<?> elementClass() {
        try {
            ParameterizedType pt = (ParameterizedType) field.getGenericType();
            return (Class<?>) pt.getActualTypeArguments()[0];
        } catch (Exception e) { return Object.class; }
    }

    private void onAdd() {
        try {
            Class<?> elem = elementClass();
            Object inst = SubtypeFactory.instantiateSubtype(elem, parent);
            if (inst == null) return;
            if (inst instanceof String) {
                // Prompt user for string value instead of inserting empty string silently
                String input = JOptionPane.showInputDialog(parent, "Value:", "", JOptionPane.PLAIN_MESSAGE);
                if (input == null) return; // cancelled
                if (input.length() == 0) {
                    JOptionPane.showMessageDialog(parent, "Empty value was not added.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                model.addElement(input);
                updateListRows();
                markDirty();
                return;
            }
            if (inst instanceof Number) {
                String input = JOptionPane.showInputDialog(parent, "Value:", inst.toString());
                if (input == null) return;
                try {
                    Number n;
                    if (inst instanceof Integer) n = Integer.parseInt(input);
                    else if (inst instanceof Long) n = Long.parseLong(input);
                    else n = Double.parseDouble(input);
                    model.addElement(n);
                    updateListRows();
                    markDirty();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent, "Invalid number", "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            if (!(inst instanceof String) && !(inst instanceof Number)) {
                GenericObjectEditorPanel<?> sub = new GenericObjectEditorPanel<>(inst.getClass());
                sub.setObject(inst);
                JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(parent), field.getName(), Dialog.ModalityType.APPLICATION_MODAL);
                JScrollPane sp = new JScrollPane(sub);
                ScrollUtil.configureScrollPane(sp);
                dlg.getContentPane().add(sp, BorderLayout.CENTER);
                final boolean[] saved = { false };
                JButton save = new JButton("Save");
                save.addActionListener(ev -> { saved[0] = true; dlg.dispose(); });
                dlg.getContentPane().add(save, BorderLayout.SOUTH);
                dlg.pack(); dlg.setLocationRelativeTo(parent); dlg.setVisible(true);
                if (saved[0]) {
                    inst = sub.getObject();
                    model.addElement(inst);
                    updateListRows();
                    markDirty();
                }
            } else {
                model.addElement(inst);
                updateListRows();
                markDirty();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Error adding element: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelected() {
        int sel = list.getSelectedIndex();
        if (sel < 0) return;
        Object item = model.getElementAt(sel);
        if (item instanceof String || item instanceof Number) {
            String input = JOptionPane.showInputDialog(parent, "Edit value:", item.toString());
            if (input != null) {
                Object newVal = (item instanceof Integer ? Integer.parseInt(input)
                        : item instanceof Long ? Long.parseLong(input)
                        : item instanceof Double ? Double.parseDouble(input)
                        : input);
                model.set(sel, newVal);
                updateListRows();
                markDirty();
            }
        } else {
            GenericObjectEditorPanel<?> sub = new GenericObjectEditorPanel<>(item.getClass());
            sub.setObject(item);
            JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(parent), field.getName(), Dialog.ModalityType.APPLICATION_MODAL);
            JScrollPane sp = new JScrollPane(sub);
            ScrollUtil.configureScrollPane(sp);
            dlg.getContentPane().add(sp, BorderLayout.CENTER);
            final boolean[] saved = { false };
            JButton save = new JButton("Save");
            save.addActionListener(ev -> { saved[0] = true; dlg.dispose(); });
            dlg.getContentPane().add(save, BorderLayout.SOUTH);
            dlg.pack(); dlg.setLocationRelativeTo(parent); dlg.setVisible(true);
            if (saved[0]) {
                model.set(sel, sub.getObject());
                updateListRows();
                markDirty();
            }
        }
    }

    private void onRemove() {
        int idx = list.getSelectedIndex();
        if (idx >= 0) {
            model.remove(idx);
            updateListRows();
            markDirty();
        }
    }

    @Override
    public void setValue(Object value) {
        try {
            updating = true;
            model.clear();
            if (value instanceof List<?> l) { for (Object e : l) model.addElement(e); }
        } finally { updating = false; }
        updateListRows();
    }

    @Override
    public Object getValue() {
        List<Object> out = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) out.add(model.getElementAt(i));
        return out.isEmpty() ? null : out;
    }

    // Renderer that shows a placeholder for empty strings or nulls and keeps visuals consistent
    private class EmptyAwareRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value == null) {
                lbl.setText("<empty>");
                lbl.setFont(lbl.getFont().deriveFont(Font.ITALIC));
                lbl.setForeground(Color.GRAY);
            } else if (value instanceof String && ((String) value).length() == 0) {
                lbl.setText("<empty>");
                lbl.setFont(lbl.getFont().deriveFont(Font.ITALIC));
                lbl.setForeground(Color.GRAY);
            } else {
                lbl.setForeground(isSelected ? lbl.getForeground() : Color.BLACK);
            }
            return lbl;
        }
    }
}
