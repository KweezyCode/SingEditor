package com.kweezy.singeditor.ui;

import com.kweezy.singeditor.ui.util.ScrollUtil;
import com.kweezy.singeditor.ui.util.SubtypeFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Master-Detail editor for a List of objects.
 * Left: List of items.
 * Right: Editor for the selected item.
 */
public class ListEditorPanel<T> extends JPanel {
    private final Class<T> itemClass;
    private final DefaultListModel<T> listModel = new DefaultListModel<>();
    private final JList<T> list = new JList<>(listModel);
    private final JPanel detailContainer = new JPanel(new BorderLayout());
    private GenericObjectEditorPanel<T> currentEditor;
    private final Supplier<T> factory;

    public ListEditorPanel(Class<T> itemClass, Supplier<T> factory) {
        this.itemClass = itemClass;
        this.factory = factory;
        setLayout(new BorderLayout());

        // Left side: List + Toolbar
        JPanel leftPanel = new JPanel(new BorderLayout());
        JScrollPane listScroll = new JScrollPane(list);
        ScrollUtil.configureScrollPane(listScroll);
        // keep list items consistent and show placeholder for empty strings
        list.setFixedCellHeight(26);
        list.setCellRenderer(new EmptyAwareRenderer<>());
        leftPanel.add(listScroll, BorderLayout.CENTER);

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        
        JButton addBtn = new JButton("+");
        JButton removeBtn = new JButton("-");
        JButton upBtn = new JButton("↑");
        JButton downBtn = new JButton("↓");
        
        toolbar.add(addBtn);
        toolbar.add(removeBtn);
        toolbar.addSeparator();
        toolbar.add(upBtn);
        toolbar.add(downBtn);
        
        leftPanel.add(toolbar, BorderLayout.SOUTH);

        // Right side: Detail
        detailContainer.add(new JLabel("Select an item to edit", SwingConstants.CENTER), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, detailContainer);
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);

        // Events
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                T selected = list.getSelectedValue();
                editItem(selected);
            }
        });

        // Delete key support
        list.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
        list.getActionMap().put("delete", new AbstractAction() { @Override public void actionPerformed(ActionEvent e) { removeItem(); } });

        addBtn.addActionListener(e -> addItem());
        removeBtn.addActionListener(e -> removeItem());
        upBtn.addActionListener(e -> moveItem(-1));
        downBtn.addActionListener(e -> moveItem(1));
    }

    private void moveItem(int delta) {
        int idx = list.getSelectedIndex();
        if (idx == -1) return;
        int newIdx = idx + delta;
        if (newIdx < 0 || newIdx >= listModel.size()) return;
        
        T item = listModel.remove(idx);
        listModel.add(newIdx, item);
        list.setSelectedIndex(newIdx);
    }

    public ListEditorPanel(Class<T> itemClass) {
        this(itemClass, () -> {
            try {
                return itemClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private void addItem() {
        T newItem = null;
        // If factory is simple, use it. If we need subtype selection, we might need SubtypeFactory logic here.
        // For now, let's try to use SubtypeFactory if available or just the factory.
        try {
             newItem = (T) SubtypeFactory.instantiateSubtype(itemClass, this);
        } catch (Exception e) {
            newItem = factory.get();
        }

        if (newItem != null) {
            // If it's a simple String or Number, prompt for the actual value to avoid blank entries
            if (newItem instanceof String) {
                String input = JOptionPane.showInputDialog(this, "Value:", "", JOptionPane.PLAIN_MESSAGE);
                if (input == null) return;
                if (input.length() == 0) {
                    JOptionPane.showMessageDialog(this, "Empty value was not added.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                listModel.addElement((T) input);
                list.setSelectedValue((T) input, true);
                return;
            }
            if (newItem instanceof Number) {
                String input = JOptionPane.showInputDialog(this, "Value:", newItem.toString());
                if (input == null) return;
                try {
                    Number n;
                    if (newItem instanceof Integer) n = Integer.parseInt(input);
                    else if (newItem instanceof Long) n = Long.parseLong(input);
                    else n = Double.parseDouble(input);
                    listModel.addElement((T) n);
                    list.setSelectedValue((T) n, true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number", "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }

            listModel.addElement(newItem);
            list.setSelectedValue(newItem, true);
        }
    }

    private void removeItem() {
        int idx = list.getSelectedIndex();
        if (idx != -1) {
            listModel.remove(idx);
            detailContainer.removeAll();
            detailContainer.add(new JLabel("Select an item to edit", SwingConstants.CENTER), BorderLayout.CENTER);
            detailContainer.revalidate();
            detailContainer.repaint();
        }
    }

    private void editItem(T item) {
        detailContainer.removeAll();
        if (item != null) {
            // We need to handle polymorphism here. The item might be a subclass of T.
            // GenericObjectEditorPanel expects the exact class to show all fields.
            Class<T> actualClass = (Class<T>) item.getClass();
            
            currentEditor = new GenericObjectEditorPanel<>(actualClass);
            currentEditor.setObject(item);
            
            // Wrap in scroll pane
            JScrollPane sp = new JScrollPane(currentEditor);
            ScrollUtil.configureScrollPane(sp);
            detailContainer.add(sp, BorderLayout.CENTER);
        } else {
            detailContainer.add(new JLabel("Select an item to edit", SwingConstants.CENTER), BorderLayout.CENTER);
        }
        detailContainer.revalidate();
        detailContainer.repaint();
    }

    public void setList(List<T> items) {
        listModel.clear();
        if (items != null) {
            for (T item : items) {
                listModel.addElement(item);
            }
        }
    }

    public List<T> getList() {
        // Before returning, we should make sure the currently edited item is updated?
        // GenericObjectEditorPanel updates the object in place usually, or we call getObject().
        // But GenericObjectEditorPanel.getObject() creates a NEW instance.
        // We need to update the instance in the list model if GenericObjectEditorPanel creates new instances.
        
        // Let's check GenericObjectEditorPanel.getObject().
        // It creates a new instance. This is tricky for Master-Detail if we want to preserve identity or just update.
        // If we are editing 'item' in place, we are good.
        // But GenericObjectEditorPanel.setObject(obj) populates fields.
        // GenericObjectEditorPanel.getObject() reads fields and creates NEW object.
        
        // So we need to sync back the currently edited object to the list model.
        if (currentEditor != null && list.getSelectedIndex() != -1) {
            T updated = currentEditor.getObject();
            int idx = list.getSelectedIndex();
            listModel.set(idx, updated);
        }

        List<T> result = new ArrayList<>();
        for (int i = 0; i < listModel.size(); i++) {
            result.add(listModel.get(i));
        }
        return result;
    }

    // Simple renderer to show <empty> placeholder for empty strings/nulls
    private static class EmptyAwareRenderer<E> extends DefaultListCellRenderer {
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
            }
            return lbl;
        }
    }
}
