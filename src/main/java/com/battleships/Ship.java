package com.battleships;

public class Ship {
    private final int length;
    private String rotation;
    private boolean placed;

    public Ship(int length) {
        this.length = length;
        rotation = "vertical";
        placed = false;
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

    public boolean getPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

}
