package com.kweezy.singeditor;

import com.formdev.flatlaf.FlatDarkLaf;
import com.kweezy.singeditor.app.ConfigFileService;
import com.kweezy.singeditor.app.FileDialogService;
import com.kweezy.singeditor.app.ImportDialogService;
import com.kweezy.singeditor.app.Result;
import com.kweezy.singeditor.config.SingBoxConfig;
import com.kweezy.singeditor.importer.ImportResult;
import com.kweezy.singeditor.importer.ImportService;
import com.kweezy.singeditor.ui.SingBoxConfigEditorPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class MainGui extends JFrame {
    private final SingBoxConfigEditorPanel editorPanel;
    private final ConfigFileService configFileService;
    private final FileDialogService fileDialogService;
    private final ImportDialogService importDialogService;
    private final ImportService importService;

    public MainGui() {
        setTitle("Sing Editor");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create specialized editor panel for SingBoxConfig
        editorPanel = new SingBoxConfigEditorPanel();
        configFileService = ConfigFileService.defaultService();
        fileDialogService = new FileDialogService();
        importDialogService = new ImportDialogService();
        importService = ImportService.defaultService();
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu importMenu = new JMenu("Import");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem importVlessItem = new JMenuItem("URIs...");

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        importMenu.add(importVlessItem);
        menuBar.add(fileMenu);
        menuBar.add(importMenu);

        setJMenuBar(menuBar);
        add(editorPanel, BorderLayout.CENTER);

        // Add action listeners
        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> saveFile());
        exitItem.addActionListener(e -> System.exit(0));
        importVlessItem.addActionListener(e -> importVlessDialog());
    }

    private void openFile() {
        fileDialogService.chooseOpenFile(this).ifPresent(file -> {
            Result<SingBoxConfig> result = configFileService.load(file);
            result.ifOk(this::refreshEditor)
                  .ifError(msg -> showError("Error", msg));
        });
    }

    private void saveFile() {
        fileDialogService.chooseSaveFile(this).ifPresent(file -> {
            SingBoxConfig updated = editorPanel.getConfig();
            configFileService.save(file, updated)
                    .ifError(msg -> showError("Error", msg));
        });
    }

    private void importVlessDialog() {
        Optional<String> input = importDialogService.promptImportText(this);
        if (input.isEmpty()) return;

        SingBoxConfig cfg = editorPanel.getConfig();
        ImportResult result = importService.importText(input.get(), cfg);
        refreshEditor(cfg);

        String msg = "Imported: " + result.getImported() + (result.getFailed() > 0 ? ", failed: " + result.getFailed() : "");
        if (result.getErrors() != null && !result.getErrors().isEmpty()) {
            int show = Math.min(10, result.getErrors().size());
            msg += "\n" + String.join("\n", result.getErrors().subList(0, show));
        }
        JOptionPane.showMessageDialog(this, msg, "Import", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshEditor(SingBoxConfig config) {
        editorPanel.setConfig(config);
        editorPanel.revalidate();
        editorPanel.repaint();
    }

    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            new MainGui().setVisible(true);
        });
    }
}
