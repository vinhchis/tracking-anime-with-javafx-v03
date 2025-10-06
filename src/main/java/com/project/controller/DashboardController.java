package com.project.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.project.navigation.Refreshable;
import com.project.navigation.View;
import com.project.util.AlertUtil;
import com.project.util.AssetUtil;
import com.project.util.SaveRegistry;
import com.project.viewmodel.DashboardViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

        navButtons.forEach(button -> {
            button.getStyleClass().add("tab-button");
        });

        navButtons.forEach(button -> button.setOnAction((event) -> {
            handleNavButtonClick(event);
        }));

        notiToggleButton.setOnAction(this::handleNotiToggle);

        // Load default view
        Parent root = AssetUtil.loadFXML(View.OVERVIEW.getFxmlFile());
        // set active class correctly (only the 'active' class, tab-button already
        // present)
        overViewButton.getStyleClass().add("active");
        mainBorderPane.setCenter(root);

    }

    @FXML
    public void handleNavButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        Parent root = null;

        // add only the 'active' class
        clickedButton.getStyleClass().add("active");
        // remove 'active' from others
        navButtons.stream().filter(btn -> btn != clickedButton).forEach(btn -> {
            btn.getStyleClass().remove("active");
        });
        AlertUtil.showAlert(AlertType.INFORMATION, mainBorderPane.getScene().getWindow(),
                "Navigate to " + clickedButton.getText(), "You just clicked " + clickedButton.getText() + " button.");

        switch (clickedButton.getId()) {
            case "overViewButton":
                SaveRegistry.saveAll();
                root = AssetUtil.loadFXML(View.OVERVIEW.getFxmlFile());
                break;
            case "discoverButton":
                SaveRegistry.saveAll();
                root = AssetUtil.loadFXML(View.DISCOVER.getFxmlFile());
                break;
            case "myListButton":
                root = AssetUtil.loadFXML(View.MY_LIST.getFxmlFile());
                break;
            default:
                break;
        }

        mainBorderPane.setCenter(root);

    }

    @FXML
    public void handleNotiToggle(ActionEvent event) {
        boolean isSelected = notiToggleButton.isSelected();
        // todo
    }

    private void openView(String fxmlPath) {
        try {
            // save changes first so Overview reads newest data
            SaveRegistry.saveAll();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof Refreshable) {
                ((Refreshable) controller).onFresh();
            }

            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
