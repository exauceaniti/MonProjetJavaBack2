package models;

/**
 * Représente un contact enregistré dans la base de données.
 * Chaque contact est associé à un compte utilisateur via 'compte_id'.
 */
public class Contact {
    private int id;
    private String nom;
    private String postnom;
    private String email;
    private String numeroTelephone;
    private String genre;
    private String adresse;
    private int compteId; // ID du compte lié à ce contact

    /**
     * Constructeur vide – utile pour instancier sans données initiales.
     */
    public Contact() {}

    /**
     * Constructeur complet – initialise tous les champs du contact.
     *
     * @param id Identifiant unique du contact
     * @param nom Nom de famille
     * @param postnom Postnom
     * @param email Adresse email
     * @param numeroTelephone Numéro de téléphone
     * @param genre Genre (M/F/Autre)
     * @param adresse Adresse physique
     * @param compteId ID du compte propriétaire
     */
    public Contact(int id, String nom, String postnom, String email,
                   String numeroTelephone, String genre, String adresse, int compteId) {
        this.id = id;
        this.nom = nom;
        this.postnom = postnom;
        this.email = email;
        this.numeroTelephone = numeroTelephone;
        this.genre = genre;
        this.adresse = adresse;
        this.compteId = compteId;
    }

    // ================= GETTERS =================

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPostnom() { return postnom; }
    public String getEmail() { return email; }
    public String getNumeroTelephone() { return numeroTelephone; }
    public String getGenre() { return genre; }
    public String getAdresse() { return adresse; }
    public int getCompteId() { return compteId; }

    // ================= SETTERS =================

    public void setId(int id) {
        if (id < 0) throw new IllegalArgumentException("L'ID ne peut pas être négatif");
        this.id = id;
    }

    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) throw new IllegalArgumentException("Le nom est obligatoire");
        this.nom = nom.trim();
    }

    public void setPostnom(String postnom) {
        if (postnom == null || postnom.trim().isEmpty()) throw new IllegalArgumentException("Le postnom est obligatoire");
        this.postnom = postnom.trim();
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("L'email est obligatoire");
        this.email = email.trim().toLowerCase();
    }

    public void setNumeroTelephone(String numeroTelephone) {
        if (numeroTelephone == null || numeroTelephone.trim().isEmpty()) throw new IllegalArgumentException("Le numéro est obligatoire");
        this.numeroTelephone = numeroTelephone.trim();
    }

    public void setGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) throw new IllegalArgumentException("Le genre est obligatoire");
        this.genre = genre.trim();
    }

    public void setAdresse(String adresse) {
        this.adresse = (adresse != null) ? adresse.trim() : null;
    }

    public void setCompteId(int compteId) {
        if (compteId < 0) throw new IllegalArgumentException("compte_id invalide");
        this.compteId = compteId;
    }

    // ================= METHODES =================
    /**
     * Vérifie que le contact contient les infos de base valides.
     */
    public boolean isValid() {
        return nom != null && !nom.isEmpty() &&
                postnom != null && !postnom.isEmpty() &&
                email != null && !email.isEmpty() &&
                numeroTelephone != null && !numeroTelephone.isEmpty() &&
                genre != null && !genre.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Contact #%d - %s %s (%s)", id, nom, postnom, numeroTelephone);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Contact)) return false;
        return id == ((Contact) obj).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
