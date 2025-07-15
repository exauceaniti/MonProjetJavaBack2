package ui.theme;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;

/**
 * Gestionnaire de thème pour l'application
 */
public class ThemeManager {
    public static void applyTheme() {
        try {
            // Définition d'un thème Metal personnalisé
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme() {
                @Override
                public ColorUIResource getWindowTitleInactiveBackground() {
                    return new ColorUIResource(AppColors.PRIMARY);
                }

                @Override
                public ColorUIResource getWindowTitleBackground() {
                    return new ColorUIResource(AppColors.PRIMARY);
                }

                @Override
                public ColorUIResource getPrimaryControlHighlight() {
                    return new ColorUIResource(AppColors.PRIMARY_LIGHT);
                }
            });

            UIManager.setLookAndFeel(new MetalLookAndFeel());

            // Styles globaux
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.background", AppColors.PRIMARY);
            UIManager.put("Panel.background", AppColors.BACKGROUND);
            UIManager.put("Label.foreground", AppColors.TEXT_PRIMARY);
            UIManager.put("TextField.background", AppColors.CARD_BACKGROUND);
            UIManager.put("TextArea.font", AppFonts.MONOSPACED);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'application du thème: " + e.getMessage());
        }
    }
}
