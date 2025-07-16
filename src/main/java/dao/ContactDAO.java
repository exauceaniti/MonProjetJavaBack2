package dao;

import models.Contact;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactDAO {
    // Requêtes SQL préparées
    private static final String INSERT_SQL = "INSERT INTO contacts (nom, postnom, " +
            "email, numero_telephone, genre, adresse, photo_contact) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM contacts";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM contacts WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM contacts WHERE id = ?";
    private static final String SEARCH_SQL = "SELECT * FROM contacts WHERE nom LIKE ? " +
            "OR postnom LIKE ? OR numero_telephone LIKE ?";
    private static final String UPDATE_SQL = "UPDATE contacts SET nom = ?, " +
            "postnom = ?, email = ?, numero_telephone = ?, genre = ?, adresse = ?, " +
            "photo_contact = ? WHERE id = ?";

    /**
     * Méthode de recherche unifiée optimisée
     */
    public List<Contact> searchContacts(String searchTerm) {
        List<Contact> contacts = new ArrayList<>();

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllContacts();
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_SQL)) {

            String pattern = "%" + searchTerm + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contacts.add(mapResultSetToContact(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException("Erreur lors de la recherche", e);
        }
        return contacts;
    }

    /**
     * Ajout d'un contact avec gestion des clés générées
     */
    public boolean addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact ne peut pas être null");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            setContactParameters(stmt, contact);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        contact.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            handleSQLException("Erreur lors de l'ajout", e);
        }
        return false;
    }

    /**
     * Mise à jour complète d'un contact
     */
    public boolean updateContact(Contact contact) {
        if (contact == null || contact.getId() <= 0) {
            throw new IllegalArgumentException("Contact invalide");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            setContactParameters(stmt, contact);
            stmt.setInt(8, contact.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException("Erreur lors de la mise à jour", e);
            return false;
        }
    }

    /**
     * Récupération par ID avec Optional
     */
    public Optional<Contact> findById(int id) {
        if (id <= 0) {
            return Optional.empty();
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToContact(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException("Erreur lors de la recherche par ID", e);
        }
        return Optional.empty();
    }

    /**
     * Récupération de tous les contacts
     */
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                contacts.add(mapResultSetToContact(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Erreur lors de la récupération", e);
        }
        return contacts;
    }

    /**
     * Suppression d'un contact
     */
    public boolean deleteContact(int id) {
        if (id <= 0) {
            return false;
        }
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException("Erreur lors de la suppression", e);
            return false;
        }
    }


    // ===== MÉTHODES UTILITAIRES PRIVÉES =====
    /**
     * Mapping ResultSet → Contact
     */
    private Contact mapResultSetToContact(ResultSet rs) throws SQLException {
        Contact contact = new Contact();
        contact.setId(rs.getInt("id"));
        contact.setNom(rs.getString("nom"));
        contact.setPostnom(rs.getString("postnom"));
        contact.setEmail(rs.getString("email"));
        contact.setNumeroTelephone(rs.getString("numero_telephone"));
        contact.setGenre(rs.getString("genre"));
        contact.setAdresse(rs.getString("adresse"));
        contact.setPhotoContact(rs.getInt("photo_contact"));
        return contact;
    }

    /**
     * Paramétrage commun pour insert/update
     */
    private void setContactParameters(PreparedStatement stmt, Contact contact) throws SQLException {
        stmt.setString(1, contact.getNom());
        stmt.setString(2, contact.getPostnom());
        stmt.setString(3, contact.getEmail());
        stmt.setString(4, contact.getNumeroTelephone());
        stmt.setString(5, contact.getGenre());
        stmt.setString(6, contact.getAdresse());
        stmt.setInt(7, contact.getPhotoContact());
    }

    /**
     * Gestion centralisée des erreurs SQL
     */
    private void handleSQLException(String context, SQLException e) {
        System.err.println(context + ": " + e.getMessage());
        e.printStackTrace();
        // Ici vous pourriez ajouter une journalisation plus sophistiquée
    }
}