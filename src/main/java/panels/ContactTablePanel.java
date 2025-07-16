package panels;

import models.Contact;
import service.ContactService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ContactTablePanel extends JPanel {
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private ContactService contactService;

    public ContactTablePanel() {
        contactService = new ContactService();
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Création du modèle de table avec colonnes
        String[] columnNames = {"ID", "Nom", "Post-Nom", "Email", "Téléphone", "Genre", "Adresse"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Toutes les cellules non éditables
            }
        };

        // Création du tableau
        contactTable = new JTable(tableModel);

        // Style de base du tableau
        contactTable.setRowHeight(30);
        contactTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        contactTable.setFont(new Font("Arial", Font.PLAIN, 12));
        contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Ajout de la barre de défilement
        JScrollPane scrollPane = new JScrollPane(contactTable);
        add(scrollPane, BorderLayout.CENTER);

        // Chargement des données
        loadContactData();
    }

    private void loadContactData() {
        // Vider le modèle actuel
        tableModel.setRowCount(0);

        try {
            List<Contact> contacts = contactService.getAllContacts();

            for (Contact contact : contacts) {
                Object[] rowData = {
                        contact.getId(),
                        contact.getNom(),
                        contact.getPostnom(),
                        contact.getEmail(),
                        contact.getNumeroTelephone(),
                        contact.getGenre(),
                        contact.getAdresse()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des contacts: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}