package ui;

import dao.ContactDAO;
import models.Contact;
import service.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UserDashboard extends JFrame {
    private final User user;
    private final ContactDAO contactDao;
    private JTable contactTable;
    private JTextField searchField;

    public UserDashboard(User user) {
        this.user = user;
        this.contactDao = new ContactDAO();
        initializeUI();
        loadInitialContacts();
    }

    private void initializeUI() {
        // Configuration de base
        setTitle("Annuaire de Contacts - " + user.getPhoneNumber());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Barre de recherche améliorée
        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // 2. Tableau de résultats avec rendu amélioré
        contactTable = new JTable();
        customizeTableAppearance();
        JScrollPane scrollPane = new JScrollPane(contactTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 3. Barre d'état en bas
        JLabel statusLabel = new JLabel(" Prêt");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        // Configuration du menu
        setupMenuBar();

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Champ de recherche avec placeholder
        searchField = new JTextField();
        searchField.setToolTipText("Rechercher par nom, postnom ou numéro");
        searchField.putClientProperty("JTextField.placeholderText", "Entrez un nom, postnom ou numéro...");

        // Bouton de recherche avec icône
        JButton searchButton = new JButton("Rechercher", new ImageIcon("icons/search.png"));
        searchButton.setPreferredSize(new Dimension(120, 30));

        // Gestion des événements
        searchButton.addActionListener(this::performSearch);
        searchField.addActionListener(this::performSearch);

        panel.add(searchField, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.EAST);

        return panel;
    }

    private void customizeTableAppearance() {
        contactTable.setRowHeight(30);
        contactTable.setIntercellSpacing(new Dimension(10, 5));
        contactTable.setShowGrid(false);
        contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Fichier
        JMenu fileMenu = new JMenu("Fichier");
        fileMenu.setMnemonic('F');

        JMenuItem refreshItem = new JMenuItem("Actualiser", 'A');
        refreshItem.addActionListener(e -> loadInitialContacts());

        JMenuItem exitItem = new JMenuItem("Quitter", 'Q');
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void loadInitialContacts() {
        SwingWorker<List<Contact>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Contact> doInBackground() throws Exception {
                return contactDao.getAllContacts();
            }

            @Override
            protected void done() {
                try {
                    updateTable(get());
                } catch (Exception e) {
                    showError("Erreur de chargement", e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void performSearch(ActionEvent e) {
        String searchTerm = searchField.getText().trim();

        SwingWorker<List<Contact>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Contact> doInBackground() throws Exception {
                if (searchTerm.isEmpty()) {
                    return contactDao.getAllContacts();
                }
                return contactDao.searchContacts(searchTerm);
            }

            @Override
            protected void done() {
                try {
                    updateTable(get());
                } catch (Exception ex) {
                    showError("Erreur de recherche", ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void updateTable(List<Contact> contacts) {
        String[] columnNames = {"ID", "Nom", "Postnom", "Téléphone", "Email", "Genre"};
        Object[][] data = new Object[contacts.size()][columnNames.length];

        for (int i = 0; i < contacts.size(); i++) {
            Contact c = contacts.get(i);
            data[i] = new Object[]{
                    c.getId(),
                    c.getNom(),
                    c.getPostnom(),
                    c.getNumeroTelephone(),
                    c.getEmail(),
                    c.getGenre()
            };
        }

        contactTable.setModel(new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre le tableau non éditable
            }
        });
    }

    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }
}