package controls_demo;

public class GameModel {
    private final int cols, rows;
    private int playerX, playerY;

    public GameModel(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        // debug 
        this.playerX = cols / 2;
        this.playerY = rows / 2;
    }

    public int getCols() { return cols; }
    public int getRows() { return rows; }

    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }

    public void moveNorth() { if (playerY > 0) playerY--; }
    public void moveSouth() { if (playerY < rows - 1) playerY ++; }
    public void moveEast() { if (playerX < cols - 1) playerX++; }
    public void moveWest() { if (playerX > 0) playerX--; }
}
