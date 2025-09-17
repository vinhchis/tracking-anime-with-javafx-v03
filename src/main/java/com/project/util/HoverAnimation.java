package com.project.util;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class HoverAnimation {
       public static void install(Node node) {
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            node.setScaleX(0.9);
            node.setScaleY(0.9);
        });

        node.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            node.setScaleX(1.0);
            node.setScaleY(1.0);
        });

        node.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            node.setScaleX(1.05);
            node.setScaleY(1.05);
        });

        node.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            node.setScaleX(1.05);
            node.setScaleY(1.05);
        });

        node.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
            node.setScaleX(1.0);
            node.setScaleY(1.0);
        });

    }
}
