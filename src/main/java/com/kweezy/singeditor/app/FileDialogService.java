package com.kweezy.singeditor.app;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Optional;

public final class FileDialogService {
    public Optional<File> chooseOpenFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        int res = fileChooser.showOpenDialog(parent);
        if (res == JFileChooser.APPROVE_OPTION) {
            return Optional.of(fileChooser.getSelectedFile());
        }
        return Optional.empty();
    }

    public Optional<File> chooseSaveFile(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        int res = fileChooser.showSaveDialog(parent);
        if (res == JFileChooser.APPROVE_OPTION) {
            return Optional.of(fileChooser.getSelectedFile());
        }
        return Optional.empty();
    }
}
