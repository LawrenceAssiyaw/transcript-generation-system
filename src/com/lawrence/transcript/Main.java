package com.lawrence.transcript;

import com.lawrence.transcript.gui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // The default Swing look and feel is acceptable if the system one is unavailable.
            }
            new MainFrame().setVisible(true);
        });
    }
}
