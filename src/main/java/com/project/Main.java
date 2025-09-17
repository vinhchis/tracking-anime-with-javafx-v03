package com.project;

import com.project.navigation.SceneManager;
import com.project.navigation.View;
import com.project.util.AssetUtils;
import com.project.util.JpaUtil;
// import com.project.util.SeedData;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage primaryStage;
    @Override
    public void start(Stage stage) throws Exception {
        // SEED DATA //
        // SeedData.seeds();

        // ---------------- //
        primaryStage = stage;
        primaryStage.setScene(new Scene(new VBox()));
        primaryStage.setTitle("Tracking Anime Application");

        // inject Stage to SceneManager
        // SceneManager sceneManager = new SceneManager(primaryStage); // not change
        // sceneManager.switchTo(View.USER_DASHBOARD);
        Parent root = AssetUtils.loadFXML("/DashboardView.fxml");
        primaryStage.getScene().setRoot(root);

        primaryStage.show();
    }

    @Override
    public void stop(){
        JpaUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
