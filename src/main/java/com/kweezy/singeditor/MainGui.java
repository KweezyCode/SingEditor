package com.kweezy.singeditor;

import com.formdev.flatlaf.FlatDarkLaf;
import com.kweezy.singeditor.config.SingBoxConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kweezy.singeditor.ui.GenericObjectEditorPanel;

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
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Create generic editor panel for SingBoxConfig
        editorPanel = new GenericObjectEditorPanel<>(SingBoxConfig.class);
        JScrollPane scrollPane = new JScrollPane(editorPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> saveFile());
        exitItem.addActionListener(e -> System.exit(0));
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
