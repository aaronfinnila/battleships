package com.battleships;

public class Player {

    public static final int MAX_MANA = 10;
    private String name;
    private int mana;
    private String waterSpotsStatus[][];

    public Player(String name) {
        this.name = name;
        mana = 0;
        waterSpotsStatus = new String[15][15];
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 15; x++) {
                waterSpotsStatus[y][x] = "empty";
            }
        }
    }

    public void setName(String name) {
        this.name = name;
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
