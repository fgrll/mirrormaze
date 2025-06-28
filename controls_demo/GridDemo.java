package controls_demo;

import javax.swing.*;

public class GridDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameModel model = new GameModel(10, 10);
            GridPanel panel = new GridPanel(model);

            JFrame frame = new JFrame("Grid Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack(); 
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
