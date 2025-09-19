package com.project.shared;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimePicker extends HBox {
    @FXML private TextField textField;
    @FXML private Spinner<Integer> hourSpinner;
    @FXML private Spinner<Integer> minuteSpinner;
    @FXML private HBox spinnerBox;

    private final PopupControl popup = new PopupControl();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private final ObjectProperty<LocalTime> time = new SimpleObjectProperty<>();

    public TimePicker() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/shared/timepicker.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
        // Spinner giờ
        hourSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
        // Spinner phút
        minuteSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        // popup root là spinnerBox
        // popup.getScene().setRoot(spinnerBox);
        popup.getScene().setRoot(new HBox(hourSpinner, minuteSpinner));


        // Click mở popup
        textField.setOnMouseClicked(e -> {
            if (!popup.isShowing()) {
                popup.show(textField, e.getScreenX(), e.getScreenY());
            }
        });

        // Cập nhật khi chọn
        hourSpinner.valueProperty().addListener((obs, o, n) -> updateTime());
        minuteSpinner.valueProperty().addListener((obs, o, n) -> updateTime());

        setTime(LocalTime.now());
    }

    private void updateTime() {
        LocalTime t = LocalTime.of(hourSpinner.getValue(), minuteSpinner.getValue());
        time.set(t);
        textField.setText(t.format(formatter));
    }

    public void setTime(LocalTime t) {
        if (t != null) {
            time.set(t);
            hourSpinner.getValueFactory().setValue(t.getHour());
            minuteSpinner.getValueFactory().setValue(t.getMinute());
            textField.setText(t.format(formatter));
        }
    }

    public LocalTime getTime() {
        return time.get();
    }

    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }
}

