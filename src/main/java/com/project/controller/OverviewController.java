package com.project.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.project.dto.TrackingScheduleCardDto;
import com.project.entity.Tracking.DAY_OF_WEEK;
import com.project.entity.Tracking.TRACKINGS_STATUS;
import com.project.shared.TrackingScheduleCard;
import com.project.viewmodel.OverviewViewModel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.collections.ListChangeListener;
import javafx.scene.chart.PieChart.Data;

public class OverviewController implements Initializable {
    @FXML
    private BorderPane OverviewPane;

    @FXML
    private PieChart pieChart;

    @FXML
    private Label watchingCountLabel, completedCountLabel, onHoldCountLabel, droppedCountLabel, planToWatchCountLabel,
            totalCountLabel;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab sundayTab, mondayTab, tuesdayTab, wednesdayTab, thursdayTab, fridayTab, saturdayTab;

    @FXML
    private FlowPane sundayFlowPane, mondayFlowPane, tuesdayFlowPane, wednesdayFlowPane, thursdayFlowPane,
            fridayFlowPane,
            saturdayFlowPane;

    private OverviewViewModel viewModel;

    public OverviewController() {
        // ViewModel đã tự khởi tạo service bên trong (Bước 4)
        this.viewModel = new OverviewViewModel();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        // 1. GỌI TẢI DỮ LIỆU BAN ĐẦU (chỉ cần gọi 1 lần)
        // Lưu ý: ViewModel đã gọi loadData trong constructor, nhưng gọi lại để đảm bảo
        viewModel.loadScheduleData();
        viewModel.loadStatisticsData();

        // 2. BINDING DỮ LIỆU THỐNG KÊ (Binding giúp tự cập nhật)
        bindStatisticsData();

        // 3. THIẾT LẬP CÁC FLOWPANE (Sử dụng Map để đơn giản hóa)
        setupFlowPanes();

        // 4. BINDING LỊCH CHIẾU VÀ LẮNG NGHE THAY ĐỔI TAB
        // Thiết lập lắng nghe cho FilteredList trong ViewModel
        // Vì FilteredList chỉ chứa các DTO đã được lọc cho ngày đó, ta chỉ cần cập nhật FlowPane tương ứng.
        setupScheduleBinding();

        // Thiết lập tab mặc định và lắng nghe thay đổi tab
        // Khi tab thay đổi, nó sẽ cập nhật selectedDay trong ViewModel,
        // và Listener ở setupScheduleBinding sẽ tự động cập nhật UI.
        setupTabSelectionListener();
    }

    // Phương thức Binding dữ liệu thống kê vào Label và PieChart
    private void bindStatisticsData() {
        // Binding Label
        watchingCountLabel.textProperty().bind(viewModel.watchingCountProperty().asString());
        completedCountLabel.textProperty().bind(viewModel.completedCountProperty().asString());
        totalCountLabel.textProperty().bind(viewModel.totalTrackingCountProperty().asString());
        // Lộc cần thêm binding cho onHold, dropped, planToWatch tương tự nếu ViewModel có property cho chúng.

        // Binding PieChart (Cần PieChart.Data động, phức tạp hơn, ta sẽ làm tĩnh theo phong cách cũ)
        // Cập nhật PieChart data
        updatePieChart();

        // Listener cho các property để cập nhật PieChart nếu dữ liệu thay đổi
        viewModel.watchingCountProperty().addListener((obs, oldVal, newVal) -> updatePieChart());
        viewModel.completedCountProperty().addListener((obs, oldVal, newVal) -> updatePieChart());
        // ... thêm các Listener khác nếu cần ...
    }

    // Cập nhật PieChart data
    private void updatePieChart() {
        pieChart.getData().clear();
        pieChart.getData().add(new Data("Watching", viewModel.watchingCountProperty().get()));
        pieChart.getData().add(new Data("Completed", viewModel.completedCountProperty().get()));

        // Tạm thời hardcode, Lộc cần thêm logic lấy số liệu cho các trạng thái khác
        // Sửa lỗi: Thay thế viewModel.statisticsService bằng viewModel.getStatisticsService()
        pieChart.getData().add(new Data("On Hold", viewModel.getStatisticsService().getTrackingCountByStatus(TRACKINGS_STATUS.ON_HOLD)));
        pieChart.getData().add(new Data("Dropped", viewModel.getStatisticsService().getTrackingCountByStatus(TRACKINGS_STATUS.DROPPED)));
        pieChart.getData().add(new Data("Plan to Watch", viewModel.getStatisticsService().getTrackingCountByStatus(TRACKINGS_STATUS.PLAN_TO_WATCH)));
    }

    // Phương thức thiết lập Binding cho lịch chiếu
    private void setupScheduleBinding() {
        // Map Enum DAY_OF_WEEK tới FlowPane tương ứng
        FlowPane[] flowPanes = new FlowPane[]{sundayFlowPane, mondayFlowPane, tuesdayFlowPane,
                wednesdayFlowPane, thursdayFlowPane, fridayFlowPane,
                saturdayFlowPane};

        // Lắng nghe sự thay đổi trong FilteredList của ViewModel.
        // Khi ViewModel lọc lại danh sách cho ngày mới, Listener này kích hoạt
        viewModel.getScheduleList().addListener((ListChangeListener<TrackingScheduleCardDto>) c -> {
            c.next();

            // Lấy FlowPane đang được chọn (dựa vào selectedDay)
            DAY_OF_WEEK currentDay = viewModel.getSelectedDay().get();
            if (currentDay == null) return;

            FlowPane targetFlowPane = getFlowPaneForDay(currentDay);

            if (targetFlowPane != null) {
                // Xóa và load lại chỉ FlowPane đang được hiển thị
                targetFlowPane.getChildren().clear();

                for (TrackingScheduleCardDto dto : viewModel.getScheduleList()) {
                    TrackingScheduleCard card = new TrackingScheduleCard(dto);
                    targetFlowPane.getChildren().add(card);
                }
            }
        });
    }

    // Khởi tạo các FlowPane (chỉ cần thiết lập padding và gap 1 lần)
    private void setupFlowPanes() {
        FlowPane[] allFlowPanes = new FlowPane[]{sundayFlowPane, mondayFlowPane, tuesdayFlowPane, wednesdayFlowPane, thursdayFlowPane, fridayFlowPane, saturdayFlowPane};
        for (FlowPane fp : allFlowPanes) {
            if (fp != null) {
                fp.setPadding(new Insets(10));
                fp.setHgap(10);
                fp.setVgap(10);
            }
        }
    }


    // Phương thức helper để lấy FlowPane dựa trên DAY_OF_WEEK (Đã chuyển sang Switch Statement)
    private FlowPane getFlowPaneForDay(DAY_OF_WEEK day) {
        FlowPane targetFlowPane = null;
        switch (day) {
            case SUNDAY:
                targetFlowPane = sundayFlowPane;
                break;
            case MONDAY:
                targetFlowPane = mondayFlowPane;
                break;
            case TUESDAY:
                targetFlowPane = tuesdayFlowPane;
                break;
            case WEDNESDAY:
                targetFlowPane = wednesdayFlowPane;
                break;
            case THURSDAY:
                targetFlowPane = thursdayFlowPane;
                break;
            case FRIDAY:
                targetFlowPane = fridayFlowPane;
                break;
            case SATURDAY:
                targetFlowPane = saturdayFlowPane;
                break;
            default:
                targetFlowPane = null; // Mặc dù Enum không có default, nhưng để an toàn
                break;
        }
        return targetFlowPane;
    }

    // Phương thức xử lý thay đổi Tab (chỉ cần gọi set SelectedDay trong ViewModel)
    private void setupTabSelectionListener() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == sundayTab) {
                viewModel.getSelectedDay().set(DAY_OF_WEEK.SUNDAY);
            } else if (newTab == mondayTab) {
                viewModel.getSelectedDay().set(DAY_OF_WEEK.MONDAY);
            } else if (newTab == tuesdayTab) {
                viewModel.getSelectedDay().set(DAY_OF_WEEK.TUESDAY);
            } else if (newTab == wednesdayTab) {
                viewModel.getSelectedDay().set(DAY_OF_WEEK.WEDNESDAY);
            } else if (newTab == thursdayTab) {
                viewModel.getSelectedDay().set(DAY_OF_WEEK.THURSDAY);
            } else if (newTab == fridayTab) {
                viewModel.getSelectedDay().set(DAY_OF_WEEK.FRIDAY);
            } else if (newTab == saturdayTab) {
                viewModel.getSelectedDay().set(DAY_OF_WEEK.SATURDAY);
            }
            // Không cần gọi loadCardsToTab(newTab); vì nó được xử lý bởi setupScheduleBinding
        });

        // Đặt mặc định
        tabPane.getSelectionModel().select(sundayTab); // Chọn tab đầu tiên
    }

    // LOẠI BỎ PHƯƠNG THỨC loadData() và loadCardsToTab() CŨ
    /*
    private void loadCardsToTab(Tab tab) { ... }
    private void loadData() { ... }
    */
}