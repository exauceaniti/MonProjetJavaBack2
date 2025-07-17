//package panels;
//
//import dao.ContactDAO;
//import models.Contact;
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.util.List;
//
///**
// * Panneau de gestion des contacts fournissant une interface CRUD complète
// */
//public class ContactManagementPanel extends JPanel {
//    // DAO pour les opérations de persistance des contacts
//    private final ContactDAO contactDAO;
//    // Tableau d'affichage des contacts
//    private JTable contactTable;
//    // Modèle de données pour le tableau
//    private DefaultTableModel tableModel;
//
//    /**
//     * Constructeur initialisant l'interface de gestion des contacts
//     */
//    public ContactManagementPanel() {
//        this.contactDAO = new ContactDAO();
//        setLayout(new BorderLayout());
//        setBackground(Color.WHITE);
//
//        // Initialisation de la barre d'outils
//        initToolbar();
//
//        // Initialisation du tableau des contacts
//        add(new JScrollPane(contactTable), BorderLayout.CENTER);
//
//        // Chargement initial des données
//        refreshContactTable();
//    }
//
//    /**
//     * Initialise la barre d'outils avec les boutons d'actions
//     */
//    private void initToolbar() {
//        JToolBar toolBar = new JToolBar();
//        toolBar.setFloatable(false);
//
//        // Bouton d'ajout
//        JButton addButton = new JButton("Ajouter");
//        addButton.setToolTipText("Ajouter un nouveau contact");
//        addButton.addActionListener(this::showAddContactDialog);
//        toolBar.add(addButton);
//
//        // Bouton de modification
//        JButton editButton = new JButton("Modifier");
//        editButton.setToolTipText("Modifier le contact sélectionné");
//        editButton.addActionListener(this::showEditContactDialog);
//        toolBar.add(editButton);
//
//        // Bouton de suppression
//        JButton deleteButton = new JButton("Supprimer");
//        deleteButton.setToolTipText("Supprimer le contact sélectionné");
//        deleteButton.addActionListener(this::deleteSelectedContact);
//        toolBar.add(deleteButton);
//
//        // Bouton d'actualisation
//        JButton refreshButton = new JButton("Actualiser");
//        refreshButton.setToolTipText("Rafraîchir la liste des contacts");
//        refreshButton.addActionListener(e -> refreshContactTable());
//        toolBar.add(refreshButton);
//
//        // Champ et bouton de recherche
//        initSearchComponents(toolBar);
//        add(toolBar, BorderLayout.NORTH);
//    }
//
//    /**
//     * Initialise les composants de recherche
//     * @param toolBar Barre d'outils à laquelle ajouter les composants
//     */
//    private void initSearchComponents(JToolBar toolBar) {
//        JTextField searchField = new JTextField(20);
//        searchField.setToolTipText("Terme de recherche");
//        JButton searchButton = new JButton("Rechercher");
//        searchButton.setToolTipText("Rechercher des contacts");
//        searchButton.addActionListener(e -> searchContacts(searchField.getText()));
//
//        toolBar.add(Box.createHorizontalGlue());
//        toolBar.add(new JLabel("Recherche:"));
//        toolBar.add(searchField);
//        toolBar.add(searchButton);
//    }
//
//    /**
//     * Rafraîchit le contenu du tableau avec les contacts de la base
//     */
//    private void refreshContactTable() {
//        tableModel.setRowCount(0); // Réinitialise le tableau
//        List<Contact> contacts = contactDAO.getAllContacts(); // Récupère tous les contacts
//
//        // Ajoute chaque contact comme nouvelle ligne
//        for (Contact contact : contacts) {
//            Object[] row = {
//                    contact.getId(),
//                    contact.getNom(),
//                    contact.getPostnom(),
//                    contact.getEmail(),
//                    contact.getNumeroTelephone(),
//                    contact.getGenre(),
//                    contact.getAdresse()
//            };
//            tableModel.addRow(row);
//        }
//    }
//
//    /**
//     * Affiche le dialogue d'ajout d'un nouveau contact
//     * @param e Événement déclencheur
//     */
//    private void showAddContactDialog(ActionEvent e) {
//        // Création des champs du formulaire
//        JTextField nomField = new JTextField();
//        JTextField postnomField = new JTextField();
//        JTextField emailField = new JTextField();
//        JTextField telephoneField = new JTextField();
//        JTextField genreField = new JTextField();
//        JTextField adresseField = new JTextField();
//
//        // Structure des champs pour la boîte de dialogue
//        Object[] fields = {
//                "Nom:", nomField,
//                "Postnom:", postnomField,
//                "Email:", emailField,
//                "Téléphone:", telephoneField,
//                "Genre:", genreField,
//                "Adresse:", adresseField
//        };
//
//        // Affichage de la boîte de dialogue
//        int option = JOptionPane.showConfirmDialog(
//                this,
//                fields,
//                "Ajouter un nouveau contact",
//                JOptionPane.OK_CANCEL_OPTION
//        );
//
//        // Traitement si validation
//        if (option == JOptionPane.OK_OPTION) {
//            // Création du nouveau contact
//            Contact newContact = new Contact();
//            newContact.setNom(nomField.getText());
//            newContact.setPostnom(postnomField.getText());
//            newContact.setEmail(emailField.getText());
//            newContact.setNumeroTelephone(telephoneField.getText());
//            newContact.setGenre(genreField.getText());
//            newContact.setAdresse(adresseField.getText());
//            // TODO: Gestion de la photo avec JFileChooser
//
//            // Persistance et feedback
//            if (contactDAO.addContact(newContact)) {
//                JOptionPane.showMessageDialog(this, "Contact ajouté avec succès!");
//                refreshContactTable(); // Actualisation de l'affichage
//            } else {
//                JOptionPane.showMessageDialog(this,
//                        "Erreur lors de l'ajout du contact",
//                        "Erreur",
//                        JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
//
//    /**
//     * Affiche le dialogue de modification d'un contact existant
//     * @param e Événement déclencheur
//     */
//    private void showEditContactDialog(ActionEvent e) {
//        // Vérification de la sélection
//        int selectedRow = contactTable.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(this,
//                    "Veuillez sélectionner un contact à modifier",
//                    "Avertissement",
//                    JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        // Récupération du contact
//        int contactId = (int) tableModel.getValueAt(selectedRow, 0);
//        Contact contact = contactDAO.findById(contactId).orElse(null);
//
//        if (contact == null) {
//            JOptionPane.showMessageDialog(this,
//                    "Contact introuvable",
//                    "Erreur",
//                    JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        // Création des champs pré-remplis
//        JTextField nomField = new JTextField(contact.getNom());
//        JTextField postnomField = new JTextField(contact.getPostnom());
//        JTextField emailField = new JTextField(contact.getEmail());
//        JTextField telephoneField = new JTextField(contact.getNumeroTelephone());
//        JTextField genreField = new JTextField(contact.getGenre());
//        JTextField adresseField = new JTextField(contact.getAdresse());
//
//        // Structure des champs pour la boîte de dialogue
//        Object[] fields = {
//                "Nom:", nomField,
//                "Postnom:", postnomField,
//                "Email:", emailField,
//                "Téléphone:", telephoneField,
//                "Genre:", genreField,
//                "Adresse:", adresseField
//        };
//
//        // Affichage de la boîte de dialogue
//        int option = JOptionPane.showConfirmDialog(
//                this,
//                fields,
//                "Modifier le contact",
//                JOptionPane.OK_CANCEL_OPTION
//        );
//
//        // Traitement si validation
//        if (option == JOptionPane.OK_OPTION) {
//            // Mise à jour des propriétés
//            contact.setNom(nomField.getText());
//            contact.setPostnom(postnomField.getText());
//            contact.setEmail(emailField.getText());
//            contact.setNumeroTelephone(telephoneField.getText());
//            contact.setGenre(genreField.getText());
//            contact.setAdresse(adresseField.getText());
//
//            // Persistance et feedback
//            if (contactDAO.updateContact(contact)) {
//                JOptionPane.showMessageDialog(this, "Contact modifié avec succès!");
//                refreshContactTable(); // Actualisation de l'affichage
//            } else {
//                JOptionPane.showMessageDialog(this,
//                        "Erreur lors de la modification du contact",
//                        "Erreur",
//                        JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
//
//    /**
//     * Supprime le contact sélectionné après confirmation
//     * @param e Événement déclencheur
//     */
//    private void deleteSelectedContact(ActionEvent e) {
//        // Vérification de la sélection
//        int selectedRow = contactTable.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(this,
//                    "Veuillez sélectionner un contact à supprimer",
//                    "Avertissement",
//                    JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        // Récupération de l'ID
//        int contactId = (int) tableModel.getValueAt(selectedRow, 0);
//
//        // Demande de confirmation
//        int confirm = JOptionPane.showConfirmDialog(
//                this,
//                "Êtes-vous sûr de vouloir supprimer ce contact?",
//                "Confirmation de suppression",
//                JOptionPane.YES_NO_OPTION
//        );
//
//        // Traitement si confirmation
//        if (confirm == JOptionPane.YES_OPTION) {
//            if (contactDAO.deleteContact(contactId)) {
//                JOptionPane.showMessageDialog(this, "Contact supprimé avec succès!");
//                refreshContactTable(); // Actualisation de l'affichage
//            } else {
//                JOptionPane.showMessageDialog(this,
//                        "Erreur lors de la suppression du contact",
//                        "Erreur",
//                        JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
//
//    /**
//     * Filtre les contacts selon un terme de recherche
//     * @param searchTerm Terme à rechercher
//     */
//    private void searchContacts(String searchTerm) {
//        tableModel.setRowCount(0); // Réinitialise le tableau
//        List<Contact> contacts = contactDAO.searchContacts(searchTerm); // Recherche
//
//        // Ajout des résultats
//        for (Contact contact : contacts) {
//            Object[] row = {
//                    contact.getId(),
//                    contact.getNom(),
//                    contact.getPostnom(),
//                    contact.getEmail(),
//                    contact.getNumeroTelephone(),
//                    contact.getGenre(),
//                    contact.getAdresse()
//            };
//            tableModel.addRow(row);
//        }
//    }
//}