package com.kweezy.singeditor.ui.field;

import javax.swing.*;

/**
 * Base implementation of FieldEditor that encapsulates common state and behavior.
 *
 * Fields:
 * - component: the Swing UI component that visually represents this editor. Subclasses
 *   pass it via the constructor and may attach listeners to track user changes.
 * - dirty: a boolean flag that indicates whether the user has modified the value
 *   since the last programmatic load (setValue) or manual reset (clearDirty).
 * - updating: a guard flag for subclasses to temporarily suppress dirty-marking
 *   while performing programmatic updates of the UI (e.g., inside setValue).
 *
 * Subclasses should:
 * - Call markDirty() from their event listeners when the user changes the UI.
 * - Use the 'updating' guard when pushing model values to the UI so that the
 *   change does not mark the editor dirty.
 */
public abstract class AbstractFieldEditor implements FieldEditor {
    /**
     * The Swing component that renders and edits the field value.
     */
    protected final JComponent component;

    /**
     * Tracks whether this editor has been modified by the user since the last
     * setValue(...) or clearDirty().
     */
    private boolean dirty = false;

    /**
     * Guard to suppress dirty updates during programmatic UI changes.
     * Set to true before pushing model values into UI widgets, and reset after.
     */
    protected boolean updating = false;

    protected AbstractFieldEditor(JComponent component) {
        this.component = component;
    }

    @Override
    public JComponent getComponent() {
        return component;
    }

    /**
     * Mark this editor as modified by the user (no-op if currently updating programmatically).
     */
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
