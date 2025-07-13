package mirrormaze.generator;

public class MirrorMazeGenerator implements MazeGenerator {
    private final MazeGenerator halfDimension;

    public MirrorMazeGenerator(MazeGenerator halfDimension) {
        this.halfDimension = halfDimension;
    }

    @Override
    public boolean[][] generate(int cols, int rows) {
        boolean[][] half = halfDimension.generate(cols, rows);
        int H = rows;
        int W = cols * 2;
        boolean[][] full = new boolean[W][H];

        for (int x = 0; x < cols; x++) {
            System.arraycopy(half[x], 0, full[x], 0, H);
        }

        for (int x = 0; x < cols; x++) {
            int mx = W - 1 - x;
            for (int y = 0; y < H; y++) {
                full[mx][y] = half[x][y];
            }
        }

        return full;
    }
}
