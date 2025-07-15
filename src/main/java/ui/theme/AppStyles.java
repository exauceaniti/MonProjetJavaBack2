package ui.theme;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Styles prédéfinis pour les composants Swing
 */
public final class AppStyles {
    // Bordures
    public static final Border ROUNDED_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.BORDER, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
    );

    public static final Border EMPTY_BORDER_10 = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    // Style pour les boutons
    public static void styleButton(JButton button) {
        button.setFont(AppFonts.BUTTON_TEXT);
        button.setBackground(AppColors.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppColors.PRIMARY_DARK, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Effet au survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(AppColors.PRIMARY_LIGHT);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(AppColors.PRIMARY);
            }
        });
    }

    // Style pour les champs de texte
    public static void styleTextField(JTextField field) {
        field.setFont(AppFonts.NORMAL);
        field.setBorder(ROUNDED_BORDER);
        field.setBackground(AppColors.CARD_BACKGROUND);
    }

    // Style pour les labels de titre
    public static void styleTitleLabel(JLabel label) {
        label.setFont(AppFonts.TITLE);
        label.setForeground(AppColors.TEXT_PRIMARY);
    }

    private AppStyles() {} // Empêche l'instanciation
}
