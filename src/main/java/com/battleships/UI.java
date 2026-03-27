package com.battleships;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UI {

    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 900;
    public static final String TITLE = "Battleships";
    public boolean gameStarted = false;

    private Stage stage;
    private Scene scene;
    private StackPane root;
    private BorderPane gameViewPane;
    private GameCanvas gameCanvas;
    private KeyHandler keyHandler;
    private VBox startMenuVbox;

    public UI(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        root = new StackPane();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        keyHandler = new KeyHandler();
        
        startMenuVbox = createStartMenuVBox();
        gameViewPane = createGameViewPane();
        
        root.getChildren().setAll(startMenuVbox);

        stage.setTitle(TITLE);
        
        scene.setOnKeyPressed(keyHandler::handleKeyPressed);
        scene.setOnKeyReleased(keyHandler::handleKeyReleased);
        
        stage.setScene(scene);
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

    public VBox createStartMenuVBox() {
        VBox vbox = new VBox();

        Label title = new Label(TITLE);
        Button startGameButton = new Button("Start Game");
        Button settingsButton = new Button("Settings");
        
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.getChildren().addAll(title, startGameButton, settingsButton);
        startGameButton.setOnAction(e -> {
            root.getChildren().setAll(gameViewPane);
        });

        return vbox;
    }

    public BorderPane createGameViewPane() {
        BorderPane borderPane = new BorderPane();
        setBorderPaneCenter(borderPane);
        setBorderPaneTop(borderPane);
        setBorderPaneLeft(borderPane);
        setBorderPaneRight(borderPane);

        return borderPane;
    }

    public void setBorderPaneLeft(BorderPane borderPane) {
        Label leftLabel = new Label("Enemy Ships:");
        BorderPane.setAlignment(leftLabel, Pos.TOP_CENTER);
        BorderPane.setMargin(leftLabel, new Insets(200, 0, 0, 115));
        borderPane.setLeft(leftLabel);
    }

    public void setBorderPaneRight(BorderPane borderPane) {
        Label rightLabel = new Label("Abilities:");
        BorderPane.setAlignment(rightLabel, Pos.TOP_CENTER);
        BorderPane.setMargin(rightLabel, new Insets(200, 140, 0, 0));
        borderPane.setRight(rightLabel);
    }

    public void setBorderPaneTop(BorderPane borderPane) {
        Label topLabel = new Label(TITLE);
        BorderPane.setAlignment(topLabel, Pos.CENTER);
        BorderPane.setMargin(topLabel, new Insets(40, 0, 0, 0));
        borderPane.setTop(topLabel);
    }

    public void setBorderPaneCenter(BorderPane borderPane) {
        gameCanvas = new GameCanvas(525, 525);
        borderPane.setCenter(gameCanvas);
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
