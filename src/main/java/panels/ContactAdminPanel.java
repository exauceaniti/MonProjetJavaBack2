package panels;

import dao.ContactDAO;
import models.Contact;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * ContactManagerPanel
 * Composant réutilisable pour gérer : Ajouter / Modifier / Supprimer / Actualiser un contact.
 * Utilise les méthodes de ContactDAO et interagit avec ContactTablePanel.
 */
public class ContactManagerPanel extends JPanel {

    private final JTextField txtNom = new JTextField();
    private final JTextField txtPostnom = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtTel = new JTextField();
    private final JTextField txtGenre = new JTextField();
    private final JTextField txtAdresse = new JTextField();

    private final ContactDAO contactDAO = new ContactDAO();
    private final ContactTablePanel tablePanel;

    private Contact selectedContact = null; // 🧠 Contact en cours d'édition

    public ContactManagerPanel(ContactTablePanel tablePanel) {
        this.tablePanel = tablePanel;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("⚙️ Gestion du contact sélectionné"));

        JPanel formPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Nom"));
        formPanel.add(new JLabel("Postnom"));
        formPanel.add(new JLabel("Email"));
        formPanel.add(new JLabel("Téléphone"));
        formPanel.add(new JLabel("Genre"));
        formPanel.add(new JLabel("Adresse"));

        formPanel.add(txtNom);
        formPanel.add(txtPostnom);
        formPanel.add(txtEmail);
        formPanel.add(txtTel);
        formPanel.add(txtGenre);
        formPanel.add(txtAdresse);

        add(formPanel, BorderLayout.CENTER);

        // 🔘 Boutons d'action
        JButton btnAdd = new JButton("Ajouter");
        JButton btnUpdate = new JButton("Modifier");
        JButton btnDelete = new JButton("Supprimer");
        JButton btnRefresh = new JButton("Actualiser");

        btnAdd.addActionListener(e -> ajouterContact());
        btnUpdate.addActionListener(e -> modifierContact());
        btnDelete.addActionListener(e -> supprimerContact());
        btnRefresh.addActionListener(e -> actualiserTable());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);

        add(btnPanel, BorderLayout.SOUTH);

        // 🖱️ Sélection depuis la table : pré-remplit le formulaire
        tablePanel.getContactTable().getSelectionModel().addListSelectionListener(e -> {
            int row = tablePanel.getContactTable().getSelectedRow();
            if (row >= 0) {
                int id = (int) tablePanel.getTableModel().getValueAt(row, 0);
                selectedContact = contactDAO.findById(id).orElse(null);
                if (selectedContact != null) {
                    remplirFormulaire(selectedContact);
                }
            }
        });
    }

    private void ajouterContact() {
        Contact contact = lireFormulaire();
        if (contact == null) return;

        boolean success = contactDAO.addContact(contact);
        if (success) {
            JOptionPane.showMessageDialog(this, "✅ Contact ajouté !");
            actualiserTable();
            nettoyerFormulaire();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Échec de l'ajout.");
        }
    }

    private void modifierContact() {
        if (selectedContact == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contact.");
            return;
        }

        Contact updated = lireFormulaire();
        if (updated == null) return;
        updated.setId(selectedContact.getId());
        updated.setPhotoContact(0); // à ajuster si nécessaire

        boolean success = contactDAO.updateContact(updated);
        if (success) {
            JOptionPane.showMessageDialog(this, "✏️ Contact modifié !");
            actualiserTable();
            nettoyerFormulaire();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Échec de la modification.");
        }
    }

    private void supprimerContact() {
        if (selectedContact == null) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un contact à supprimer.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer ce contact ?", "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = contactDAO.deleteContact(selectedContact.getId());
            if (success) {
                JOptionPane.showMessageDialog(this, "🗑️ Contact supprimé !");
                actualiserTable();
                nettoyerFormulaire();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Échec de la suppression.");
            }
        }
    }

    private void actualiserTable() {
        tablePanel.refresh();
    }

    private void nettoyerFormulaire() {
        txtNom.setText("");
        txtPostnom.setText("");
        txtEmail.setText("");
        txtTel.setText("");
        txtGenre.setText("");
        txtAdresse.setText("");
        selectedContact = null;
    }

    private Contact lireFormulaire() {
        String nom = txtNom.getText().trim();
        String postnom = txtPostnom.getText().trim();
        String email = txtEmail.getText().trim();
        String tel = txtTel.getText().trim();
        String genre = txtGenre.getText().trim();
        String adresse = txtAdresse.getText().trim();

        if (nom.isEmpty() || postnom.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Champs obligatoires manquants.");
            return null;
        }

        Contact c = new Contact();
        c.setNom(nom);
        c.setPostnom(postnom);
        c.setEmail(email);
        c.setNumeroTelephone(tel);
        c.setGenre(genre);
        c.setAdresse(adresse);
        c.setPhotoContact(0);
        return c;
    }

    private void remplirFormulaire(Contact c) {
        txtNom.setText(c.getNom());
        txtPostnom.setText(c.getPostnom());
        txtEmail.setText(c.getEmail());
        txtTel.setText(c.getNumeroTelephone());
        txtGenre.setText(c.getGenre());
        txtAdresse.setText(c.getAdresse());
    }
}
