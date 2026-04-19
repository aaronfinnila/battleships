package com.battleships;

public class Player {

    public static final int MAX_MANA = 10;
    private String name;
    private int mana;
    // use this to track status of water spots. 
    // values: hidden, empty, hit, miss
    // TODO: UI setborderpanebottom: mana bar, ability buttons.
    // TODO: gamecontroller: hiding mines, radar handleSpot (3x3) (can use handleShot with if statement), mortar abilities (handleShot on 3x3, etc)
    private String waterSpots[][];
    private Ship ships[];
    private Ship equippedShip;
    private boolean shipsPlaced;

    public Player(String name) {
        this.name = name;
        mana = MAX_MANA;
        waterSpots = new String[15][15];
        ships = new Ship[] {new Ship(2), new Ship(2), new Ship(3), new Ship(4)};
        equippedShip = ships[0];
        shipsPlaced = false;
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 15; x++) {
                waterSpots[y][x] = "empty";
            }
        }
    }

    public void checkDestroyedShips() {
        for (int i = 0; i < ships.length; i++) {
            Ship ship = ships[i];
            int x = ship.getPositionX();
            int y = ship.getPositionY();
            if (waterSpots[y][x].equals("hit")) {
                boolean destr = true;
                switch (ship.getRotation()) {
                    case "vertical":
                        for (int j = 0; j < ship.getLength(); j++) {
                            if (!waterSpots[y+j][x].equals("hit")) {
                                destr = false;
                            }
                        } break;
                    case "horizontal":
                        for (int j = 0; j < ship.getLength(); j++) {
                            if (!waterSpots[y][x+j].equals("hit")) {
                                destr = false;
                            }
                        } break;
                }
                if (destr == true) {
                    ship.setDestroyed(true);
                }
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

    public String[][] getWaterSpots() {
        return waterSpots;
    }

    public int getMana() {
        return mana;
    }

    public void addMana(int amount) {
        if (mana + amount <= MAX_MANA) {
            mana += amount;
        }
    }

    public void subtractMana(int amount) {
        if (mana - amount >= 0) {
            mana -= amount;
            System.out.println(mana);
        }
    }
}
