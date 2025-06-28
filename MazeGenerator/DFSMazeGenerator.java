package MazeGenerator;

import java.util.*;

public class DFSMazeGenerator implements MazeGenerator {
    private static final char WALL = '#';
    private static final char PATH = ' ';

    @Override
    public boolean[][] generate(int cols, int rows) {
        char[][] maze = new char[2*cols+1][2*rows+1];
        
        for (char[] row : maze) Arrays.fill(row, WALL);
        boolean[][] visited = new boolean[cols][rows];
        Random rand = new Random();

        class DFS {
            void carve(int x, int y) {
                visited[x][y] = true;
                int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};
                Collections.shuffle(Arrays.asList(dirs), rand);
                for (int[] d : dirs) {
                    int nx = x + d[0], ny = y + d[1];
                    if (nx >= 0 && nx < cols && ny >= 0 && ny < rows && !visited[nx][ny]) {
                        maze[x*2+1 + d[0]][y*2+1 + d[1]] = PATH;
                        maze[nx*2+1][ny*2+1] = PATH;
                        carve(nx, ny);
                    }
                }
            }
        }

        maze[1][1] = PATH;
        new DFS().carve(0, 0);
        maze[0][1] = PATH;
        maze[2*cols][2*rows-1] = PATH;

        boolean[][] walls = new boolean[2*cols+1][2*rows+1];
        for (int x = 0; x < walls.length; x++) {
            for (int y = 0; y < walls[0].length; y++) {
                walls[x][y] = (maze[x][y] == WALL);
            }
        }
        return walls;
    }
}
