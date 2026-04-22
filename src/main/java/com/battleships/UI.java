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
import javafx.scene.layout.HBox;
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
    private Rectangle[] leftPaneRects;
    private GridPane rightPaneGrid;
    private Rectangle[][] cells;
    private Rectangle[] botPaneRects;
    public int shipRow;

    public UI(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
        initialize();
    }
    
    private void initialize() {
        rightPaneGrid = new GridPane();
        leftPaneShips = new Ship[] {new Ship(2), new Ship(2), new Ship(3), new Ship(4)};
        cells = new Rectangle[15][15];
        leftPaneRects = new Rectangle[] {new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle()};
        botPaneRects = new Rectangle[10];

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
        setBorderPaneBottom(borderPane);
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
        leftVbox.getChildren().add(leftPaneLabel);
        for (int i = 0; i < leftPaneRects.length; i++) {
            setLeftPaneShip(leftPaneRects[i], i);
            leftVbox.getChildren().add(leftPaneRects[i]);
        }
        leftVbox.setAlignment(Pos.TOP_RIGHT);
        leftVbox.setPrefWidth(190);
        borderPane.setLeft(leftVbox);
    }

    public void setBorderPaneRight(BorderPane borderPane) {
        VBox rightVbox = new VBox(315);
        BorderPane.setMargin(rightVbox, new Insets(VBOX_TOP_MARGIN, 0, 0, 0));
        Label label = new Label("");
        
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

    public void setBorderPaneBottom(BorderPane borderPane) {
        VBox botVbox = new VBox(50);
        HBox manaHbox = new HBox(0);
        HBox abilityHbox = new HBox(30);
        BorderPane.setMargin(manaHbox, new Insets(0, 0, 90, 0));
        for (int i = 0; i < botPaneRects.length; i++) {
            botPaneRects[i] = new Rectangle(40, 25);
            manaHbox.getChildren().add(botPaneRects[i]);
        }
        manaHbox.setAlignment(Pos.CENTER);

        Button ability1 = new Button("mine");
        ability1.setOnAction(event -> {
            if (controller.getCurrentActivePlayer().getMana() >= 1) {
                controller.handlePlaceMine();
                updateMana();
            } else {
                controller.falseMoveAlert("Not enough mana!");
            }
        });
        Button ability2 = new Button("radar");
        ability2.setOnAction(event -> {
            if (controller.getCurrentActivePlayer().getMana() >= 3) {
                controller.handleRadar();
                updateMana();
            } else {
                controller.falseMoveAlert("Not enough mana!");
            }
        });
        Button ability3 = new Button("mortar");
        ability3.setOnAction(event -> {
            if (controller.getCurrentActivePlayer().getMana() >= 5) {
                controller.handleMortar();
                updateMana();
            } else {
                controller.falseMoveAlert("Not enough mana!");
            }
        });
        abilityHbox.getChildren().addAll(ability1, ability2, ability3);
        abilityHbox.setAlignment(Pos.CENTER);

        botVbox.getChildren().addAll(manaHbox, abilityHbox);
        borderPane.setBottom(botVbox);
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
            leftPaneShips = controller.getCurrentActivePlayer().equals(controller.getPlayer1())
             ? controller.getPlayer2().getShips() 
             : controller.getPlayer1().getShips();
        }
        for (int i = 0; i < leftPaneShips.length; i++) {
            if (leftPaneShips[i].isDestroyed() == false) {
                leftPaneRects[i].setFill(Color.BROWN);
            } else {
                leftPaneRects[i].setFill(Color.BLACK);
            }
        }
    }

    public void updateMana() {
        for (int i = 0; i < Player.MAX_MANA; i++) {
            if (i < controller.getCurrentActivePlayer().getMana()) {
                botPaneRects[i].setFill(Color.BLUE);
            } else {
                botPaneRects[i].setFill(Color.BLACK);
            }
            botPaneRects[i].setStroke(Color.BLACK);
        }
    }
    
    public void updateRightPaneGrid(String[][] waterSpots) {
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                Rectangle cell = new Rectangle(10, 10);
                cell.setStroke(Color.BLACK);
                switch (waterSpots[row][col]) {
                    case "empty":
                        cell.setFill(Color.rgb(95, 162, 204)); break;
                    case "hidden":
                        cell.setFill(Color.BROWN); break;
                    case "hit":
                        cell.setFill(Color.BLACK); break;
                    case "miss":
                        cell.setFill(Color.rgb(18, 100, 201)); break;
                    case "mine":
                        cell.setFill(Color.rgb(40, 58, 72)); break;
                }
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
