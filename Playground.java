package Package1;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

public class PlayGround {
    public PlayGround() {
        JPanel panel = createPanel();
        createFrame(panel);
    }

    public JPanel createPanel() {
        // Create a new JPanel object
        JPanel panel = new JPanel();

        // Set the background color of the panel to white
        panel.setBackground(Color.WHITE);

        // Create a new NodeManager object and add the grid panel to the panel
        NodeManager nodeManager = new NodeManager();
        JPanel gridPanel = nodeManager.getGridPanel();
        panel.add(gridPanel);

        // Save the grid panel information in an ArrayList in the NodeManager object
        ArrayList<ArrayList<Boolean>> nodeColors = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            ArrayList<Boolean> rowColors = new ArrayList<>();
            for (int j = 0; j < 25; j++) {
                rowColors.add(nodeManager.getNodeColor(i, j));
            }
            nodeColors.add(rowColors);
        }
        nodeManager.setNodeColors(nodeColors);

        return panel;
    }

    public void createFrame(JPanel panel) {
        // Create a new JFrame object and add the panel to it
        JFrame frame = new JFrame("Empty Pane");
        frame.getContentPane().add(panel);

        // Set the properties of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // Make the frame visible
        frame.setVisible(true);
    }
}