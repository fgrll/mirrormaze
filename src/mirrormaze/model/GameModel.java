package mirrormaze.model;

public class GameModel {
    private final boolean[][] walls;
    private final int cols, rows;
    private int playerX, playerY;
    private int exitX, exitY;

    public GameModel(int cols, int rows, boolean[][] walls) {
        this.walls = walls;
        this.cols = 2*cols+1;
        this.rows = 2*rows+1;

        this.playerX = 0;
        this.playerY = 1;

        this.exitX = 2*cols;
        this.exitY = 2*rows-1;
    }

    public boolean isWall(int x, int y) {
        return walls[x][y];
    }

    public int getCols() { return cols; }
    public int getRows() { return rows; }

    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }

    public boolean tryMove(Direction dir) {
        int nx = playerX + dir.dx;
        int ny = playerY + dir.dy;

        if (nx < 0 || nx >= cols || ny < 0 || ny >= rows) {
            return false;
        }

        if (walls[nx][ny]) {
            return false;
        }

        playerX = nx; 
        playerY = ny;
        return true;
    }

    public void resetPosition() {
        playerX = 0;
        playerY = 1;
    }

    public boolean isFinished() {
        return playerX == exitX && playerY == exitY;
    }
}
