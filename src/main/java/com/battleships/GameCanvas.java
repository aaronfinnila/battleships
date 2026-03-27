package com.battleships;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameCanvas extends Canvas {

    private GraphicsContext gc;
    private AnimationTimer gameLoop;
    private boolean running;

    private long lastUpdateTime;
    private double deltaTime;

/*     private static final int TARGET_FPS = 60;
    private static final double TARGET_TIME = 1_000_000_000.0 / TARGET_FPS; */

    public GameCanvas(double width, double height) {
        super(width, height);
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

    public void update(double dt) {}

    public void render() {
        clearCanvas();
        drawPlaceholder();
    }

    private void clearCanvas() {
        gc.setFill(Color.LIGHTSKYBLUE);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawPlaceholder() {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        double cellSize = 35;
        double offsetX = 0;
        double offsetY = 0;

        for (double y = 0; y < 16; y++) {
            for (double x = 0; x < 16; x++) {
                gc.strokeLine(offsetX+x*cellSize, offsetY+y*cellSize,
                    offsetX+(x+1)*cellSize, offsetY+y*cellSize);
                gc.strokeLine(offsetX+x*cellSize, offsetY+y*cellSize,
                    offsetX+x*cellSize, offsetY+(y+1)*cellSize);
            }
        }
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
