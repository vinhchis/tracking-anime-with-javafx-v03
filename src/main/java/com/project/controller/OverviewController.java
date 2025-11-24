package com.project.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.project.dto.TrackingScheduleCardDto;
import com.project.entity.Tracking.DAY_OF_WEEK;
import com.project.entity.Tracking.TRACKINGS_STATUS;
import com.project.shared.TrackingScheduleCard;
import com.project.viewmodel.OverviewViewModel;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable; // Đã thêm Import
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        // default load sunday tab
        viewModel.getSelectedDay().set(DAY_OF_WEEK.SUNDAY);
        loadCardsToTab(sundayTab);


        // listen for tab changes
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