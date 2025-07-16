package utils;

import service.User;

public class SessionManager {
    private static User currentUser;
    private static final ThreadLocal<User> threadLocalUser = new ThreadLocal<>();

    // Pour applications desktop/Swing
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    // Pour applications web (thread-safe)
    public static void setThreadUser(User user) {
        threadLocalUser.set(user);
    }

    public static User getThreadUser() {
        return threadLocalUser.get();
    }

    public static void clearSession() {
        currentUser = null;
        threadLocalUser.remove();
    }
}