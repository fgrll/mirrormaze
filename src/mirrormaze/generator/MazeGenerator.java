package mirrormaze.generator;

public interface MazeGenerator {

    boolean[][] generate(int cols, int rows);
}