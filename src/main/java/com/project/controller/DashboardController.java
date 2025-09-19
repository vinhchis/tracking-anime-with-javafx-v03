package com.project.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import com.project.navigation.View;
import com.project.service.ApiService;
import com.project.util.AlertUtil;
import com.project.util.AssetUtil;
import com.project.util.SaveRegistry;
import com.project.viewmodel.DashboardViewModel;
import com.project.entity.Anime;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;


public class DashboardController implements Initializable {
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ToggleButton notiToggleButton;

    @FXML
    private Button overViewButton, discoverButton, myListButton;

    private DashboardViewModel viewModel;

    private List<Button> navButtons;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        viewModel = new DashboardViewModel();
        navButtons = List.of(overViewButton, discoverButton, myListButton);

        navButtons.forEach(button -> button.setOnAction((event) -> {
            handleNavButtonClick(event);
        }));

        notiToggleButton.setOnAction(this::handleNotiToggle);

        // Load default view
        Parent root = AssetUtil.loadFXML(View.MY_LIST.getFxmlFile());
        mainBorderPane.setCenter(root);


        // Use ApiService.getAnimeById asynchronously to avoid blocking the FX thread
        // ApiService apiService = new ApiService();
        // fetchAndShowAnime(apiService, 1);
    }

    @FXML
    public void handleNavButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        Parent root = null;

        switch (clickedButton.getId()) {
            case "overViewButton":
                root = AssetUtil.loadFXML(View.OVERVIEW.getFxmlFile());
                SaveRegistry.saveAll();
                break;
            case "discoverButton":
                root = AssetUtil.loadFXML(View.DISCOVER.getFxmlFile());
                SaveRegistry.saveAll();
                break;
            case "myListButton":
                root = AssetUtil.loadFXML(View.MY_LIST.getFxmlFile());
                break;
            default:
                break;
        }
        mainBorderPane.setCenter(root);
        AlertUtil.showAlert(AlertType.INFORMATION, mainBorderPane.getScene().getWindow(),
                "Navigate to " + clickedButton.getText(), "You just clicked " + clickedButton.getText() + " button.");
    }

    @FXML
    public void handleNotiToggle(ActionEvent event) {
        boolean isSelected = notiToggleButton.isSelected();

    }

    // Helper: fetch anime off the FX thread and show result on FX thread
    private void fetchAndShowAnime(ApiService apiService, int id) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return apiService.getAnimeById(id);
            } catch (Exception e) {
                // wrap checked exception to propagate to exceptionally block
                throw new RuntimeException(e);
            }
        }).thenAccept(anime -> {
            Platform.runLater(() -> {
                // Update UI or show alert with anime info
                String title = anime != null ? anime.getTitle() : "No anime";
                String content = anime != null ? anime.toString() : "No details available";
                AlertUtil.showAlert(AlertType.INFORMATION, mainBorderPane.getScene().getWindow(), title, content);
            });
        }).exceptionally(ex -> {
            Platform.runLater(() -> {
                AlertUtil.showAlert(AlertType.ERROR, mainBorderPane.getScene().getWindow(),
                        "Error fetching anime", ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
            });
            return null;
        });
    }

}
