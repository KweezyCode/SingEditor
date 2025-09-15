package com.kweezy.singeditor.ui.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;

public class ScrollUtil {
    // Default unit increment in pixels
    private static final int DEFAULT_UNIT = 16;

    public static void configureScrollPane(JScrollPane sp) {
        if (sp == null) return;
        JScrollBar vb = sp.getVerticalScrollBar();
        if (vb != null) vb.setUnitIncrement(DEFAULT_UNIT);

        // Ensure mouse wheel scrolls by pixels consistently across components
        java.util.function.Consumer<MouseWheelEvent> handler = (MouseWheelEvent e) -> {
            if (vb == null) return;
            int rotation = e.getWheelRotation();
            int units = e.getScrollAmount();
            int delta = rotation * Math.max(1, units) * vb.getUnitIncrement();
            vb.setValue(vb.getValue() + delta);
            e.consume();
        };
        sp.addMouseWheelListener(handler::accept);

        // If viewport view is a JList without fixed cell height, leave as-is.
        Component view = sp.getViewport().getView();
        if (view instanceof JList) {
            JList<?> l = (JList<?>) view;
            int ch = l.getFixedCellHeight();
            if (ch <= 0) {
                // set a reasonable default so unitIncrement aligns with cell height
                l.setFixedCellHeight(DEFAULT_UNIT + 4);
            }
        }
        // Also attach same wheel handler to the viewport view (e.g. JTextArea) so events are handled
        if (view != null) {
            view.addMouseWheelListener(handler::accept);
        }
    }
}
