package controls_demo;

public class GameModel {
    private final boolean[][] walls;
    private final int cols, rows;
    private int playerX, playerY;

    public GameModel(int cols, int rows, boolean[][] walls) {
        this.walls = walls;
        this.cols = cols;
        this.rows = rows;

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
}
