package com.kweezy.singeditor;

import com.formdev.flatlaf.FlatDarkLaf;
import com.kweezy.singeditor.config.SingBoxConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.kweezy.singeditor.ui.GenericObjectEditorPanel;
import com.kweezy.singeditor.importer.ImportResult;
import com.kweezy.singeditor.importer.ImportService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainGui extends JFrame {

    private final ObjectMapper objectMapper;
    private final GenericObjectEditorPanel<SingBoxConfig> editorPanel;

    public MainGui() {
        setTitle("Sing Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Setup ObjectMapper
        objectMapper = new ObjectMapper();

        // Accept single value as array globally (@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY))
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Create generic editor panel for SingBoxConfig
        editorPanel = new GenericObjectEditorPanel<>(SingBoxConfig.class);
        JScrollPane scrollPane = new JScrollPane(editorPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu importMenu = new JMenu("Import");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem importVlessItem = new JMenuItem("VLESS URIs...");

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        importMenu.add(importVlessItem);
        menuBar.add(fileMenu);
        menuBar.add(importMenu);

        setJMenuBar(menuBar);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> saveFile());
        exitItem.addActionListener(e -> System.exit(0));
        importVlessItem.addActionListener(e -> importVlessDialog());
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // Load JSON into object and update editor
                SingBoxConfig config = objectMapper.readValue(file, SingBoxConfig.class);
                editorPanel.setObject(config);
                editorPanel.revalidate();
                editorPanel.repaint();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading or parsing file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // Get updated object from editor and save as JSON
                SingBoxConfig updated = editorPanel.getObject();
                objectMapper.writeValue(file, updated);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error parsing or saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importVlessDialog() {
        JTextArea textArea = new JTextArea(15, 80);
        JScrollPane sp = new JScrollPane(textArea);
        int res = JOptionPane.showConfirmDialog(this, sp, "Paste VLESS URIs (one per line)", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;
        String input = textArea.getText();
        if (input == null || input.trim().isEmpty()) return;

        // Use ImportService to import any supported outbound lines (currently VLESS)
        ImportService service = new ImportService();
        SingBoxConfig cfg = editorPanel.getMutableObject();
        ImportResult result = service.importText(input, cfg);

        // Refresh editor view
        editorPanel.setObject(cfg);
        editorPanel.revalidate();
        editorPanel.repaint();

        String msg = "Imported: " + result.getImported() + (result.getFailed() > 0 ? ", failed: " + result.getFailed() : "");
        if (result.getErrors() != null && !result.getErrors().isEmpty()) {
            int show = Math.min(10, result.getErrors().size());
            msg += "\n" + String.join("\n", result.getErrors().subList(0, show));
        }
        JOptionPane.showMessageDialog(this, msg, "Import", JOptionPane.INFORMATION_MESSAGE);
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
