import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MazeSwingDemo {
    static class MazeDFS {
        static final char WALL = '█';
        static final char PATH = ' ';
        private final int width, height;
        private final boolean[][] visited;
        private final char[][] maze;
        private final Random rand = new Random();

        public MazeDFS(int w, int h) {
            width  = w;
            height = h;
            visited = new boolean[width][height];
            maze = new char[2*width +1][2*height +1];
            for (char[] row : maze) Arrays.fill(row, WALL);
        }

        private void generate(int cx, int cy) {
            visited[cx][cy] = true;
            int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
            Collections.shuffle(Arrays.asList(dirs), rand);

            for (int[] d : dirs) {
                int nx = cx + d[0], ny = cy + d[1];
                if (0 <= nx && nx < width &&
                    0 <= ny && ny < height &&
                    !visited[nx][ny]) {

                    maze[cx*2 +1 + d[0]][cy*2+1 + d[1]] = PATH;
                    maze[nx*2 +1][ny*2+1] = PATH;
                    generate(nx, ny);
                }
            }
        }

        public void carve() {
            maze[1][1] = PATH;
            generate(0, 0);
            maze[0][1] = PATH;
            maze[2*width][2*height-1] = PATH;
        }

        public char[][] getMaze() {
            return maze;
        }
    }

    static class MazePanel extends JPanel {
        private final char[][] maze;
        private final int cellSize;

        public MazePanel(char[][] maze, int cellSize) {
            this.maze = maze;
            this.cellSize = cellSize;
            int w = maze.length * cellSize;
            int h = maze[0].length * cellSize;
            setPreferredSize(new Dimension(w, h));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int x = 0; x < maze.length; x++) {
                for (int y = 0; y < maze[0].length; y++) {
                    if (maze[x][y] == MazeDFS.WALL) {
                        g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int w = 10, h = 10;
        int cellSize = 20;

        MazeDFS generator = new MazeDFS(w, h);
        generator.carve();
        char[][] maze = generator.getMaze();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mirror Maze Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new MazePanel(maze, cellSize));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
