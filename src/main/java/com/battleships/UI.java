package com.battleships;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class UI {

    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 900;
    public static final double VBOX_TOP_MARGIN = 180;
    public static final String TITLE = "Battleships";

    private GameController controller;
    private Stage stage;
    private Scene scene;
    private StackPane root;
    private BorderPane gameViewPane;
    private GameCanvas gameCanvas;
    private KeyHandler keyHandler;
    private VBox startMenuVbox;
    private Label turnLabel;
    public int shipRow;

    public UI(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
        initialize();
    }
    
    private void initialize() {
        root = new StackPane();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        keyHandler = new KeyHandler(controller, this);
        
        startMenuVbox = createStartMenuVBox();
        gameViewPane = createGameViewPane();
        
        root.getChildren().setAll(startMenuVbox);

        stage.setTitle(TITLE);
        
        scene.setOnKeyPressed(keyHandler::handleKeyPressed);
        scene.setOnKeyReleased(keyHandler::handleKeyReleased);
        
        stage.setScene(scene);
        shipRow = 0;
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
        VBox vbox = new VBox(35);

        Label title = new Label(TITLE);
        Button startGameButton = new Button("Start Game");
        
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(title, startGameButton);
        startGameButton.setOnAction(event -> {
            startGame();
        });

        return vbox;
    }

    public void startGame() {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Input");
        dialog.setHeaderText("Enter player 1 name");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(input -> {
            controller.getPlayer1().setName(input);

            TextInputDialog dialog2 = new TextInputDialog();
            dialog2.setTitle("Input");
            dialog2.setHeaderText("Enter player 2 name");
            dialog2.setContentText("Name:");

            Optional<String> result2 = dialog2.showAndWait();

            result2.ifPresent(input2 -> {
                controller.getPlayer2().setName(input2);
            });
        });

        updateTurnLabel();
        root.getChildren().setAll(gameViewPane);
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
        VBox leftVbox = new VBox(25);
        BorderPane.setMargin(leftVbox, new Insets(VBOX_TOP_MARGIN, 0, 0, 0));
        int shipSize = gameCanvas.SPOT_SIZE-3;
        Label label = new Label("");
        if (controller.getGameState() == controller.HIDESTATE) {
            label.setText("Your ships:");
        } else if (controller.getGameState() == controller.SHOOTSTATE) {
            label.setText("Enemy ships:");
        }
        label.setTranslateX(20);
        Rectangle ship1 = new Rectangle(shipSize, shipSize*2);
        ship1.setFill(Color.BROWN);
        Rectangle ship2 = new Rectangle(shipSize, shipSize*2);
        ship2.setFill(Color.BROWN);
        Rectangle ship3 = new Rectangle(shipSize, shipSize*3);
        ship3.setFill(Color.BROWN);
        Rectangle ship4 = new Rectangle(shipSize, shipSize*4);
        ship4.setFill(Color.BROWN);
        leftVbox.getChildren().addAll(
            label,
            ship1, ship2, ship3, ship4
        );
        leftVbox.setAlignment(Pos.TOP_RIGHT);
        leftVbox.setPrefWidth(190);
        borderPane.setLeft(leftVbox);
    }

    public void setBorderPaneRight(BorderPane borderPane) {
        VBox rightVbox = new VBox(25);
        BorderPane.setMargin(rightVbox, new Insets(VBOX_TOP_MARGIN, 0, 0, 0));
        Label label = new Label("Abilities:");
        rightVbox.getChildren().setAll(label);
        rightVbox.setAlignment(Pos.TOP_LEFT);
        rightVbox.setPrefWidth(220);
        borderPane.setRight(rightVbox);
    }

    public void setBorderPaneTop(BorderPane borderPane) {
        VBox topVbox = new VBox(35);
        turnLabel = new Label(controller.getCurrentActivePlayer().getName() + "'s turn");
        topVbox.getChildren().addAll(new Label("Battleships"),
        turnLabel
        );
        topVbox.setAlignment(Pos.CENTER);
        borderPane.setTop(topVbox);
    }

    public void setBorderPaneCenter(BorderPane borderPane) {
        gameCanvas = new GameCanvas(525, 525, controller, this);
        borderPane.setCenter(gameCanvas);
    }

    public void updateTurnLabel() {
        turnLabel.setText(controller.getCurrentActivePlayer().getName() + "'s turn");
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
