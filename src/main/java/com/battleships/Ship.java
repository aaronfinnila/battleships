package com.battleships;

public class Ship {
    private final int length;
    private String rotation;
    private boolean placed;
    private boolean destroyed;
    private int positionX;
    private int positionY;

    public Ship(int length) {
        this.length = length;
        rotation = "vertical";
        placed = false;
        destroyed = false;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public int getLength() {
        return length;
    }

    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

}
