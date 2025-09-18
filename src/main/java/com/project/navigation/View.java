package com.project.navigation;

public enum View {
    DASHBOARD("/DashboardView.fxml"),
    OVERVIEW("/OverviewView.fxml"),
    DISCOVER("/DiscoverView.fxml"),
    MY_LIST("/MyListView.fxml");

    private final String fxmlFile;

    View(String fxmlFile) {
        this.fxmlFile = fxmlFile;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }
}
