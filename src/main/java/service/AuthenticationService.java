package service;

import com.mysql.cj.protocol.x.CompressionSplittedOutputStream;
import ui.AdminDashboard;
import ui.UserDashboard;
import utils.DBConnection;
import javax.swing.*;
import java.sql.*;

//Service d'authentification lors de la connexion.
public class AuthenticationService {
    private static final String AUTH_QUERY =
            "SELECT id, numero_telephone, role FROM comptes " +
                    "WHERE numero_telephone = ? AND mot_de_passe = SHA2(?, 256)";

    public void authenticateAndRedirect(String phoneNumber, String password, JFrame loginFrame)
            throws AuthenticationException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(AUTH_QUERY)) {

            stmt.setString(1, phoneNumber.trim());
            stmt.setString(2, password.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User.Builder(
                            rs.getInt("id"),
                            rs.getString("numero_telephone"),
                            rs.getString("role"))
                            .build();

                    // Fermer la fenêtre de login dans l'EDT
                    SwingUtilities.invokeLater(() -> {
                        loginFrame.dispose();

                        if (user.isAdmin()) {
                            System.out.println("Ouverture du dashboard admin...");
                            new AdminDashboard(user); // setVisible est déjà dans le constructeur
                        } else {
                            System.out.println("Ouverture du dashboard utilisateur...");
                            new UserDashboard(user);
                        }
                    });
                } else {
                    throw new AuthenticationException("Identifiants incorrects");
                }
            }
        } catch (SQLException e) {
            throw new AuthenticationException("Erreur de connexion à la base de données", e);
        }
    }
    public static class AuthenticationException extends Exception {
        public AuthenticationException(String message) {
            super(message);
        }

        public AuthenticationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}