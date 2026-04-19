package com.battleships;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameCanvas extends Canvas {

    private GraphicsContext gc;
    private AnimationTimer gameLoop;
    private boolean running;
    private GameController controller;
    private UI ui;
    private Player canvasActivePlayer;
    private double mouseX;
    private double mouseY;
    public final int SPOT_SIZE = 35;

    private long lastUpdateTime;
    private double deltaTime;

    public GameCanvas(double width, double height, GameController controller, UI ui) {
        super(width, height);
        this.ui = ui;
        this.controller = controller;
        this.canvasActivePlayer = controller.getCurrentActivePlayer();
        gc = getGraphicsContext2D();
        running = false;
        lastUpdateTime = 0;
        deltaTime = 0;
    }

    public void startCanvas() {
        if (running) {
            return;
        }

        running = true;
        lastUpdateTime = System.nanoTime();

        this.setOnMouseClicked(event -> {
            int xi = (int)(event.getX() / SPOT_SIZE);
            int yi = (int)(event.getY() / SPOT_SIZE);
            controller.handleClick(xi, yi);
        });

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                deltaTime = (currentTime - lastUpdateTime) / 1_000_000_000.0;
                lastUpdateTime = currentTime;

                update(deltaTime);

                render();
            }
        };

        gameLoop.start();
    }

    public void stopCanvas() {
        running = false;
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public void update(double dt) {
        Player currentPlayer = controller.getCurrentActivePlayer();

        if (!currentPlayer.equals(canvasActivePlayer)) {
            canvasActivePlayer = currentPlayer;
            ui.updateLabels();
            ui.updateShips();
            if (controller.getGameState() == controller.SHOOTSTATE) {
                Player enemy = canvasActivePlayer.equals(controller.getPlayer1()) ? controller.getPlayer1() : controller.getPlayer2();
                ui.updateRightPaneGrid(enemy.getWaterSpots());
                ui.updateMana();
            }
        }
    }

    public void render() {
        clearCanvas();
        drawCanvas();
    }

    private void clearCanvas() {
        gc.setFill(Color.rgb(135, 205, 250));
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawCanvas() {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        
        double cellSize = (double) SPOT_SIZE;
        double offsetX = 0;
        double offsetY = 0;
        
        for (double y = 0; y < 15; y++) {
            for (double x = 0; x < 15; x++) {
                gc.strokeLine(offsetX+x*cellSize, offsetY+y*cellSize,
                offsetX+(x+1)*cellSize, offsetY+y*cellSize);
                gc.strokeLine(offsetX+x*cellSize, offsetY+y*cellSize,
                offsetX+x*cellSize, offsetY+(y+1)*cellSize);
                String[][] waterSpots;
                if (controller.getGameState() == controller.HIDESTATE) {
                    waterSpots = controller.getCurrentActivePlayer().getWaterSpots();
                } else {
                    waterSpots = controller.getCurrentActivePlayer().equals(controller.getPlayer1())
                     ? controller.getPlayer2().getWaterSpots()
                     : controller.getPlayer1().getWaterSpots();
                }
                switch (waterSpots[(int) y][(int) x]) {
                    case "hit":
                        gc.setFill(Color.BLACK);
                        gc.fillOval(x*cellSize, y*cellSize, cellSize, cellSize);
                        break;
                    case "miss":
                        gc.setFill(Color.rgb(95, 162, 204));
                        gc.fillOval(x*cellSize, y*cellSize, cellSize, cellSize);
                        break;
                    case "hidden":
                        if (controller.getGameState() == controller.HIDESTATE) {
                            gc.setFill(Color.ORANGE);
                            gc.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
                        }
                        break;
                }
            }
        }

        if (controller.getGameState() == controller.HIDESTATE) {
            this.setOnMouseMoved(event -> {
                mouseX = event.getX();
                mouseY = event.getY();
            });
            gc.setFill(Color.BROWN);
            int length = controller.getCurrentActivePlayer().getEquippedShip().getLength();
            String rotation = controller.getCurrentActivePlayer().getEquippedShip().getRotation();
            int coordinateX = getSpotCoordinateX(mouseX);
            int coordinateY = getSpotCoordinateY(mouseY);
            switch (rotation) {
                case "vertical":
                    gc.fillRect(coordinateX*SPOT_SIZE, coordinateY*SPOT_SIZE, SPOT_SIZE, SPOT_SIZE*length);
                    break;
                case "horizontal":
                    gc.fillRect(coordinateX*SPOT_SIZE, coordinateY*SPOT_SIZE, SPOT_SIZE*length, SPOT_SIZE);
                    break;
            }
        }
    }

    public int getSpotCoordinateX(double mouseX) {
        int coordinate = (int) Math.floor(mouseX/SPOT_SIZE);
        return coordinate;
    }

    public int getSpotCoordinateY(double mouseY) {
        int coordinate = (int) Math.floor(mouseY/SPOT_SIZE);
        return coordinate;
    }

    public GraphicsContext getGC() {
        return gc;
    }

    public boolean isRunning() {
        return running;
    }

    public double getDeltaTime() {
        return deltaTime;
    }
}
