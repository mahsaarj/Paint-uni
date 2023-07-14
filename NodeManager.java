package Package1;

import javax.swing.*;
import java.awt.*;

public class NodeManager {
    private JPanel[][] nodes;

    public NodeManager() {
        nodes = new JPanel[25][25];
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                JPanel node = new JPanel();
                node.setBackground(Color.LIGHT_GRAY);
                node.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                nodes[i][j] = node;
            }
        }
    }

    public int getNodeSize(JPanel gridPanel) {
        Dimension size = gridPanel.getSize();
        int numRows = nodes.length;
        int numCols = nodes[0].length;
        int nodeSize = Math.min(size.width / numCols, size.height / numRows);
        return nodeSize;
    }

    public JPanel getNode(int row, int col) {
        return nodes[row][col];
    }
}