package com.project.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageService {
    private static final String IMAGE_DIR = "images";
    private final Path imageDirectory;

    public ImageService() {
        imageDirectory = Paths.get(System.getProperty("user.dir"), IMAGE_DIR); // root/images
        try {
            Files.createDirectories(imageDirectory);
        } catch (IOException e) {
            System.err.println("Failed to create image directory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void uploadImage(File imageFile) {
        try {
            Path targetPath = imageDirectory.resolve(imageFile.getName());
            if (Files.exists(targetPath)) {
                System.out.println("Image already exists, skipping upload: " + targetPath);
                return;
            }

            Files.copy(imageFile.toPath(), targetPath,StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Image uploaded successfully: " + targetPath);
        } catch (IOException e) {
            System.err.println("Failed to upload image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteImage(String imagePath) {
        try {
            Path targetPath = imageDirectory.resolve(imagePath);
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
}
