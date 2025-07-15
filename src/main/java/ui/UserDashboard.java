package ui;

import service.User;
import javax.swing.*;
import java.awt.*;

/**
 * Tableau de bord de l'utilisateur standard.
 */
public class UserDashboard extends JFrame {
    private final User user;

    /**
     * Construit le tableau de bord utilisateur.
     *
     * @param user L'utilisateur standard
     */
    public UserDashboard(User user) {
        this.user = user;
        initializeUI();
    }

    /**
     * Initialise l'interface utilisateur.
     */
    private void initializeUI() {
        // Configuration de la fenêtre
        setTitle("Tableau de bord - Utilisateur");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Barre de menu
        createMenuBar();

        // Contenu principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Message de bienvenue
        JLabel welcomeLabel = new JLabel(
                "<html><center><h1>Bienvenue</h1>" +
                        "<p>Connecté en tant que: " + user.getPhoneNumber() + "</p></center></html>",
                SwingConstants.CENTER
        );
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);

        add(mainPanel);
    }

    /**
     * Crée la barre de menu.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Fichier
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem logoutItem = new JMenuItem("Déconnexion");
        logoutItem.addActionListener(e -> logout());
        fileMenu.add(logoutItem);

        JMenuItem exitItem = new JMenuItem("Quitter");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Gère la déconnexion.
     */
    private void logout() {
        dispose();
        new LoginForm().setVisible(true);
    }
}