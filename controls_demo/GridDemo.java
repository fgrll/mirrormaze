package controls_demo;

import javax.swing.*;

import MazeGenerator.DFSMazeGenerator;
import MazeGenerator.MazeGenerator;

public class GridDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MazeGenerator gen = new DFSMazeGenerator();
            boolean[][] walls = gen.generate(10, 8);
            GameModel model = new GameModel(walls.length, walls[0].length, walls);
            GridPanel panel = new GridPanel(model);

            JFrame frame = new JFrame("Maze Demo (with collision)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack(); 
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
