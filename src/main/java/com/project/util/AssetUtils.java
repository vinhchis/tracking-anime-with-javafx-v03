package com.project.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import com.project.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;

public class AssetUtils {
    private static final String BASE = "/com/project/view";

     public static Parent loadFXML(String name) {
        String fxmlPath = BASE + name;
        try {
            URL url = Main.class.getResource(fxmlPath);
            if (url == null) throw new IllegalArgumentException("FXML not found: " + fxmlPath);
            return FXMLLoader.load(url);
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML: " + fxmlPath, e);
        }
    }

    // /resource/images/<name>
    public static Image getImage(String path) {
        try {
            InputStream imageStream = Main.class.getResourceAsStream("/images/" + path);
            if (imageStream == null) {
                System.err.println("Can't find image from : " + path);
                return null;
            }
            return new Image(imageStream);

        } catch (Exception e) {
            System.err.println("Can't load image: " + path);
            e.printStackTrace();
            return null;
        }
    }

    // get from computer ~.tracking-anime/images/<filename>
    public static Image getImageFromComputer(String filename) {
        String userHome = System.getProperty("user.home"); // C:\Users\<user> |/home/<user>
        Path storageDirectory = Paths.get(userHome, ".tracking-anime", "images");
        if (filename == null || filename.isBlank()) {
            return null;
        }

        try {
            Path imagePath = storageDirectory.resolve(filename);
            if (Files.exists(imagePath)) {
                return new Image(imagePath.toUri().toString());
            }
        } catch (Exception e) {
            System.err.println("Can't get file: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


}
/*
 * src/main/resources/images/logo.png
 */
