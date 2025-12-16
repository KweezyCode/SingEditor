package com.kweezy.singeditor;

import com.formdev.flatlaf.FlatDarkLaf;
import com.kweezy.singeditor.config.SingBoxConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.kweezy.singeditor.ui.GenericObjectEditorPanel;
import com.kweezy.singeditor.importer.ImportResult;
import com.kweezy.singeditor.importer.ImportService;
import com.kweezy.singeditor.ui.SingBoxConfigEditorPanel;
import com.kweezy.singeditor.ui.util.ScrollUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainGui extends JFrame {

    private final ObjectMapper objectMapper;
    private final SingBoxConfigEditorPanel editorPanel;

    public MainGui() {
        setTitle("Sing Editor");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Setup ObjectMapper
        objectMapper = new ObjectMapper();

        // Accept single value as array globally (@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY))
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Create specialized editor panel for SingBoxConfig
        editorPanel = new SingBoxConfigEditorPanel();
        
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
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // Load JSON into object and update editor
                SingBoxConfig config = objectMapper.readValue(file, SingBoxConfig.class);
                editorPanel.setConfig(config);
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
                SingBoxConfig updated = editorPanel.getConfig();
                objectMapper.writeValue(file, updated);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error parsing or saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importVlessDialog() {
        JTextArea textArea = new JTextArea(15, 80);
        JScrollPane sp = new JScrollPane(textArea);
        ScrollUtil.configureScrollPane(sp);
        int res = JOptionPane.showConfirmDialog(this, sp, "Paste URIs (one per line)", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;
        String input = textArea.getText();
        if (input == null || input.trim().isEmpty()) return;

        // Use ImportService to import any supported outbound lines (currently VLESS)
        ImportService service = new ImportService();
        SingBoxConfig cfg = editorPanel.getConfig();
        ImportResult result = service.importText(input, cfg);

        // Refresh editor view
        editorPanel.setConfig(cfg);
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
