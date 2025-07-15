package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Représente un utilisateur du système avec toutes ses propriétés.
 * Immuable et construit via le pattern Builder.
 */
public final class User {
    // Champs obligatoires
    private final int id;
    private final String phoneNumber;
    private final String role;

    // Champs optionnels
    private final String lastName;
    private final String firstName;
    private final String email;
    private final String gender;
    private final String address;
    private final LocalDate birthDate;
    private final String profilePicture;
    private final LocalDateTime creationDate;
    private final LocalDateTime lastLogin;

    // Expressions régulières pour validation
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{8,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_ADDRESS_LENGTH = 255;

    /**
     * Constructeur privé utilisé par le Builder.
     */
    private User(Builder builder) {
        this.id = builder.id;
        this.phoneNumber = validatePhone(builder.phoneNumber);
        this.role = validateRole(builder.role);
        this.lastName = validateName(builder.lastName, "Nom");
        this.firstName = validateName(builder.firstName, "Prénom");
        this.email = validateEmail(builder.email);
        this.gender = validateGender(builder.gender);
        this.address = validateAddress(builder.address);
        this.birthDate = builder.birthDate;
        this.profilePicture = builder.profilePicture;
        this.creationDate = builder.creationDate != null ? builder.creationDate : LocalDateTime.now();
        this.lastLogin = builder.lastLogin;
    }

    // ==================== VALIDATION METHODS ====================

    private static String validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Le numéro de téléphone est obligatoire");
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Format de numéro invalide");
        }
        return phone.trim();
    }

    private static String validateRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Le rôle est obligatoire");
        }

        String normalizedRole = role.trim().toLowerCase();
        if (!"admin".equals(normalizedRole) && !"user".equals(normalizedRole)) {
            throw new IllegalArgumentException("Rôle invalide. Doit être 'admin' ou 'user'");
        }
        return normalizedRole;
    }

    private static String validateName(String name, String fieldName) {
        if (name == null) return null;
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(fieldName + " ne peut dépasser " + MAX_NAME_LENGTH + " caractères");
        }
        return name.trim();
    }

    private static String validateEmail(String email) {
        if (email == null) return null;
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
        return email.trim().toLowerCase();
    }

    private static String validateGender(String gender) {
        if (gender == null) return null;
        String normalizedGender = gender.trim().toUpperCase();
        if (!"M".equals(normalizedGender) && !"F".equals(normalizedGender) && !"AUTRE".equals(normalizedGender)) {
            throw new IllegalArgumentException("Genre invalide. Utilisez M, F ou Autre");
        }
        return normalizedGender;
    }

    private static String validateAddress(String address) {
        if (address == null) return null;
        if (address.length() > MAX_ADDRESS_LENGTH) {
            throw new IllegalArgumentException("L'adresse ne peut dépasser " + MAX_ADDRESS_LENGTH + " caractères");
        }
        return address.trim();
    }

    // ==================== GETTERS ====================

    public int getId() { return id; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRole() { return role; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getProfilePicture() { return profilePicture; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public LocalDateTime getLastLogin() { return lastLogin; }

    // ==================== UTILITY METHODS ====================

    public boolean isAdmin() {
        return "admin".equals(role);
    }

    public boolean isUser() {
        return "user".equals(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    // ==================== BUILDER CLASS ====================

    public static class Builder {
        // Champs obligatoires
        private final int id;
        private final String phoneNumber;
        private final String role;

        // Champs optionnels
        private String lastName;
        private String firstName;
        private String email;
        private String gender;
        private String address;
        private LocalDate birthDate;
        private String profilePicture;
        private LocalDateTime creationDate;
        private LocalDateTime lastLogin;

        public Builder(int id, String phoneNumber, String role) {
            this.id = id;
            this.phoneNumber = phoneNumber;
            this.role = role;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder profilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
            return this;
        }

        public Builder creationDate(LocalDateTime creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder lastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}