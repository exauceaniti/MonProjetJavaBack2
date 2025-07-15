package service;

import service.User;
import ui.AdminDashboard;
import ui.UserDashboard;
import utils.DBConnection;

import javax.swing.*;
import java.sql.*;

/**
 * Service d'authentification des utilisateurs.
 * Gère la vérification des identifiants et la redirection selon le rôle.
 */
public class AuthenticationService {

    private static final String AUTH_QUERY =
            "SELECT id, numero_telephone, role FROM comptes " +
                    "WHERE numero_telephone = ? AND mot_de_passe = SHA2(?, 256)";

    /**
     * Authentifie un utilisateur et le redirige vers le dashboard approprié.
     *
     * @param phoneNumber Le numéro de téléphone de l'utilisateur
     * @param password Le mot de passe en clair (sera hashé avant comparaison)
     * @throws AuthenticationException Si l'authentification échoue
     */
    public void authenticateAndRedirect(String phoneNumber, String password) throws AuthenticationException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(AUTH_QUERY)) {

            // Paramétrage de la requête
            stmt.setString(1, phoneNumber.trim());
            stmt.setString(2, password.trim());

            // Exécution de la requête
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Création de l'objet User
                User user = new User.Builder(
                        rs.getInt("id"),
                        rs.getString("numero_telephone"),
                        rs.getString("role")
                ).build();

                // Redirection dans l'EDT
                SwingUtilities.invokeLater(() -> redirectUser(user));
            } else {
                throw new AuthenticationException("Identifiants incorrects");
            }
        } catch (SQLException e) {
            throw new AuthenticationException("Erreur de connexion à la base de données", e);
        }
    }

    /**
     * Redirige l'utilisateur vers le dashboard approprié selon son rôle.
     *
     * @param user L'utilisateur authentifié
     */
    private void redirectUser(User user) {
        switch (user.getRole().toLowerCase()) {
            case "admin":
                new AdminDashboard(user).setVisible(true);
                break;
            case "user":
                new UserDashboard(user).setVisible(true);
                break;
            default:
                throw new IllegalStateException("Rôle utilisateur inconnu: " + user.getRole());
        }
    }

    /**
     * Exception spécifique pour les échecs d'authentification.
     */
    public static class AuthenticationException extends Exception {
        public AuthenticationException(String message) {
            super(message);
        }

        public AuthenticationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}