package com.kweezy.singeditor.app;

import com.kweezy.singeditor.ui.util.ScrollUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public final class ImportDialogService {
    public Optional<String> promptImportText(Component parent) {
        JTextArea textArea = new JTextArea(15, 80);
        JScrollPane sp = new JScrollPane(textArea);
        ScrollUtil.configureScrollPane(sp);
        int res = JOptionPane.showConfirmDialog(parent, sp, "Paste URIs (one per line)", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return Optional.empty();
        String input = textArea.getText();
        if (input == null) return Optional.empty();
        String trimmed = input.trim();
        return trimmed.isEmpty() ? Optional.empty() : Optional.of(input);
    }
}
