package com.project.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.project.navigation.Refreshable;
import com.project.navigation.View;
import com.project.service.ExportService;
import com.project.service.TrackingService;
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

    // ĐÃ SỬA: Khai báo để khớp với fx:id="exportScheduleButton" trong FXML
    @FXML
    private Button exportScheduleButton;

    private DashboardViewModel viewModel;

    // Khai báo Services
    private ExportService exportService;
    private TrackingService trackingService;

    private List<Button> navButtons;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        viewModel = new DashboardViewModel();

        // KHỞI TẠO SERVICES CỦA LỘC
        this.trackingService = new TrackingService();
        this.exportService = new ExportService(this.trackingService);

        // SỬA LỖI: Thay thế List.of() bằng Arrays.asList()
        // CẦN THÊM 'exportScheduleButton' vào danh sách nếu muốn nó có style 'tab-button'
        navButtons = Arrays.asList(overViewButton, discoverButton, myListButton);

        // Bạn nên kiểm tra nếu bạn muốn nút Export cũng là một phần của navButtons
        // Nếu không, chỉ cần đảm bảo logic cho nó hoạt động.

        navButtons.forEach(button -> {
            button.getStyleClass().add("tab-button");
        });

        navButtons.forEach(button -> button.setOnAction((event) -> {
            handleNavButtonClick(event);
        }));

        notiToggleButton.setOnAction(this::handleNotiToggle);

        // ĐÃ SỬA: Sử dụng exportScheduleButton
        if (exportScheduleButton != null) {
            exportScheduleButton.setOnAction(this::handleExportScheduleEvent);
        }

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

        // add only the 'active' class
        clickedButton.getStyleClass().add("active");
        navButtons.stream().filter(btn -> btn != clickedButton).forEach(btn -> {
            btn.getStyleClass().remove("active");
        });
        AlertUtil.showAlert(AlertType.INFORMATION, mainBorderPane.getScene().getWindow(),
                "Navigate to " + clickedButton.getText(), "You just clicked " + clickedButton.getText() + " button.");

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
