package Package1;

import javax.swing.*;
import java.awt.*;

public class NodeManager {
    private JPanel[][] nodes;
    private boolean[][] coloredNodes;
    private int[] numNodesInColumn;

    public NodeManager() {
        nodes = new JPanel[25][25];
        coloredNodes = new boolean[25][25];
        numNodesInColumn = new int[25];
        for (int i = 0; i < 25; i++) {
            numNodesInColumn[i] = 0;
            for (int j = 0; j < 25; j++) {
                nodes[i][j] = new JPanel();
                nodes[i][j].setBackground(Color.lightGray);
                nodes[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                coloredNodes[i][j] = false;
            }
        }
    }

    public JPanel getNode(int row, int col) {
        return nodes[row][col];
    }

    public int getRow(JPanel node) {
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                if (nodes[i][j] == node) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getCol(JPanel node) {
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                if (nodes[i][j] == node) {
                    return j;
                }
            }
        }
        return -1;
    }

    public boolean isNodeColored(int row, int col) {

        return coloredNodes[row][col];
    }

    public void setNodeColored(int row, int col, boolean colored) {
        coloredNodes[row][col] = colored;
        if (colored) {
            numNodesInColumn[col]++;
        } else {
            numNodesInColumn[col]--;
        }
    }

    public int getNumNodesInRow(int col) {
        return numNodesInColumn[col];
    }
}