package com.battleships;

public class Ship {
    private final int length;
    private String rotation;
    private boolean isPlaced;
    private boolean isDestroyed;

    public Ship(int length) {
        this.length = length;
        rotation = "vertical";
        isPlaced = false;
        isDestroyed = false;
    }

    public boolean getDestroyed() {
        return isDestroyed;
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
        return isPlaced;
    }

    public void setPlaced(boolean placed) {
        this.isPlaced = placed;
    }

}
