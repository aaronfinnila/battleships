package com.battleships;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UI {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final String TITLE = "Battleships";

    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private GameCanvas gameCanvas;
    private KeyHandler keyHandler;

    public UI(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        root = new BorderPane();

        gameCanvas = new GameCanvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        root.setCenter(gameCanvas);

        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        keyHandler = new KeyHandler();
        scene.setOnKeyPressed(keyHandler::handleKeyPressed);
        scene.setOnKeyReleased(keyHandler::handleKeyReleased);

        // Configure the stage
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    public void show() {
        stage.show();
        gameCanvas.startCanvas();
    }

    public void cleanup() {
        if (gameCanvas != null) {
            gameCanvas.stopCanvas();
        }
    }

    public GameCanvas getGameCanvas() {
        return gameCanvas;
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }
}
