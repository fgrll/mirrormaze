package controls_demo;

import javax.swing.*;

import MazeGenerator.DFSMazeGenerator;
import MazeGenerator.MazeGenerator;

public class GridDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MazeGenerator gen = new DFSMazeGenerator();
            GameModel model = new GameModel(10, 8, gen);
            GridPanel panel = new GridPanel(model);

            JFrame frame = new JFrame("Maze Demo (no collision)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack(); 
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
