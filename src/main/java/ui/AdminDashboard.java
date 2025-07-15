package ui;

import service.User;
import javax.swing.*;
import java.awt.*;

/**
 * Tableau de bord de l'administrateur.
 */
public class AdminDashboard extends JFrame {
    private final User user;

    /**
     * Construit le tableau de bord administrateur.
     *
     * @param user L'utilisateur administrateur
     */
    public AdminDashboard(User user) {
        this.user = user;
        initializeUI();
    }

    /**
     * Initialise l'interface utilisateur.
     */
    private void initializeUI() {
        // Configuration de la fenêtre
        setTitle("Tableau de bord - Administrateur");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Barre de menu
        createMenuBar();

        // Contenu principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Message de bienvenue
        JLabel welcomeLabel = new JLabel(
                "<html><center><h1>Bienvenue, Administrateur</h1>" +
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
        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Quitter");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Menu Administration
        JMenu adminMenu = new JMenu("Administration");
        JMenuItem usersItem = new JMenuItem("Gérer les utilisateurs");
        adminMenu.add(usersItem);

        menuBar.add(fileMenu);
        menuBar.add(adminMenu);

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