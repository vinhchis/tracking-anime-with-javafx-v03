package com.project.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;

import com.browniebytes.javafx.control.DateTimePicker;
import com.browniebytes.javafx.control.HoursPicker;
import com.jfoenix.controls.JFXTimePicker;
import com.project.shared.TimePicker;
import com.project.shared.TrackingCard;
import com.project.util.AssetUtil;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class MyListController implements Initializable {
    @FXML
    private BorderPane myListBorderPane;

    @FXML
    private ComboBox<String> filterStatusComboBox;

    @FXML
    private Label totalAnimeLabel;

    @FXML
    private FlowPane trackingFlowPane;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        filterStatusComboBox.getItems().addAll("All", "Watching", "Completed", "On Hold", "Dropped", "Plan to Watch");
        filterStatusComboBox.setValue("All");

        filterStatusComboBox.setOnAction(event -> {
            String selectedStatus = filterStatusComboBox.getValue();
            // Implement filtering logic based on selectedStatus
            System.out.println("Selected Status: " + selectedStatus);
            totalAnimeLabel.setText("Total Anime: " + (int)(Math.random() * 100)); // Placeholder for total count
        });

        // FLOW PANE SETUP

        for (int i = 1; i <= 5; i++) {
            // TrackingCard card = new TrackingCard();
            // card.applyModel("Anime" + i, "Haha", "Watching", AssetUtils.getImageFromProject("a1.png"), i, i);
            DateTimePicker dtp = new DateTimePicker();
            trackingFlowPane.getChildren().add(dtp);
        }
    }

}
