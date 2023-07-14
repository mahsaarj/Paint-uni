package Package1;

import javax.swing.*;
import java.awt.*;

public class NodeManager {
    private JPanel[][] nodes;
    private boolean[][] coloredNodes;
    private int topRow = 0;
    private int bottomRow = 24;

    public NodeManager() {
        nodes = new JPanel[25][25];
        coloredNodes = new boolean[25][25];
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                nodes[i][j] = new JPanel();
                nodes[i][j].setBackground(Color.lightGray);
                nodes[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                coloredNodes[i][j] = false;
            }
        }
    }

    public JPanel getNode(int row, int col) {
        if (row < topRow) {
            // Add new row at the top
            for (int i = topRow; i > row; i--) {
                for (int j = 0; j < 25; j++) {
                    nodes[i][j] = nodes[i-1][j];
                }
            }
            for (int j = 0; j < 25; j++) {
                nodes[row][j] = new JPanel();
                nodes[row][j].setBackground(Color.lightGray);
                nodes[row][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                coloredNodes[row][j] = false;
            }
            topRow = row;
        } else if (row > bottomRow) {
            // Add new row at the bottom
            for (int i = bottomRow; i < row; i++) {
                for (int j = 0; j < 25; j++) {
                    nodes[i][j] = nodes[i+1][j];
                }
            }
            for (int j = 0; j < 25; j++) {
                nodes[row][j] = new JPanel();
                nodes[row][j].setBackground(Color.lightGray);
                nodes[row][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                coloredNodes[row][j] = false;
            }
            bottomRow = row;
        }

        return nodes[row][col];
    }

    public boolean isNodeColored(int row, int col) {
        return coloredNodes[row][col];
    }

    public void setNodeColored(int row, int col, boolean colored) {
        coloredNodes[row][col] = colored;
    }

    public int getTopRow() {
        return topRow;
    }

    public void setTopRow(int topRow) {
        this.topRow = topRow;
    }

    public int getBottomRow() {
        return bottomRow;
    }

    public void setBottomRow(int bottomRow) {
        this.bottomRow = bottomRow;
    }
}