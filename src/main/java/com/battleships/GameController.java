package com.battleships;

public class GameController {
    
    private Player player1;
    private Player player2;
    private Player currentActivePlayer;

    public GameController() {
        player1 = new Player("");
        player2 = new Player("");
        currentActivePlayer = player1;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentActivePlayer() {
        return currentActivePlayer;
    }

    public void handleShot(int x, int y) {
        String[][] waterSpots = currentActivePlayer.getWaterSpotsStatus();
        String shotStatus = waterSpots[y][x];
        switch (shotStatus) {
            case "empty":
                handleShotMissed(x, y); break;
            case "hidden":
                handleShotHit(x, y); break;
        }
        currentActivePlayer = currentActivePlayer.equals(player1) ? player2 : player1;
    }

    public void handleShotMissed(int x, int y) {
        String[][] waterSpots = currentActivePlayer.getWaterSpotsStatus();
        System.out.println("You missed at: " + x + " " + y);
        waterSpots[y][x] = "miss";
    }

    public void handleShotHit(int x, int y) {
        String[][] waterSpots = currentActivePlayer.getWaterSpotsStatus();
        System.out.println("You hit a ship at: " + x + " " + y);
        waterSpots[y][x] = "hit";
    }
}
