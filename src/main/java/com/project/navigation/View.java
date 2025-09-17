package com.project.navigation;

public enum View {
    // Auth Pages


    LOGIN("/auth/LoginView.fxml"),
    REGISTER("/auth/RegisterView.fxml"),

    // Admin Pages
    ADMIN_DASHBOARD("/admin/AdminDashboardView.fxml"),

    // User Module
    USER_DASHBOARD("/user/UserDashboardView.fxml"),
    USER_PROFILE("/user/ProfileView.fxml");


    private final String fxmlFile;

    View(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }
}
