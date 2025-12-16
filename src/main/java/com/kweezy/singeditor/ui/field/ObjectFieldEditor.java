package com.kweezy.singeditor.ui.field;

import com.kweezy.singeditor.ui.GenericObjectEditorPanel;
import com.kweezy.singeditor.ui.util.SubtypeFactory;

import javax.swing.*;
import java.awt.*;

public class ObjectFieldEditor extends AbstractFieldEditor {
    private Object value;
    private final String fieldName;
    private final Class<?> fieldType;
    private final Component parent;
    
    private final JPanel headerPanel;
    private final JPanel contentPanel;
    private final JToggleButton toggleBtn;
    private final JLabel statusLabel;
    private final JButton removeBtn;
    
    private GenericObjectEditorPanel<?> inlineEditor;

    public ObjectFieldEditor(String fieldName, Class<?> fieldType, Component parent) {
        super(new JPanel(new BorderLayout()));
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.parent = parent;
        
        // Header
        headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        headerPanel.setOpaque(false);
        
        toggleBtn = new JToggleButton("▶");
        toggleBtn.setMargin(new Insets(0, 4, 0, 4));
        toggleBtn.setPreferredSize(new Dimension(24, 24));
        toggleBtn.setFocusable(false);
        
        statusLabel = new JLabel("Not set");
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC));
        
        removeBtn = new JButton("×");
        removeBtn.setMargin(new Insets(0, 4, 0, 4));
        removeBtn.setPreferredSize(new Dimension(24, 24));
        removeBtn.setToolTipText("Remove object");
        
        headerPanel.add(toggleBtn);
        headerPanel.add(statusLabel);
        headerPanel.add(removeBtn);
        
        // Content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setVisible(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0)); // Indent
        
        ((JPanel) this.component).add(headerPanel, BorderLayout.NORTH);
        ((JPanel) this.component).add(contentPanel, BorderLayout.CENTER);
        
        toggleBtn.addActionListener(e -> onToggle());
        removeBtn.addActionListener(e -> onRemove());
        
        updateState();
    }

    private void updateState() {
        if (value == null) {
            statusLabel.setText("Null");
            toggleBtn.setText("+");
            toggleBtn.setSelected(false);
            removeBtn.setVisible(false);
            contentPanel.setVisible(false);
        } else {
            statusLabel.setText(value.getClass().getSimpleName());
            toggleBtn.setText(contentPanel.isVisible() ? "▼" : "▶");
            removeBtn.setVisible(true);
        }
        component.revalidate();
        component.repaint();
    }

    private void onToggle() {
        if (value == null) {
            // Create
            try {
                Object inst = SubtypeFactory.instantiateSubtype(fieldType, parent);
                if (inst != null) {
                    value = inst;
                    markDirty();
                    // Auto-expand
                    createInlineEditor();
                    contentPanel.setVisible(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Error creating object: " + ex.getMessage());
            }
        } else {
            // Toggle visibility
            boolean visible = !contentPanel.isVisible();
            contentPanel.setVisible(visible);
            if (visible && inlineEditor == null) {
                createInlineEditor();
            }
        }
        updateState();
    }
    
    private void createInlineEditor() {
        if (value == null) return;
        contentPanel.removeAll();
        inlineEditor = new GenericObjectEditorPanel<>(value.getClass());
        inlineEditor.setObject(value);
        contentPanel.add(inlineEditor, BorderLayout.CENTER);

        // Footer with Save button to persist changes made in the inline editor
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        JButton saveBtn = new JButton("Save");
        footer.add(saveBtn);
        contentPanel.add(footer, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            if (inlineEditor != null) {
                Object updated = inlineEditor.getObject();
                value = updated;
                markDirty();
                updateState();
            }
        });
        contentPanel.revalidate();
    }

    private void onRemove() {
        value = null;
        inlineEditor = null;
        contentPanel.removeAll();
        updateState();
        markDirty();
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
        // If we have an editor, update it or discard it if type changed
        if (inlineEditor != null && value != null && inlineEditor.getClass().equals(value.getClass())) {
             // Same type, update
             inlineEditor.setObject(value);
        } else {
             // Different type or null, reset editor
             inlineEditor = null;
             contentPanel.removeAll();
        }
        // Collapse by default when setting new value from outside? Or keep state?
        // Let's keep collapsed to avoid clutter unless it was already open?
        // For now, collapse.
        contentPanel.setVisible(false);
        updateState();
    }

    @Override
    public Object getValue() {
        if (value != null && inlineEditor != null) {
            // Update value from editor
            return inlineEditor.getObject();
        }
        return value;
    }
}
