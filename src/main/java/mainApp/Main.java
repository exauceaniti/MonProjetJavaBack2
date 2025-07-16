package mainApp;

import ui.LoginForm;
import ui.theme.ThemeManager;

/**
     * Classe principale pour lancer l'application
     */
    public class Main {
        public static void main(String[] args) {
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

