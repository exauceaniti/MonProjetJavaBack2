package dao;

import models.Contact;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe DAO (Data Access Object) pour la gestion des contacts dans la base de données.
 * Fournit toutes les opérations CRUD (Create, Read, Update, Delete) pour la table contacts.
 */
public class ContactDAO {

    // Requêtes SQL préparées
    public static final String INSERT_SQL = "INSERT INTO contacts (nom, postnom, email, numero_telephone, genre, adresse) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String SELECT_ALL_SQL = "SELECT * FROM contacts";
    public static final String SELECT_BY_ID_SQL = "SELECT * FROM contacts WHERE id = ?";
    public static final String DELETE_SQL = "DELETE FROM contacts WHERE id = ?";

    /**
     * Ajoute un nouveau contact dans la base de données.
     *
     * @param contact L'objet Contact à ajouter (ne doit pas être null)
     * @return true si l'ajout a réussi, false sinon
     * @throws IllegalArgumentException si le contact est null
     */
    public boolean addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Le contact ne peut pas être null");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            // Paramétrage de la requête
            stmt.setString(1, contact.getNom());
            stmt.setString(2, contact.getPostnom());
            stmt.setString(3, contact.getEmail());
            stmt.setString(4, contact.getNumeroTelephone());
            stmt.setString(5, contact.getGenre());
            stmt.setString(6, contact.getAdresse());

            // Exécution de la requête
            int affectedRows = stmt.executeUpdate();

            // Récupération de l'ID généré
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        contact.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            handleSQLException("Erreur lors de l'ajout du contact", e);
        }
        return false;
    }

    /**
     * Met à jour un contact existant dans la base de données.
     * Seuls les champs non-nuls de l'objet contact seront mis à jour.
     *
     * @param contact L'objet Contact contenant les modifications (doit avoir un ID valide)
     * @return true si la mise à jour a réussi, false sinon
     * @throws IllegalArgumentException si le contact est null ou n'a pas d'ID
     */
    public boolean updateContact(Contact contact) {
        if (contact == null || contact.getId() <= 0) {
            throw new IllegalArgumentException("Contact invalide ou ID manquant");
        }

        StringBuilder sql = new StringBuilder("UPDATE contacts SET ");
        List<Object> params = new ArrayList<>();

        // Construction dynamique de la requête
        if (contact.getNom() != null) {
            sql.append("nom = ?, ");
            params.add(contact.getNom());
        }
        if (contact.getPostnom() != null) {
            sql.append("postnom = ?, ");
            params.add(contact.getPostnom());
        }
        if (contact.getEmail() != null) {
            sql.append("email = ?, ");
            params.add(contact.getEmail());
        }
        if (contact.getNumeroTelephone() != null) {
            sql.append("numero_telephone = ?, ");
            params.add(contact.getNumeroTelephone());
        }
        if (contact.getGenre() != null) {
            sql.append("genre = ?, ");
            params.add(contact.getGenre());
        }
        if (contact.getAdresse() != null) {
            sql.append("adresse = ?, ");
            params.add(contact.getAdresse());
        }

        // Vérification qu'au moins un champ a été modifié
        if (params.isEmpty()) {
            return false;
        }

        // Finalisation de la requête
        sql.delete(sql.length() - 2, sql.length()); // Supprime la dernière virgule
        sql.append(" WHERE id = ?");
        params.add(contact.getId());

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Paramétrage dynamique
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException("Erreur lors de la mise à jour du contact", e);
            return false;
        }
    }

    /**
     * Récupère un contact par son ID.
     *
     * @param id L'ID du contact à rechercher
     * @return Un Optional contenant le contact trouvé, ou Optional.empty() si non trouvé
     * @throws IllegalArgumentException si l'ID est invalide (<= 0)
     */
    public Optional<Contact> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalide");
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
            handleSQLException("Erreur lors de la recherche du contact par ID", e);
        }
        return Optional.empty();
    }

    /**
     * Récupère tous les contacts de la base de données.
     *
     * @return Une liste des contacts (peut être vide mais jamais null)
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
            handleSQLException("Erreur lors de la récupération des contacts", e);
        }
        return contacts;
    }

    /**
     * Recherche des contacts par nom (recherche partielle insensible à la casse).
     *
     * @param nom Le nom ou partie du nom à rechercher
     * @return Une liste des contacts correspondants (peut être vide mais jamais null)
     * @throws IllegalArgumentException si le nom est null ou vide
     */
    public List<Contact> findByNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        return searchByField("nom", nom);
    }

    /**
     * Recherche des contacts par postnom (recherche partielle insensible à la casse).
     *
     * @param postnom Le postnom ou partie du postnom à rechercher
     * @return Une liste des contacts correspondants (peut être vide mais jamais null)
     * @throws IllegalArgumentException si le postnom est null ou vide
     */
    public List<Contact> findByPostnom(String postnom) {
        if (postnom == null || postnom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le postnom ne peut pas être vide");
        }
        return searchByField("postnom", postnom);
    }

    /**
     * Recherche des contacts par nom ou postnom (recherche partielle insensible à la casse).
     *
     * @param searchTerm Le terme à rechercher dans les champs nom et postnom
     * @return Une liste des contacts correspondants (peut être vide mais jamais null)
     * @throws IllegalArgumentException si le terme de recherche est null ou vide
     */
    public List<Contact> findByNomOrPostnom(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Le terme de recherche ne peut pas être vide");
        }

        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts WHERE nom LIKE ? OR postnom LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contacts.add(mapResultSetToContact(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException("Erreur lors de la recherche par nom/postnom", e);
        }
        return contacts;
    }

    /**
     * Supprime un contact de la base de données.
     *
     * @param id L'ID du contact à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws IllegalArgumentException si l'ID est invalide (<= 0)
     */
    public boolean deleteContact(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalide");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            handleSQLException("Erreur lors de la suppression du contact", e);
            return false;
        }
    }

    // Méthodes privées utilitaires

    /**
     * Méthode générique pour rechercher par champ.
     */
    private List<Contact> searchByField(String fieldName, String value) {
        List<Contact> contacts = new ArrayList<>();
        String sql = String.format("SELECT * FROM contacts WHERE %s LIKE ?", fieldName);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + value + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contacts.add(mapResultSetToContact(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException("Erreur lors de la recherche par " + fieldName, e);
        }
        return contacts;
    }

    /**
     * Convertit un ResultSet en objet Contact.
     *
     * @param rs Résultat SQL contenant les colonnes du contact
     * @return Objet Contact avec les données du résultat
     * @throws SQLException Si une lecture échoue
     */
    private Contact mapResultSetToContact(ResultSet rs) throws SQLException {
        return new Contact(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("postnom"),
                rs.getString("email"),
                rs.getString("numero_telephone"),
                rs.getString("genre"),
                rs.getString("adresse"),
                rs.getInt("compte_id") // ✅ Ajout du champ manquant
        );
    }

    /**
     * Gère les exceptions SQL de manière uniforme.
     */
    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
}