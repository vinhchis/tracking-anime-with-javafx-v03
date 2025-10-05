package com.project.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.project.dto.TrackingScheduleCardDto;
import com.project.entity.Tracking.DAY_OF_WEEK;
import com.project.entity.Tracking.TRACKINGS_STATUS;
import com.project.shared.TrackingScheduleCard;
import com.project.viewmodel.OverviewViewModel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable; // Đã thêm Import
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

    // Đã Khai báo các Label theo FXML
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
        this.viewModel = new OverviewViewModel();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // 1. GỌI TẢI DỮ LIỆU BAN ĐẦU
        viewModel.loadScheduleData();
        viewModel.loadStatisticsData();

        // 2. BINDING DỮ LIỆU THỐNG KÊ (Đã hoàn thiện)
        bindStatisticsData();

        // 3. THIẾT LẬP CÁC FLOWPANE
        setupFlowPanes();

        // 4. BINDING LỊCH CHIẾU VÀ LẮNG NGHE THAY ĐỔI TAB
        setupScheduleBinding();

        // Thiết lập tab mặc định và lắng nghe thay đổi tab
        setupTabSelectionListener();
    }

    // Phương thức Binding dữ liệu thống kê vào Label và PieChart
    private void bindStatisticsData() {
        // Binding Label
        watchingCountLabel.textProperty().bind(viewModel.watchingCountProperty().asString());
        completedCountLabel.textProperty().bind(viewModel.completedCountProperty().asString());
        totalCountLabel.textProperty().bind(viewModel.totalTrackingCountProperty().asString());

        // BỔ SUNG: Hoàn thiện binding cho 3 trạng thái còn lại
        // LƯU Ý: Các property này phải được thêm vào OverviewViewModel
        onHoldCountLabel.textProperty().bind(viewModel.onHoldCountProperty().asString());
        droppedCountLabel.textProperty().bind(viewModel.droppedCountProperty().asString());
        planToWatchCountLabel.textProperty().bind(viewModel.planToWatchCountProperty().asString());

        // Binding PieChart (Cần PieChart.Data động, phức tạp hơn, ta sẽ làm tĩnh theo phong cách cũ)
        updatePieChart();

        // Listener cho các property để cập nhật PieChart nếu dữ liệu thay đổi
        viewModel.watchingCountProperty().addListener((obs, oldVal, newVal) -> updatePieChart());
        viewModel.completedCountProperty().addListener((obs, oldVal, newVal) -> updatePieChart());
        viewModel.onHoldCountProperty().addListener((obs, oldVal, newVal) -> updatePieChart()); // Bổ sung
        viewModel.droppedCountProperty().addListener((obs, oldVal, newVal) -> updatePieChart());   // Bổ sung
        viewModel.planToWatchCountProperty().addListener((obs, oldVal, newVal) -> updatePieChart()); // Bổ sung
    }

    // Cập nhật PieChart data
    private void updatePieChart() {
        pieChart.getData().clear();
        pieChart.getData().add(new Data("Watching", viewModel.watchingCountProperty().get()));
        pieChart.getData().add(new Data("Completed", viewModel.completedCountProperty().get()));

        // ĐÃ SỬA: Lấy dữ liệu từ ViewModel Property (tốt hơn) thay vì gọi Service trực tiếp
        pieChart.getData().add(new Data("On Hold", viewModel.onHoldCountProperty().get()));
        pieChart.getData().add(new Data("Dropped", viewModel.droppedCountProperty().get()));
        pieChart.getData().add(new Data("Plan to Watch", viewModel.planToWatchCountProperty().get()));
    }

    // Phương thức thiết lập Binding cho lịch chiếu (Không thay đổi)
    private void setupScheduleBinding() {
        FlowPane[] flowPanes = new FlowPane[]{sundayFlowPane, mondayFlowPane, tuesdayFlowPane,
                wednesdayFlowPane, thursdayFlowPane, fridayFlowPane,
                saturdayFlowPane};

        viewModel.getScheduleList().addListener((ListChangeListener<TrackingScheduleCardDto>) c -> {
            c.next();
            DAY_OF_WEEK currentDay = viewModel.getSelectedDay().get();
            if (currentDay == null) return;

            FlowPane targetFlowPane = getFlowPaneForDay(currentDay);

            if (targetFlowPane != null) {
                targetFlowPane.getChildren().clear();

                for (TrackingScheduleCardDto dto : viewModel.getScheduleList()) {
                    TrackingScheduleCard card = new TrackingScheduleCard(dto);
                    targetFlowPane.getChildren().add(card);
                }
            }
        });
    }

    // Khởi tạo các FlowPane (Không thay đổi)
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

    // Phương thức helper để lấy FlowPane dựa trên DAY_OF_WEEK (Không thay đổi)
    private FlowPane getFlowPaneForDay(DAY_OF_WEEK day) {
        FlowPane targetFlowPane = null;
        switch (day) {
            case SUNDAY: targetFlowPane = sundayFlowPane; break;
            case MONDAY: targetFlowPane = mondayFlowPane; break;
            case TUESDAY: targetFlowPane = tuesdayFlowPane; break;
            case WEDNESDAY: targetFlowPane = wednesdayFlowPane; break;
            case THURSDAY: targetFlowPane = thursdayFlowPane; break;
            case FRIDAY: targetFlowPane = fridayFlowPane; break;
            case SATURDAY: targetFlowPane = saturdayFlowPane; break;
            default: targetFlowPane = null; break;
        }
        return targetFlowPane;
    }

    // Phương thức xử lý thay đổi Tab (Không thay đổi)
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
        });

        // Đặt mặc định(---)
        tabPane.getSelectionModel().select(sundayTab);
    }
}