package mainApp;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import ui.LoginForm;
import ui.theme.ThemeManager;

/**
     * Classe principale pour lancer l'application
     */
    public class Main {
        public static void main(String[] args) {
            // Appliquer le thème avant de créer les fenêtres
            ThemeManager.applyTheme();

            // Utilisation de SwingUtilities pour le thread-safe
            javax.swing.SwingUtilities.invokeLater(() -> {
                LoginForm loginForm = new LoginForm();
                loginForm.setVisible(true);

                // Centrer la fenêtre
                loginForm.setLocationRelativeTo(null);
            });
        }
    }

