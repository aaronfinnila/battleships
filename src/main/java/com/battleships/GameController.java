package com.battleships;

import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

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

    public void switchCurrentActivePlayer() {
        currentActivePlayer = currentActivePlayer.equals("player1") ? "player2" : "player1";
    }

    public int getGameState() {
        return gameState;
    }

    public void handleClick(int x, int y) {
        if (gameState == HIDESTATE) {
            handleHide(x, y);
        } else {
            handleShot(x, y);
        }
    }

    public void handleHide(int x, int y) {
        if (getCurrentActivePlayer().getHideMine() == true) {
            String[][] waterSpots = getCurrentActivePlayer().getWaterSpots();
            boolean minePlaced = false;
            switch (waterSpots[y][x]) {
                case "hidden":
                    falseMoveAlert("You can't place there!"); break;
                case "empty":
                    waterSpots[y][x] = "mine"; minePlaced = true; break;
                case "hit":
                    falseMoveAlert("You can't place there!"); break;
                case "miss":
                    falseMoveAlert("You can't place there!"); break;
                case "mine":
                    falseMoveAlert("You can't place there!"); break;
            }
            if (minePlaced == true) {
                gameState = SHOOTSTATE;
            }
        } else {
            boolean allowHide = true;
            String[][] waterSpots = getCurrentActivePlayer().getWaterSpots();
            String shipRotation = getCurrentActivePlayer().getEquippedShip().getRotation();
            int shipLength = getCurrentActivePlayer().getEquippedShip().getLength();
            Ship currentShip = getCurrentActivePlayer().getEquippedShip();
            switch (shipRotation) {
                case "vertical":
                    for (int i = 0; i < shipLength; i++) {
                        if (y+i >= 15 || waterSpots[y+i][x] == "hidden") {
                            allowHide = false;
                            falseMoveAlert("You can't place there!");
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
                        if (x+i >= 15 || waterSpots[y][x+i] == "hidden") {
                            allowHide = false;
                            falseMoveAlert("You can't place there!");
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
                currentShip.setPositionX(x);
                currentShip.setPositionY(y);
            }
            if (activeShipsPlaced() == true) {
                getCurrentActivePlayer().setShipsPlaced(true);
                switchCurrentActivePlayer();
                currentShipIndex = 0;
            } else if (currentShip.isPlaced()) {
                currentShipIndex += 1;
                getCurrentActivePlayer().changeEquippedShip(currentShipIndex);
            }
            if (player1.getShipsPlaced() == true && player2.getShipsPlaced() == true) {
            gameState = SHOOTSTATE;
            }
        }
    }
    
    public void falseMoveAlert(String text) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.show();
    }

    public boolean activeShipsPlaced() {
        boolean allPlaced = true;
        Ship[] ships = getCurrentActivePlayer().getShips();
        for (int i = 0; i < ships.length; i++) {
            if (ships[i].isPlaced() == false) {
                allPlaced = false;
            }
        }
        return allPlaced;
    }

    public void handleShot(int x, int y) {
        if (getCurrentActivePlayer().getShootMortar() == true) {
            handleShootMortar(x, y);
        } else {
            Player enemy = getCurrentEnemy();
            String[][] waterSpots = enemy.getWaterSpots();
            String shotStatus = waterSpots[y][x];
            boolean changePlayer = false;
            switch (shotStatus) {
                case "empty":
                    handleShotMissed(x, y); changePlayer = true; break;
                case "hidden":
                    handleShotHit(x, y); changePlayer = true; break;
                case "hit":
                    falseMoveAlert("You can't shoot there!"); break;
                case "miss":
                    falseMoveAlert("You can't shoot there!"); break;
                case "mine":
                    handleShotMine(x, y); changePlayer = true; break;
            }
            if (changePlayer == true) {
                switchCurrentActivePlayer();
            }
        }
    }

    public void handleShotMissed(int x, int y) {
        nextTurnAlert("You missed!");
        Player enemy = getCurrentEnemy();
        String[][] waterSpots = enemy.getWaterSpots();
        waterSpots[y][x] = "miss";
    }

    public void handleShotMine(int x, int y) {
        nextTurnAlert("You shot a mine!");
        Random random = new Random();
        String[][] waterSpots = getCurrentActivePlayer().getWaterSpots();
        String[][] enemyWaterSpots = getCurrentEnemy().getWaterSpots();
        enemyWaterSpots[y][x] = "miss";
        for (int i = 0; i < 3; i++) {
            int row = random.nextInt(0, 15);
            int col = random.nextInt(0, 15);
            switch (waterSpots[row][col]) {
                case "hidden":
                    waterSpots[row][col] = "hit";
                    getCurrentActivePlayer().checkDestroyedShips();
                    getCurrentActivePlayer().addMana(1);
                    break;
                case "empty":
                    waterSpots[row][col] = "miss";
                    break;
                case "mine":
                    System.out.println("shots from a mine hit a mine!");
                    switchCurrentActivePlayer();
                    handleShotMine(col, row);
                    switchCurrentActivePlayer();
                    break;
            }
        }
    }

    public void handleShotHit(int x, int y) {
        nextTurnAlert("You hit a ship!");
        Player enemy = getCurrentEnemy();
        String[][] waterSpots = enemy.getWaterSpots();
        waterSpots[y][x] = "hit";
        enemy.checkDestroyedShips();
        enemy.addMana(1);
    }

    public Player getCurrentEnemy() {
        Player enemy = getCurrentActivePlayer().equals(player1) ? player2 : player1;
        return enemy;
    }

    public void nextTurnAlert(String text) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(text);
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("Next turn");
        alert.showAndWait();
    }

    public void handlePlaceMine() {
        getCurrentActivePlayer().setHideMine(true);
        getCurrentActivePlayer().subtractMana(2);
        gameState = HIDESTATE;
    }

    public void handleRadar() {
        getCurrentActivePlayer().subtractMana(3);
    }

    public void handlePlaceMortar() {
        getCurrentActivePlayer().setShootMortar(true);
        getCurrentActivePlayer().subtractMana(5);
    }

    public void handleShootMortar(int x, int y) {
        String[][] enemyWaterSpots = getCurrentEnemy().getWaterSpots();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                switch (enemyWaterSpots[y+i][x+j]) {
                    case "hidden":
                        enemyWaterSpots[y+i][x+j] = "hit";
                        getCurrentActivePlayer().checkDestroyedShips();
                        getCurrentActivePlayer().addMana(1);
                        break;
                    case "empty":
                        enemyWaterSpots[y+i][x+j] = "miss";
                        break;
                    case "mine":
                        switchCurrentActivePlayer();
                        handleShotMine(y+i, x+j);
                        switchCurrentActivePlayer();
                        break;
                    }
            }
        }
    }
}
