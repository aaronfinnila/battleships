package com.battleships;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GameController {
    
    private Player player1;
    private Player player2;
    private String currentActivePlayer;
    private int currentShipIndex;
    private int gameState;
    public final int HIDESTATE = 1;
    public final int SHOOTSTATE = 2;

    public GameController() {
        player1 = new Player("");
        player2 = new Player("");
        currentActivePlayer = "player1";
        currentShipIndex = 0;
        gameState = HIDESTATE;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentActivePlayer() {
        if (currentActivePlayer.equals("player1")) {
            return player1;
        } else {
            return player2;
        }
    }

    public int getGameState() {
        return gameState;
    }

    public void handleClick(int x, int y) {
        System.out.println(gameState);
        if (gameState == HIDESTATE) {
            handleHide(x, y);
        }
        if (gameState == SHOOTSTATE) {
            handleShot(x, y);
        }
    }

    public void handleHide(int x, int y) {
        if (player1.getShipsPlaced() == true && player2.getShipsPlaced() == true) {
            gameState = SHOOTSTATE;
        } else {
            boolean allowHide = true;
            String[][] waterSpots = getCurrentActivePlayer().getWaterSpots();
            String shipRotation = getCurrentActivePlayer().getEquippedShip().getRotation();
            int shipLength = getCurrentActivePlayer().getEquippedShip().getLength();
            Ship currentShip = getCurrentActivePlayer().getEquippedShip();
            switch (shipRotation) {
                case "vertical":
                    for (int i = 0; i < shipLength; i++) {
                        if (y+i > 15 || waterSpots[y+i][x] == "hidden") {
                            allowHide = false;
                            handleHideFalse();
                            break;
                        }
                    }
                    if (allowHide) {
                        for (int i = 0; i < shipLength; i++) {
                            if (y+i < 15) {
                                waterSpots[y+i][x] = "hidden";
                            }
                        }
                    } break;

                case "horizontal":
                    for (int i = 0; i < shipLength; i++) {
                        if (x+i > 15 || waterSpots[y][x+i] == "hidden") {
                            allowHide = false;
                            handleHideFalse();
                            break;
                        }
                    }
                    if (allowHide) {
                        for (int i = 0; i < shipLength; i++) {
                            if (x+i < 15) {
                                waterSpots[y][x+i] = "hidden";
                            }
                        }
                    } break;
            }
            if (allowHide == true) {
                currentShip.setPlaced(true);
            }
            if (allShipsPlaced() == true) {
                getCurrentActivePlayer().setShipsPlaced(true);
                currentActivePlayer = currentActivePlayer.equals("player1") ? "player2" : "player1";
                currentShipIndex = 0;
            } else if (currentShip.getPlaced()) {
                currentShipIndex += 1;
                getCurrentActivePlayer().changeEquippedShip(currentShipIndex);
            }
        }
    }
    
    public void handleHideFalse() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("You can't place there!");

        alert.showAndWait();
    }

    public boolean allShipsPlaced() {
        boolean allPlaced = true;
        Ship[] ships = getCurrentActivePlayer().getShips();
        for (int i = 0; i < ships.length; i++) {
            if (ships[i].getPlaced() == false) {
                allPlaced = false;
            }
        }
        return allPlaced;
    }

    public void handleShot(int x, int y) {
        Player enemy = getCurrentActivePlayer().equals(player1) ? player2 : player1;
        String[][] waterSpots = enemy.getWaterSpots();
        String shotStatus = waterSpots[y][x];
        boolean changePlayer = false;
        switch (shotStatus) {
            case "empty":
                handleShotMissed(x, y); changePlayer = true; break;
            case "hidden":
                handleShotHit(x, y); changePlayer = true; break;
            case "hit":
                handleShotFalse(); break;
            case "miss":
                handleShotFalse(); break;
        }
        if (changePlayer == true) {
            currentActivePlayer = currentActivePlayer.equals("player1") ? "player2" : "player1";
        }
    }

    public void handleShotFalse() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("You can't shoot there!");

        alert.showAndWait();
    }

    public void handleShotMissed(int x, int y) {
        Player enemy = getCurrentActivePlayer().equals(player1) ? player2 : player1;
        String[][] waterSpots = enemy.getWaterSpots();
        System.out.println("You missed at: " + x + " " + y);
        waterSpots[y][x] = "miss";
    }

    public void handleShotHit(int x, int y) {
        Player enemy = getCurrentActivePlayer().equals(player1) ? player2 : player1;
        String[][] waterSpots = enemy.getWaterSpots();
        System.out.println("You hit at: " + x + " " + y);
        waterSpots[y][x] = "hit";
    }
}
