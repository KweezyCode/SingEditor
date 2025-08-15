package com.kweezy.singeditor.ui.field;

import javax.swing.*;

/**
 * Contract for a single-field editor used by GenericObjectEditorPanel.
 * Implementations adapt a Swing component to edit one Java field/property,
 * while tracking if the user has actually changed the value (dirty state).
 *
 * Lifecycle and responsibilities:
 * - getComponent(): provide the Swing component to embed in the UI row.
 * - setValue(Object): programmatically load the current model value into the UI.
 *   Implementations typically guard internal listeners with an "updating" flag
 *   to avoid marking the editor dirty during programmatic updates.
 * - getValue(): read the current value from the UI. May return null when the
 *   component represents an intentionally empty value (e.g., empty text or empty list).
 * - isDirty(): return true only if the user performed a change since the last
 *   setValue(...) or clearDirty().
 * - clearDirty(): reset the dirty flag after external code acknowledges/saves changes.
 */
public interface FieldEditor {
    /**
     * @return the Swing component that visually represents the editor.
     */
    JComponent getComponent();

    /**
     * Programmatically set the editor value from the backing model.
     * Implementations should not mark the editor dirty for this call.
     * @param value the model value to display; may be null.
     */
    void setValue(Object value);

    /**
     * Read the current value from the UI. Implementations may return null to
     * represent an intentionally empty/cleared value.
     * @return the current UI value, possibly null.
     */
    Object getValue();

    /**
     * @return true if the user changed the UI after the last setValue(...) or clearDirty().
     */
    boolean isDirty();

    /**
     * Reset the dirty flag. Call after you persisted or acknowledged the change.
     */
    void clearDirty();
}
