package ui;

import service.AuthenticationService;
import service.AuthenticationService.AuthenticationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * Formulaire de connexion avec validation des identifiants.
 */
public class LoginForm extends JFrame {
    private final JTextField phoneField;
    private final JPasswordField passwordField;
    private final AuthenticationService authService;

    /**
     * Construit le formulaire de connexion.
     */
    public LoginForm() {
        // Configuration de la fenêtre
        setTitle("Authentification - GestionContacts");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Initialisation des composants
        authService = new AuthenticationService();
        phoneField = new JTextField(15);
        passwordField = new JPasswordField(15);

        // Création du layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Titre
        JLabel titleLabel = new JLabel("Connexion", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel des champs
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 10));
        formPanel.add(new JLabel("Téléphone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Mot de passe:"));
        formPanel.add(passwordField);

        // Bouton de connexion
        JButton loginButton = new JButton("Se connecter");
        loginButton.addActionListener(this::handleLogin);
        formPanel.add(new JLabel()); // Espace vide
        formPanel.add(loginButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Ajout d'un footer avec version
        JLabel versionLabel = new JLabel("v1.0.0", SwingConstants.RIGHT);
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        mainPanel.add(versionLabel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Gère l'événement de connexion.
     */
    private void handleLogin(ActionEvent event) {
        String phone = phoneField.getText().trim();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        try {
            // Validation des champs
            if (phone.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("Tous les champs sont obligatoires");
            }

            // Tentative d'authentification
            authService.authenticateAndRedirect(phone, password, this); // Ajout du paramètre this (la JFrame actuelle)

            // Nettoyage des champs sensibles
            Arrays.fill(passwordChars, '\0');
            phoneField.setText("");
            passwordField.setText("");

        } catch (AuthenticationException e) {
            showError("Échec de l'authentification", e.getMessage());
        } catch (IllegalArgumentException e) {
            showError("Erreur de validation", e.getMessage());
        }
    }

    /**
     * Affiche un message d'erreur.
     */
    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            LoginForm loginForm = new LoginForm();
//            loginForm.setVisible(true);
//        });
//    }
}