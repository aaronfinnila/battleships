package com.battleships;

public class Player {

    public static final int MAX_MANA = 10;
    private String name;
    private int mana;
    // use this to track status of water spots. 
    // values: hidden, empty, hit, miss, mine, radar
    private String waterSpots[][];
    private int radarMap[][];
    private Ship ships[];
    private Ship equippedShip;
    private boolean shipsPlaced;
    private boolean hideMine;
    private boolean shootMortar;
    private boolean placeRadar;
    private boolean lostGame;
    private boolean shotUsed;

    public Player(String name) {
        this.name = name;
        mana = 2;
        waterSpots = new String[15][15];
        radarMap = new int[15][15];
        ships = new Ship[] {new Ship(2), new Ship(2), new Ship(3), new Ship(4)};
        equippedShip = ships[0];
        shipsPlaced = false;
        hideMine = false;
        lostGame = false;
        shotUsed = false;
        placeRadar = false;
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
            if (ship.isDestroyed() == false && waterSpots[y][x].equals("hit")) {
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
        boolean allDestroyed = true;
        for (int i = 0; i < ships.length; i++) {
            if (ships[i].isDestroyed() == false) {
                allDestroyed = false;
            }
        }
        if (allDestroyed == true) {
            lostGame = true;
        }
    }

    public boolean waterSpotsInclude(String value) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (waterSpots[j][i].equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setShotUsed(boolean shotUsed) {
        this.shotUsed = shotUsed;
    }

    public boolean getShotUsed() {
        return shotUsed;
    }

    public boolean getLostGame() {
        return lostGame;
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

    public boolean getHideMine() {
        return hideMine;
    }

    public void setHideMine(boolean hideMine) {
        this.hideMine = hideMine;
    }

    public boolean getShootMortar() {
        return shootMortar;
    }

    public void setShootMortar(boolean shootMortar) {
        this.shootMortar = shootMortar;
    }

    public boolean getPlaceRadar() {
        return placeRadar;
    }

    public void setPlaceRadar(boolean placeRadar) {
        this.placeRadar = placeRadar;
    }

    public void setRadarMapPoint(int x, int y, int shootablesAmount) {
        radarMap[y][x] = shootablesAmount;
    }

    public int getRadarMapPoint(int x, int y) {
        return radarMap[y][x];
    }

    public String[][] getWaterSpots() {
        return waterSpots;
    }

    public String getWaterSpot(int x, int y) {
        return waterSpots[y][x];
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
        }
    }
}
