import java.util.*;

public class MazeDFS {
    static final char WALL = '█';
    static final char PATH = ' ';
    private final int width, height;
    private final boolean[][] visited;
    private final char[][] maze;
    private final Random rand = new Random();

    public MazeDFS(int w, int h) {
        width = w;
        height = h;
        visited = new boolean[width][height];
        maze = new char[2*width +1][2*height +1];
        for (char[] row : maze) Arrays.fill(row, WALL);
    }

    public void generate(int cx, int cy) {
        visited[cx][cy] = true;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        Collections.shuffle(Arrays.asList(dirs), rand);

        for (int[] d : dirs) {
            int nx = cx + d[0], ny = cy + d[1];
            if (0 <= nx && nx < width && 0 <= ny && ny < height && !visited[nx][ny]) {
                maze[cx*2 +1 + d[0]][cy*2+1 + d[1]] = PATH;
                maze[nx*2 + 1][ny*2+1] = PATH;
                generate(nx, ny);
            }
        }
    }

    public void carveAndPrint() {
        maze[1][1] = PATH;
        generate(0, 0);
        maze[0][1] = PATH;
        maze[2*width][2*height-1] = PATH;

        for (char[] row : maze) {
            System.out.println(new String(row));
        }
    }

    public static void main(String[] args) {
        int w = 5, h = 10;
        new MazeDFS(w, h).carveAndPrint();
    }
}
