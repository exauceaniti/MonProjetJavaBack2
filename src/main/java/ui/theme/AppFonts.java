package ui.theme;

import java.awt.*;

/**
 * Gestion centralisée des polices de l'application
 */
public final class AppFonts {
    // Polices principales
    public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBTITLE = new Font("Segoe UI", Font.PLAIN, 18);
    public static final Font NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BUTTON_TEXT = new Font("Segoe UI Semibold", Font.PLAIN, 14);

    // Polices secondaires
    public static final Font MONOSPACED = new Font("Consolas", Font.PLAIN, 14);

    // Tailles dynamiques
    public static Font dynamicFont(float size) {
        return NORMAL.deriveFont(size);
    }

    private AppFonts() {} // Empêche l'instanciation
}