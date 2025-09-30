package com.project.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;

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

    private OverviewViewModel viewModel;

    public OverviewController() {
        this.viewModel = new OverviewViewModel();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        loadData();
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == sundayTab) {
              viewModel.getSelectedDay().set(DAY_OF_WEEK.SUNDAY);
                loadCardsToTab(sundayTab);

            } else if (newTab == mondayTab) {
                viewModel.getSelectedDay().set(DAY_OF_WEEK.MONDAY);
                loadCardsToTab(mondayTab);
            } else if (newTab == tuesdayTab) {
                viewModel.getSelectedDay().set(DAY_OF_WEEK.TUESDAY);
                loadCardsToTab(tuesdayTab);
            } else if (newTab == wednesdayTab) {
                viewModel.getSelectedDay().set(DAY_OF_WEEK.WEDNESDAY);
                loadCardsToTab(wednesdayTab);
            } else if (newTab == thursdayTab) {
                viewModel.getSelectedDay().set(DAY_OF_WEEK.THURSDAY);
                loadCardsToTab(thursdayTab);
            } else if (newTab == fridayTab) {
                viewModel.getSelectedDay().set(DAY_OF_WEEK.FRIDAY);
                loadCardsToTab(fridayTab);
            } else if (newTab == saturdayTab) {
                viewModel.getSelectedDay().set(DAY_OF_WEEK.SATURDAY);
                loadCardsToTab(saturdayTab);
            }
        });
    }


    private void loadCardsToTab(Tab tab) {
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        flowPane.setPadding(new Insets(10));
        for (TrackingScheduleCardDto dto : viewModel.getDayList()) {
            TrackingScheduleCard card = new TrackingScheduleCard(dto);
            flowPane.getChildren().add(card);
        }
        tab.setContent(flowPane);
    }

    private void loadData() {
        long watchingCount = viewModel.getAnimeCountByStatus(TRACKINGS_STATUS.WATCHING);
        long completedCount = viewModel.getAnimeCountByStatus(TRACKINGS_STATUS.COMPLETED);
        long onHoldCount = viewModel.getAnimeCountByStatus(TRACKINGS_STATUS.ON_HOLD);
        long droppedCount = viewModel.getAnimeCountByStatus(TRACKINGS_STATUS.DROPPED);
        long planToWatchCount = viewModel.getAnimeCountByStatus(TRACKINGS_STATUS.PLAN_TO_WATCH);
        long totalCount = viewModel.getTotalAnimeCount();

        watchingCountLabel.setText(String.valueOf(watchingCount));
        completedCountLabel.setText(String.valueOf(completedCount));
        onHoldCountLabel.setText(String.valueOf(onHoldCount));
        droppedCountLabel.setText(String.valueOf(droppedCount));
        planToWatchCountLabel.setText(String.valueOf(planToWatchCount));
        totalCountLabel.setText(String.valueOf(totalCount));

        pieChart.getData().clear();
        pieChart.getData().add(new PieChart.Data("Watching", watchingCount));
        pieChart.getData().add(new PieChart.Data("Completed", completedCount));
        pieChart.getData().add(new PieChart.Data("On Hold", onHoldCount));
        pieChart.getData().add(new PieChart.Data("Dropped", droppedCount));
        pieChart.getData().add(new PieChart.Data("Plan to Watch", planToWatchCount));
    }

}
