package com.battleships;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class KeyHandler {
    private Set<KeyCode> pressedKeys;

    public KeyHandler() {
        pressedKeys = new HashSet<>();
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
        // Examples:
        // switch (keyCode) {
        //     case UP, W -> moveUp();
        //     case DOWN, S -> moveDown();
        //     case LEFT, A -> moveLeft();
        //     case RIGHT, D -> moveRight();
        //     case ENTER, SPACE -> confirmAction();
        //     case ESCAPE -> pauseGame();
        // }
    }

    protected void onKeyReleased(KeyCode keyCode) {
        
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
