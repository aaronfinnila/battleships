package com.battleships;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class KeyHandler {
    private Set<KeyCode> pressedKeys;
    private GameController controller;
    private UI ui;

    public KeyHandler(GameController controller, UI ui) {
        pressedKeys = new HashSet<>();
        this.controller = controller;
        this.ui = ui;
    }

    public void handleKeyPressed(KeyEvent event) {
        pressedKeys.add(event.getCode());
        onKeyPressed(event.getCode());
    }

    public void handleKeyReleased(KeyEvent event) {
        pressedKeys.remove(event.getCode());
        onKeyReleased(event.getCode());
    }

    protected void onKeyPressed(KeyCode keyCode) {
        switch (keyCode) {
            case UP, W -> handleUp();
            case DOWN, S -> handleDown();
            case LEFT, A -> handleLeft();
            case RIGHT, D -> handleRight();
            default -> {}
        }
    }

    protected void onKeyReleased(KeyCode keyCode) {
        
    }

    public void handleUp() {
        if (controller.getGameState() == controller.HIDESTATE) {
            if (ui.shipRow <= 4 && ui.shipRow > 0) {
                ui.shipRow--;
            }
        }
    }

    public void handleDown() {
        if (controller.getGameState() == controller.HIDESTATE) {
            if (ui.shipRow >= 0 && ui.shipRow < 4) {
                ui.shipRow++;
            }
        }
    }
    
    public void handleLeft() {
        if (controller.getGameState() == controller.HIDESTATE) {
            Ship ship = controller.getCurrentActivePlayer().getEquippedShip();
            String rotation = ship.getRotation();
            if (rotation == "vertical") {
                ship.setRotation("horizontal");
            } else {
                ship.setRotation("vertical");
            }
        }
    }

    public void handleRight() {
        if (controller.getGameState() == controller.HIDESTATE) {
            Ship ship = controller.getCurrentActivePlayer().getEquippedShip();
            String rotation = ship.getRotation();
            if (rotation == "vertical") {
                ship.setRotation("horizontal");
            } else {
                ship.setRotation("vertical");
            }
        }
    }

    public boolean isKeyPressed(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public boolean isUpPressed() {
        return isKeyPressed(KeyCode.UP) || isKeyPressed(KeyCode.W);
    }

    public boolean isDownPressed() {
        return isKeyPressed(KeyCode.DOWN) || isKeyPressed(KeyCode.S);
    }

    public boolean isLeftPressed() {
        return isKeyPressed(KeyCode.LEFT) || isKeyPressed(KeyCode.A);
    }

    public boolean isRightPressed() {
        return isKeyPressed(KeyCode.RIGHT) || isKeyPressed(KeyCode.D);
    }

    public boolean isConfirmPressed() {
        return isKeyPressed(KeyCode.ENTER) || isKeyPressed(KeyCode.SPACE);
    }

    public boolean isEscapePressed() {
        return isKeyPressed(KeyCode.ESCAPE);
    }

    public void clearPressedKeys() {
        pressedKeys.clear();
    }

    public Set<KeyCode> getPressedKeys() {
        return new HashSet<>(pressedKeys);
    }
}
