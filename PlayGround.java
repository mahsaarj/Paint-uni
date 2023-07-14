package Package1;

import javax.swing.*;
import java.awt.*;

class Playground extends JFrame {
    private NodeManager nodeManager;
    private JPanel gridPanel;

    public Playground() {
        super("Playground");

        // Create node manager
        nodeManager = new NodeManager();

        // Create grid panel
        gridPanel = new JPanel(new GridLayout(25, 25));
        gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Add nodes to grid panel
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                JPanel node = nodeManager.getNode(i, j);
                gridPanel.add(node);
            }
        }

        int centerRow = 12;
        int centerCol = 12;
        RectangleShape rect = new RectangleShape(Color.RED, 50);
        nodeManager.getNode(centerRow, centerCol).add(rect);

        // Add grid panel to content pane
        setContentPane(gridPanel);

        // Set frame properties
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
