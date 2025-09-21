package com.project;

import java.time.LocalTime;

import com.project.navigation.SceneManager;
import com.project.navigation.View;
import com.project.shared.CustomTimePicker;
import com.project.util.AssetUtil;
import com.project.util.JpaUtil;
import com.project.util.SaveRegistry;
import com.project.util.SeedData;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage primaryStage;
    @Override
    public void start(Stage stage) throws Exception {
        // SEED DATA //
        SeedData.seeds();

        // ---------- Setup Primary Stage ------- //
        primaryStage = stage;
        primaryStage.setTitle("Tracking Anime Application");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(900);
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(new VBox()));

        // inject Stage to SceneManager
        // SceneManager sceneManager = new SceneManager(primaryStage); // not change
        // sceneManager.switchTo(View.USER_DASHBOARD);
        Parent root = AssetUtil.loadFXML("/DashboardView.fxml");


        // CustomTimePicker myTimePicker = new CustomTimePicker();

        // Set giá trị ban đầu
        // myTimePicker.setValue(LocalTime.of(19, 30)); // 7:30 PM

        // Button showButton = new Button("Lấy thời gian");
        // Label resultLabel = new Label();

        // showButton.setOnAction(e -> {
        //     resultLabel.setText("Thời gian được chọn: " + myTimePicker.getValue());
        // });

        // VBox root = new VBox(20, myTimePicker, showButton, resultLabel);

        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    @Override
    public void stop(){
        SaveRegistry.saveAll();
        JpaUtil.shutdown();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
