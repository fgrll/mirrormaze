package controls_demo;

import MazeGenerator.MazeGenerator;

public class GameModel {
    private final boolean[][] walls;
    private final int cols, rows;
    private int playerX, playerY;

    public GameModel(int cols, int rows, MazeGenerator gen) {
        this.walls = gen.generate(cols, rows);
        this.cols = walls.length;
        this.rows = walls[0].length;

        this.playerX = 0;
        this.playerY = 1;
    }

    public boolean isWall(int x, int y) {
        return walls[x][y];
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
