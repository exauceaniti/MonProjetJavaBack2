package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    // Paramètres de connexion (à configurer selon votre environnement)
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/GestionContact?" +
            "useSSL=false&serverTimezone=UTC&zeroDateTimeBehavior=convertToNull";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // Instance unique de la connexion (singleton)
    private static Connection connection;

    // Constructeur privé pour empêcher l'instanciation
    private DBConnection() {}

    /**
     * Obtient une connexion à la base de données
     * @return Connection objet de connexion JDBC
     * @throws SQLException si la connexion échoue
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Chargement du driver (optionnel depuis JDBC 4.0 mais recommandé)
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Établissement de la connexion
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver JDBC introuvable", e);
            }
        }
        return connection;
    }

    /**
     * Ferme la connexion à la base de données
     * @throws SQLException si la fermeture échoue
     */
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * Teste si la connexion est active
     * @return boolean true si la connexion est valide
     */
    public static boolean isConnectionValid() {
        try {
            return connection != null
                    && !connection.isClosed()
                    && connection.isValid(2); // 2 secondes de timeout
        } catch (SQLException e) {
            return false;
        }
    }
}
