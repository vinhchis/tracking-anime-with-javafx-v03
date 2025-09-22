package com.project.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
            if (url == null)
                throw new IllegalArgumentException("FXML not found: " + fxmlPath);
            return FXMLLoader.load(url);
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML: " + fxmlPath, e);
        }
    }


    // ---------IMAGE ----------///
    // get image from images folder of project (on project/images/<filename> folder)
    public static Image getImageFromProject(String filename) {
        if (filename == null || filename.isBlank()) {
            return null;
        }
        try {
            Path imagePath = Paths.get(System.getProperty("user.dir"), "images", filename); // root/images/<filename>
            if (Files.exists(imagePath)) {
                return new Image(imagePath.toUri().toString());
            } else {
                // fallback to resource image
                return getFallbackImage();
            }
        } catch (Exception e) {
            System.err.println("Can't get file: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getCss(String name) {
        String pathName = BASE + "/css/" + name;
        return Objects.requireNonNull(Main.class.getResource(pathName)).toExternalForm();
    }

    // ---------IMAGE ----------///
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

    // $HOME/.tracking-anime/images/<filename>
    // posterUrl = "https://cdn.myanimelist.net/images/anime/1015/138006.jpg"
    public static String saveImage(String fileURL) {
        if (fileURL == null || fileURL.isBlank()) {
            return null;
        }
        String filename = fileURL.substring(fileURL.lastIndexOf('/') + 1);
        if (filename.isBlank()) {
            return null;
        }

        Path storageImage = Paths.get(System.getProperty("user.home"), ".tracking-anime", "images");
        if(!Files.exists(storageImage)) {
            try {
                Files.createDirectories(storageImage);
                System.out.println("Created directory: " + storageImage);
            } catch (IOException e) {
                System.err.println("Failed to create directory: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        Path savePath = storageImage.resolve(filename);
        try {
            URL url = new URL(fileURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                try (InputStream in = conn.getInputStream()) {
                    Files.copy(in, savePath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("✅ File downloaded: " + savePath);
                }
            } else {
                System.out.println("❌ Errors: HTTP " + status);
            }
        } catch (IOException e) {
            System.out.println("⚠️ Can't save file: " + e.getMessage());
        }

        return filename;
    }

    public static void deleteImage(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return;
        }
        try {
            Path targetPath = Paths.get(System.getProperty("user.home"), ".tracking-anime", "images", imagePath);
            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
                System.out.println("Image deleted successfully: " + targetPath);
            } else {
                System.out.println("Image not found, cannot delete: " + targetPath);
            }
        } catch (IOException e) {
            System.err.println("Failed to delete image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // IMAGE FROM LOCAL PATH // $HOME.tracking-animes/images/filename.png
    public static Image getImageFromLocal(String postUrl) {
        if (postUrl == null || postUrl.isBlank()) {
            return getFallbackImage();
        }
        try {
            Path storageImage = Paths.get(System.getProperty("user.home"), ".tracking-anime", "images");
            Path imagePath = storageImage.resolve(postUrl);
            if (Files.exists(imagePath)) {
                return new Image(imagePath.toUri().toString());
            } else {
                System.out.println("Image not found in local path: " + imagePath);
                return getFallbackImage();
            }
        } catch (Exception e) {
            System.err.println("Can't get file: " + postUrl + "\n" + e.getMessage());
            e.printStackTrace();
        }
        return getFallbackImage();
    }


    public static Image getFallbackImage() {
        Path imagePath = Paths.get(System.getProperty("user.dir"), "images", "placeholder_300x250.png");
        if (Files.exists(imagePath)) {
            return new Image(imagePath.toUri().toString());
        }
        return null;
    }
}
