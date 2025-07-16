package service;

import dao.ContactDAO;
import models.Contact;
import java.sql.SQLException;
import java.util.List;

public class ContactService {
    private final ContactDAO contactDAO;

    public ContactService() {
        this.contactDAO = new ContactDAO();
    }

    /**
     * Récupère tous les contacts depuis la base de données
     * @return Liste des contacts
     * @throws SQLException en cas d'erreur SQL
     */
    public List<Contact> getAllContacts() throws SQLException {
        return contactDAO.getAllContacts();
    }

    /**
     * Ajoute un nouveau contact
     * @param contact le contact à ajouter
     * @return true si l'ajout a réussi
     * @throws SQLException en cas d'erreur SQL
     */
    public boolean addContact(Contact contact) throws SQLException {
        return contactDAO.addContact(contact);
    }

    /**
     * Met à jour un contact existant
     * @param contact le contact avec les modifications
     * @return true si la mise à jour a réussi
     * @throws SQLException en cas d'erreur SQL
     */
    public boolean updateContact(Contact contact) throws SQLException {
        return contactDAO.updateContact(contact);
    }

    /**
     * Supprime un contact
     * @param id l'ID du contact à supprimer
     * @return true si la suppression a réussi
     * @throws SQLException en cas d'erreur SQL
     */
    public boolean deleteContact(int id) throws SQLException {
        return contactDAO.deleteContact(id);
    }

    /**
     * Trouve un contact par son ID
     * @param id l'ID du contact
     * @return le Contact trouvé ou null
     * @throws SQLException en cas d'erreur SQL
     */
    public Contact findById(int id) throws SQLException {
        return contactDAO.findById(id).orElse(null);
    }

}
