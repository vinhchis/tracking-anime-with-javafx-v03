package com.project.navigation;

public enum UserTabView {
    USER_DISCOVER("/user/DiscoverView.fxml"),
    USER_MYLIST("/user/MyListView.fxml"),
    USER_OVERVIEW("/user/OverviewView.fxml");


    private final String fxmlFile;

    UserTabView(String fxmlFile) {
        this.fxmlFile = "/com/project" + fxmlFile;
    }

    public String getFxmlFile() {
        return fxmlFile;
    }
}
