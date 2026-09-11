package com.battleships;

import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class GameController {
    
    private Player player1;
    private Player player2;
    private Player currentActivePlayer;
    private int currentShipIndex;
    private int gameState;
    public final int HIDESTATE = 1;
    public final int SHOOTSTATE = 2;

    public GameController() {
        player1 = new Player("");
        player2 = new Player("");
        currentActivePlayer = player1;
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
        if (currentActivePlayer == player1) {
            return player1;
        } else {
            return player2;
        }
    }

    public Player getCurrentInactivePlayer() {
        if (currentActivePlayer == player1) {
            return player2;
        } else {
            return player1;
        }
    }

    public void switchCurrentActivePlayer() {
        currentActivePlayer = currentActivePlayer.equals(player1) ? player2 : player1;
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
            handleHideMine(x, y);
        } else {
            handleHideShip(x, y);
        }
    }

    public void handleHideMine(int x, int y) {
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
    }

    public void handleHideShip(int x, int y) {
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

    public Player getOtherPlayer(Player player) {
        return player.equals(player1) ? player2 : player1;
    }

    public void handleShot(int x, int y) {
        if (getCurrentActivePlayer().getShootMortar() == true) {
            handleShootMortar(x, y);
        } else if (getCurrentActivePlayer().getPlaceRadar() == true) {
            handlePlaceRadar(x, y);
        } else {
            handleShootRegular(x, y);
        }
    }

    public void handleShotMissed(int x, int y) {
        Player enemy = getCurrentInactivePlayer();
        String[][] waterSpots = enemy.getWaterSpots();
        waterSpots[y][x] = "miss";
        getCurrentActivePlayer().setShotUsed(true);
    }

    public void handleShotMine(Player target, int x, int y) {
        continueAlert("You shot a mine!");
        Random random = new Random();
        String[][] waterSpots = target.getWaterSpots();
        String[][] enemyWaterSpots = getOtherPlayer(target).getWaterSpots();
        enemyWaterSpots[y][x] = "miss";
        for (int i = 0; i < 3; i++) {
            int row = random.nextInt(0, 15);
            int col = random.nextInt(0, 15);
            switch (waterSpots[row][col]) {
                case "hidden":
                    waterSpots[row][col] = "hit";
                    target.checkDestroyedShips();
                    target.addMana(1);
                    break;
                case "empty":
                    waterSpots[row][col] = "miss";
                    break;
                case "mine":
                    System.out.println("shots from a mine hit a mine!");
                    handleShotMine(getOtherPlayer(target), col, row);
                    break;
            }
        }
        getCurrentActivePlayer().setShotUsed(true);
    }

    public void handleShotHit(int x, int y) {
        Player enemy = getCurrentInactivePlayer();
        String[][] waterSpots = enemy.getWaterSpots();
        waterSpots[y][x] = "hit";
        enemy.checkDestroyedShips();
        enemy.addMana(1);
    }

    public void continueAlert(String text) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(text);
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("Continue");
        alert.showAndWait();
    }

    public void handlePlaceMine() {
        getCurrentActivePlayer().setHideMine(true);
        getCurrentActivePlayer().subtractMana(2);
        gameState = HIDESTATE;
    }

    public void handlePlaceRadar() {
        getCurrentActivePlayer().setPlaceRadar(true);
        getCurrentActivePlayer().subtractMana(3);
    }

    public void handlePlaceMortar() {
        getCurrentActivePlayer().setShootMortar(true);
        getCurrentActivePlayer().subtractMana(5);
    }

    public void handleShootMortar(int x, int y) {
        String[][] enemyWaterSpots = getCurrentInactivePlayer().getWaterSpots();
        if (x > 0 && y > 0 && x < 14 && y < 14) {
            int realX = x-1;
            int realY = y-1;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    switch (enemyWaterSpots[realY+i][realX+j]) {
                        case "hidden":
                            enemyWaterSpots[realY+i][realX+j] = "hit";
                            getCurrentInactivePlayer().checkDestroyedShips();
                            getCurrentInactivePlayer().addMana(1);
                            break;
                        case "empty":
                            enemyWaterSpots[realY+i][realX+j] = "miss";
                            break;
                        case "mine":
                            handleShotMine(getCurrentActivePlayer(), realY+i, realX+j);
                            break;
                        }
                    }
                }
                getCurrentActivePlayer().setShootMortar(false);
        }
    }

    public void handlePlaceRadar(int x, int y) {
        String[][] enemyWaterSpots = getCurrentInactivePlayer().getWaterSpots();
        String shotStatus = enemyWaterSpots[y][x];
        if (shotStatus.equals("miss")) {
            int shootablesFound = 0;
            if (x > 0 && y > 0 && x < 14 && y < 14) {
                int realX = x-1;
                int realY = y-1;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        String spot = enemyWaterSpots[realY+i][realX+j];
                        if (spot == "hidden" || spot == "mine") {
                            shootablesFound++;
                        }
                    }
                }
            }
            System.out.println(shootablesFound);
            getCurrentInactivePlayer().setRadarMapPoint(x, y, shootablesFound);
            getCurrentActivePlayer().setPlaceRadar(false);
            enemyWaterSpots[y][x] = "radar";
        } else if (getCurrentActivePlayer().getPlaceRadar() == true) {
            falseMoveAlert("You can't place a radar there!");
            System.out.println(shotStatus);
        }
    }

    public void handleShootRegular(int x, int y) {
        if (getCurrentActivePlayer().getShotUsed() == true) {
            falseMoveAlert("You have already shot this turn!");
        } else {
            Player enemy = getCurrentInactivePlayer();
            String[][] waterSpots = enemy.getWaterSpots();
            String shotStatus = waterSpots[y][x];
            switch (shotStatus) {
                case "empty":
                    handleShotMissed(x, y); break;
                case "hidden":
                    handleShotHit(x, y); break;
                case "hit":
                    falseMoveAlert("You can't shoot there!"); break;
                case "miss":
                    falseMoveAlert("You can't shoot there!"); break;
                case "radar":
                    falseMoveAlert("You can't shoot there!"); break;
                case "mine":
                    handleShotMine(getCurrentActivePlayer(), x, y); break;
            }
        }
    }
}
