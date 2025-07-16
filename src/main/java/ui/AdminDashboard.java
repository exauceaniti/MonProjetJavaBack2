package ui;

import panels.ContactTablePanel;
import service.User;
import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard(User user) {
        // Configuration de base ESSENTIELLE
        setTitle("Admin Dashboard - " + user.getPhoneNumber());
        setSize(1200, 800); // Taille appropriée
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre

        // Création du conteneur principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Barre de menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem exitItem = new JMenuItem("Quitter");
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Panel latéral (sidebar) avec scroll
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, -1)); // Hauteur automatique
        sidebar.setBackground(new Color(240, 240, 240));
        sidebar.setLayout(new GridLayout(10, 1, 0, 10));

        JButton contactsBtn = new JButton("Contacts");
        JButton statsBtn = new JButton("Statistiques");
        sidebar.add(contactsBtn);
        sidebar.add(statsBtn);

        // Panel de contenu avec CardLayout
        JPanel contentPanel = new JPanel(new CardLayout());
        try {
            contentPanel.add(new ContactTablePanel(), "contacts");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement des contacts: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Assemblage des composants
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Gestion des événements
        contactsBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout)(contentPanel.getLayout());
            cl.show(contentPanel, "contacts");
        });

        exitItem.addActionListener(e -> System.exit(0));

        // Ajout final
        add(mainPanel);
        pack(); // Ajuste la taille automatiquement
        setVisible(true); // ESSENTIEL!
    }
}