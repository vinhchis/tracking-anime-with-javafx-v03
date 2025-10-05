package com.project.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.project.navigation.View;
import com.project.service.ExportService;
import com.project.service.TrackingService;
import com.project.util.AlertUtil;
import com.project.util.AssetUtil;
import com.project.util.SaveRegistry;
import com.project.viewmodel.DashboardViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


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
        overViewButton.getStyleClass().add("active");
        mainBorderPane.setCenter(root);
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

        clickedButton.getStyleClass().add("active");
        navButtons.stream().filter(btn -> btn != clickedButton).forEach(btn -> {
            btn.getStyleClass().remove("active");
        });
        mainBorderPane.setCenter(root);
        AlertUtil.showAlert(AlertType.INFORMATION, mainBorderPane.getScene().getWindow(),
                "Navigate to " + clickedButton.getText(), "You just clicked " + clickedButton.getText() + " button.");
    }

    @FXML
    public void handleNotiToggle(ActionEvent event) {
        boolean isSelected = notiToggleButton.isSelected();
        // todo: Thêm logic cho việc hiển thị/ẩn panel thông báo (sử dụng NotificationService của Lộc)
    }

    // Phương thức này được gọi khi người dùng nhấn nút Export
    @FXML
    public void handleExportScheduleEvent(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu Lịch Chiếu iCalendar");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("iCalendar files (*.ics)", "*.ics");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setInitialFileName("anime_schedule.ics");

        // Lấy Stage hiện tại từ mainBorderPane
        Stage stage = (Stage) mainBorderPane.getScene().getWindow();

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            Path outputPath = file.toPath();
            try {
                // GỌI SERVICE XUẤT LỊCH CỦA LỘC
                exportService.exportScheduleToIcs(outputPath);

                AlertUtil.showAlert(AlertType.INFORMATION, stage, "Thành công",
                        "Đã xuất lịch chiếu thành công ra: " + outputPath.toAbsolutePath());

            } catch (IOException e) {
                AlertUtil.showAlert(AlertType.ERROR, stage, "Lỗi Xuất File",
                        "Không thể lưu file lịch chiếu: " + e.getMessage());
            }
        }
    }
}