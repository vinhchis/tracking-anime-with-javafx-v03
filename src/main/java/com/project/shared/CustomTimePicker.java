package com.project.shared;

import java.time.LocalTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;

public class CustomTimePicker extends HBox {
    private ComboBox<Integer> hourPicker;
    private ComboBox<Integer> minutePicker;
    private ToggleButton amPmToggle;
    private Label separatorLabel = new Label(":");
    private final ObjectProperty<LocalTime> value = new SimpleObjectProperty<>();

    public CustomTimePicker() {
        hourPicker = new ComboBox<>();
        minutePicker = new ComboBox<>();
        amPmToggle = new ToggleButton("AM");

        this.getStylesheets().add(
                getClass().getResource("/com/project/css/custom-timepicker.css").toExternalForm());
        this.getStyleClass().add("custom-time-picker");
        hourPicker.getStyleClass().add("time-picker-combo");
        minutePicker.getStyleClass().add("time-picker-combo");
        separatorLabel.getStyleClass().add("time-picker-separator");
        amPmToggle.getStyleClass().add("am-pm-toggle");

        for (int i = 1; i <= 12; i++) {
            hourPicker.getItems().add(i);
        }

        for (int i = 0; i < 60; i += 5) {
            minutePicker.getItems().add(i);
        }

        // --- 3. Sắp xếp Layout ---
        this.setSpacing(5);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(hourPicker, separatorLabel, minutePicker, amPmToggle);


        amPmToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            amPmToggle.setText(newVal ? "PM" : "AM");
            updateValueFromPickers();
        });

        hourPicker.valueProperty().addListener(obs -> updateValueFromPickers());
        minutePicker.valueProperty().addListener(obs -> updateValueFromPickers());

        value.addListener((obs, oldVal, newVal) -> updatePickersFromValue(newVal));

        setValue(LocalTime.now());
    }

    private void updateValueFromPickers() {
        Integer hour = hourPicker.getValue();
        Integer minute = minutePicker.getValue();

        if (hour != null && minute != null) {
            int hour24 = hour;
            if (hour24 == 12) {
                hour24 = amPmToggle.isSelected() ? 12 : 0;
            } else if (amPmToggle.isSelected()) { // PM
                hour24 += 12;
            }
            setValue(LocalTime.of(hour24, minute));
        }
    }

    private void updatePickersFromValue(LocalTime time) {
        if (time == null) {
            hourPicker.setValue(null);
            minutePicker.setValue(null);
            return;
        }

        int hour12 = time.getHour() % 12;
        if (hour12 == 0) {
            hour12 = 12;
        }

        boolean isPm = time.getHour() >= 12;

        hourPicker.setValue(hour12);
        minutePicker.setValue(time.getMinute());
        amPmToggle.setSelected(isPm);
    }

    public LocalTime getValue() {
        return value.get();
    }

    public void setValue(LocalTime time) {
        value.set(time);
    }

    public ObjectProperty<LocalTime> valueProperty() {
        return value;
    }
}
