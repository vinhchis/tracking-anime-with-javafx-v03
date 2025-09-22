package com.project.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SaveRegistry {
    private static final List<Saveable> REGISTRY = new CopyOnWriteArrayList<>();

    public static void register(Saveable s) {
        if (s != null && !REGISTRY.contains(s)) {
            REGISTRY.add(s);
        }
    }

    public static void unregister(Saveable s) {
        REGISTRY.remove(s);
    }

    public static void saveAll() {
        for (Saveable s : REGISTRY) {
            try {
                s.save();
            } catch (Exception e) {
                System.err.println("Save failed for " + s + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}