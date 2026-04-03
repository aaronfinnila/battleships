package com.battleships;

public class Player {

    public static final int MAX_MANA = 10;
    private String name;
    private int mana;
    // use this to track status of water spots. 
    // for example: hidden, empty, hit, miss
    private String waterSpotsStatus[][];
    private Ship ships[];
    private Ship equippedShip;
    private boolean shipsPlaced;

    public Player(String name) {
        this.name = name;
        mana = 0;
        waterSpotsStatus = new String[15][15];
        ships = new Ship[] {new Ship(2), new Ship(2), new Ship(3), new Ship(4)};
        equippedShip = ships[0];
        shipsPlaced = false;
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 15; x++) {
                waterSpotsStatus[y][x] = "empty";
            }
        }
    }

    public void setShipsPlaced(boolean shipsPlaced) {
        this.shipsPlaced = shipsPlaced;
    }

    public boolean getShipsPlaced() {
        return shipsPlaced;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ship getEquippedShip() {
        return equippedShip;
    }

    public void changeEquippedShip(int index) {
        equippedShip = ships[index];
    }

    public Ship[] getShips() {
        return ships;
    }

    public String getName() {
        return name;
    }

    public String[][] getWaterSpotsStatus() {
        return waterSpotsStatus;
    }

    public int getMana() {
        return mana;
    }
}
