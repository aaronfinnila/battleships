package com.battleships;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
    private Label leftPaneLabel;
    private Ship[] leftPaneShips;
    private GridPane rightPaneGrid = new GridPane();
    private Rectangle[][] cells = new Rectangle[15][15];
    public int shipRow;

    public UI(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
        initialize();
    }
    
    private void initialize() {
        leftPaneShips = new Ship[] {new Ship(2), new Ship(2), new Ship(3), new Ship(4)};
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

        updateLabels();
        updateShips();
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
        leftPaneLabel = new Label("");
        leftPaneLabel.setTranslateX(20);
        Rectangle ship1 = new Rectangle();
        setLeftPaneShip(ship1, 0);
        Rectangle ship2 = new Rectangle();
        setLeftPaneShip(ship2, 1);
        Rectangle ship3 = new Rectangle();
        setLeftPaneShip(ship3, 2);
        Rectangle ship4 = new Rectangle();
        setLeftPaneShip(ship4, 3);
        leftVbox.getChildren().addAll(
            leftPaneLabel,
            ship1, ship2, ship3, ship4
        );
        leftVbox.setAlignment(Pos.TOP_RIGHT);
        leftVbox.setPrefWidth(190);
        borderPane.setLeft(leftVbox);
    }

    public void setBorderPaneRight(BorderPane borderPane) {
        VBox rightVbox = new VBox(200);
        BorderPane.setMargin(rightVbox, new Insets(VBOX_TOP_MARGIN, 0, 0, 0));
        Label label = new Label("Abilities:");
        
        rightVbox.getChildren().add(label);
        rightVbox.getChildren().add(rightPaneGrid);
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

    public void updateLabels() {
        turnLabel.setText(controller.getCurrentActivePlayer().getName() + "'s turn");
        if (controller.getGameState() == controller.HIDESTATE) {
            leftPaneLabel.setText("Your ships:");
        } else if (controller.getGameState() == controller.SHOOTSTATE) {
            leftPaneLabel.setText("Enemy ships:");
        }
    }

    public void updateShips() {
        if (controller.getGameState() == controller.HIDESTATE) {
            leftPaneShips = controller.getCurrentActivePlayer().getShips();
        } else {
            System.out.println("update leftpaneships");
            leftPaneShips = controller.getCurrentActivePlayer().equals(controller.getPlayer1())
             ? controller.getPlayer2().getShips() 
             : controller.getPlayer1().getShips();
        }
    }
    
    public void updateRightPaneGrid() {
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                Rectangle cell = new Rectangle(10, 10);
                cell.setStroke(Color.BLACK);
                cell.setFill(Color.rgb(95, 162, 204));

                cells[row][col] = cell;
                rightPaneGrid.add(cell, col, row);
            }
        }
    }

    public void setLeftPaneShip(Rectangle shipRect, int index) {
        int shipSize = gameCanvas.SPOT_SIZE-3;
        Ship ship = leftPaneShips[index];
        shipRect.setHeight(ship.getLength()*shipSize);
        shipRect.setWidth(shipSize);
        System.out.println(ship.getDestroyed());
        if (ship.getDestroyed() == false) {
            shipRect.setFill(Color.BROWN);
        } else {
            shipRect.setFill(Color.BLACK);
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
