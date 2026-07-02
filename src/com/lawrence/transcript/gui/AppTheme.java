package com.lawrence.transcript.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public final class AppTheme {
    public static final Color BACKGROUND = new Color(244, 247, 251);
    public static final Color SURFACE = Color.WHITE;
    public static final Color PRIMARY = new Color(22, 72, 99);
    public static final Color ACCENT = new Color(31, 128, 112);
    public static final Color TEXT = new Color(31, 41, 55);
    public static final Color MUTED = new Color(99, 115, 129);

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SECTION_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private AppTheme() {
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        label.setForeground(PRIMARY);
        return label;
    }

    public static JLabel section(String text) {
        JLabel label = new JLabel(text);
        label.setFont(SECTION_FONT);
        label.setForeground(TEXT);
        return label;
    }

    public static void styleButton(JButton button) {
        button.setFont(BODY_FONT);
        button.setFocusPainted(false);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
    }

    public static void styleSecondaryButton(JButton button) {
        button.setFont(BODY_FONT);
        button.setFocusPainted(false);
        button.setBackground(new Color(226, 232, 240));
        button.setForeground(TEXT);
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
    }

    public static void pad(JComponent component) {
        component.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
    }
}
