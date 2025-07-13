package mirrormaze.generator;

import java.util.*;

public class DFSMazeGenerator implements MazeGenerator {
    private final Random rand = new Random();

    @Override
    public boolean[][] generate(int cols, int rows) {
        int W = 2 * cols + 1;
        int H = 2 * rows + 1;
        boolean[][] maze = new boolean[W][H];
        for (int x = 0; x < W; x++) {
            Arrays.fill(maze[x], true);
        }

        boolean[][] visited = new boolean[cols][rows];
        dfs(0, 0, cols, rows, maze, visited);

        maze[0][1] = false;
        maze[W - 1][H - 2] = false;

        return maze;
    }

    private void dfs(int cx, int cy,
            int cols, int rows,
            boolean[][] maze,
            boolean[][] visited) {
        
        visited[cx][cy] = true;
        maze[2 * cx + 1][2 * cy + 1] = false;

        List<int[]> dirs = new ArrayList<>(Arrays.asList(
                new int[] { 1, 0 },
                new int[] { -1, 0 },
                new int[] { 0, 1 },
                new int[] { 0, -1 }));
        Collections.shuffle(dirs, rand);

        for (int[] d : dirs) {
            int nx = cx + d[0], ny = cy + d[1];
            if (nx >= 0 && nx < cols && ny >= 0 && ny < rows && !visited[nx][ny]) {
                maze[2 * cx + 1 + d[0]][2 * cy + 1 + d[1]] = false;
                dfs(nx, ny, cols, rows, maze, visited);
            }
        }
    }
}