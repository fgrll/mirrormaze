package mirrormaze.generator;

import java.util.Arrays;

public class MirrorMazeGenerator implements MazeGenerator {
    private final MazeGenerator halfGen;

    public MirrorMazeGenerator(MazeGenerator halfGen) {
        this.halfGen = halfGen;
    }

    @Override
    public boolean[][] generate(int halfCols, int rows) {
        
        boolean[][] half = halfGen.generate(halfCols, rows);
        int W1 = half.length;
        int H  = half[0].length;

        int W = W1 + (W1 - 1);
        boolean[][] full = new boolean[W][H];

        for (int x = 0; x < W; x++) {
            Arrays.fill(full[x], true);
        }

        for (int x = 0; x < W1; x++) {
            System.arraycopy(half[x], 0, full[x], 0, H);
        }

        for (int x = 1; x < W1; x++) {
            int tx = W1 - 1 + x;
            System.arraycopy(half[W1-1 - x], 0, full[tx], 0, H);
        }
        return full;
    }
}
