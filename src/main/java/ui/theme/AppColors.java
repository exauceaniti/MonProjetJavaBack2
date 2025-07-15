package ui.theme;

import java.awt.*;

/**
 * Palette de couleurs centralisée pour l'application
 */
public final class AppColors {
    // Couleurs primaires
    public static final Color PRIMARY = new Color(41, 128, 185);
    public static final Color PRIMARY_DARK = new Color(32, 102, 148);
    public static final Color PRIMARY_LIGHT = new Color(52, 152, 219);

    // Couleurs secondaires
    public static final Color SECONDARY = new Color(243, 156, 18);
    public static final Color SECONDARY_DARK = new Color(230, 126, 34);

    // Arrière-plans
    public static final Color BACKGROUND = new Color(247, 247, 247);
    public static final Color CARD_BACKGROUND = Color.WHITE;

    // Texte
    public static final Color TEXT_PRIMARY = new Color(51, 51, 51);
    public static final Color TEXT_SECONDARY = new Color(119, 119, 119);

    // États
    public static final Color SUCCESS = new Color(46, 204, 113);
    public static final Color ERROR = new Color(231, 76, 60);
    public static final Color WARNING = new Color(241, 196, 15);

    // Bordures
    public static final Color BORDER = new Color(221, 221, 221);

    private AppColors() {} // Empêche l'instanciation
}
