package mirrormaze.model;

public class GameModel {
    private final boolean[][] walls;
    private int playerX, playerY;
    private int exitX, exitY;

    public GameModel(boolean[][] walls) {
        this.walls = walls;

        this.playerX = 0;
        this.playerY = 1;

        this.exitX = walls.length - 1;
        this.exitY = 1;
    }

    public boolean isWall(int x, int y) {
        return walls[x][y];
    }

    public int getCols() { return walls.length; }
    public int getRows() { return walls[0].length; }

    public int getPlayerX() { return playerX; }
    public int getPlayerY() { return playerY; }

    public boolean tryMove(Direction dir) {
        int nx = playerX + dir.dx;
        int ny = playerY + dir.dy;

        if (nx < 0 || nx >= walls.length || ny < 0 || ny >= walls[0].length) {
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
