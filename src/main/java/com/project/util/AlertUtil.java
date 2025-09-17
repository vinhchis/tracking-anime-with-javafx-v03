package com.project.util;

import org.controlsfx.control.Notifications;

import com.project.Main;

import javafx.scene.control.Alert;
import javafx.stage.Window;
import javafx.util.Duration;

public class AlertUtil {
    public static boolean result = false;

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        if (alertType.name() == null ? alertType.INFORMATION.name() == null : alertType.name().equals(alertType.INFORMATION.name())) {
            Notifications.create()
                    .owner(owner)
                    .darkStyle()
                    .title(title)
                    .text(message).hideAfter(Duration.seconds(10))
                    .showInformation();
        } else if (alertType.name() == null ? Alert.AlertType.ERROR.name() == null : alertType.name().equals(Alert.AlertType.ERROR.name())) {
            Notifications.create()
                    .owner(owner)
                    .darkStyle()
                    .title(title)
                    .text(message).hideAfter(Duration.seconds(10))
                    .showError();
        }
    }
}
