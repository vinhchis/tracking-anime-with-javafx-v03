package com.project.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.project.dto.TrackingDto;
import com.project.shared.TrackingCard;
import com.project.util.AlertUtil;
import com.project.util.SaveRegistry;
import com.project.util.Saveable;
import com.project.viewmodel.MyListViewModel;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class MyListController implements Initializable, Saveable {
    @FXML
    private BorderPane myListBorderPane;

    @FXML
    private ComboBox<String> filterStatusComboBox;

    @FXML
    private Label totalAnimeLabel;

    @FXML
    private FlowPane trackingFlowPane;
    @FXML
    private Tooltip warningTooltip;
    private MyListViewModel viewModel;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        viewModel = new MyListViewModel();
        SaveRegistry.register(this);

        warningTooltip.setText("- Your tracking anime only saved when you move to another tabs or quit app." +
            "\n - When \"My List\" selected again , your tracking was reset on last time you in there.");

        filterStatusComboBox.getItems().addAll("All", "Watching", "Completed", "On Hold", "Dropped", "Plan to Watch");

        // default View
        filterStatusComboBox.setValue("All");
        refreshList();

        // binding
        filterStatusComboBox.valueProperty().bindBidirectional(viewModel.filterStatusProperty());
        totalAnimeLabel.textProperty().bind(Bindings.size(viewModel.getFilteredList()).asString());

        // event
        filterStatusComboBox.setOnAction(event -> {
            refreshList();
        });

    }

    private void refreshList() {
        trackingFlowPane.getChildren().clear();
        viewModel.getFilteredList().forEach(tracking -> {
            trackingFlowPane.getChildren().add(createCard(tracking));
        });
    }

    private TrackingCard createCard(TrackingDto dto) {
        TrackingCard card = new TrackingCard();
        card.setData(dto);

        card.getDeleteButton().setOnAction(e -> {
            viewModel.deleteTrackingById(dto.getTrackingId());
           boolean isRemove = AlertUtil.showConfirmationAlert(
                Alert.AlertType.CONFIRMATION,
                myListBorderPane.getScene().getWindow(),
                "Delete Confirmation",
                "Are you sure you want to delete this tracking?"
            );
           if (isRemove) {
               trackingFlowPane.getChildren().remove(card);
               AlertUtil.showAlert(AlertType.INFORMATION, myListBorderPane.getScene().getWindow(),
                       "Deleted", "Tracking deleted successfully.");
           }

        });

        card.getTrackingStatusComboBox().setOnAction(e -> {
            viewModel.updateTrackingCardInfo(card);
            refreshList();
        });

        card.getScheduleDayComboBox().setOnAction(e -> {
            viewModel.updateTrackingCardInfo(card);
        });

        card.getNoteTextArea().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // focus lost
                viewModel.updateTrackingCardInfo(card);
            }
        });

        card.getRating().ratingProperty().addListener((obs, oldVal, newVal) -> {
            viewModel.updateTrackingCardInfo(card);
        });

        card.getIncreaseBtn().setOnAction(e -> {
            card.increaseEpisode();
            viewModel.updateTrackingCardInfo(card);
        });

        card.getDecreaseBtn().setOnAction(e -> {
            card.decreaseEpisode();
            viewModel.updateTrackingCardInfo(card);
        });

        card.getTimePicker().valueProperty().addListener((obs, oldVal, newVal) -> {
            viewModel.updateTrackingCardInfo(card);
        });



        return card;
    }

    @Override
    public void save() {
        if (viewModel != null) {
            viewModel.save();
        }
    }

}
