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

public class AssetUtil {
    private static final String BASE = "/com/project";

     public static Parent loadFXML(String name) {
        String fxmlPath = BASE + "/view" + name;
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

    // get image from images folder of project (on project/images/<filename> folder)
    public static Image getImageFromProject(String filename) {
        if (filename == null || filename.isBlank()) {
            return null;
        }
        try {
            Path imagePath = Paths.get(System.getProperty("user.dir"),"images", filename);
            if (Files.exists(imagePath)) {
                return new Image(imagePath.toUri().toString());
            }else{
               // fallback to resource images
            }
        } catch (Exception e) {
            System.err.println("Can't get file: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public static String getCss(String name){
        String pathName = BASE + "/css/" + name;
        return Objects.requireNonNull(Main.class.getResource(pathName)).toExternalForm();
    }

    // "https://cdn.myanimelist.net/images/anime/1015/138006.jpg"
    public static Image getImageFromLink(String posterUrl) {
        try {
            return new Image(posterUrl, true);
        } catch (Exception e) {
            System.err.println("Can't load image from link: " + posterUrl);
            e.printStackTrace();
            return null;
        }
    }

}

