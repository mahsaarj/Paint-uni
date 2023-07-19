package Package1;
import javax.swing.*;
import java.awt.*;
public class NodeManager extends Component {
    private JPanel[][] nodes;
    private int row;
    private int col;
    private boolean[][] coloredNodes;
    private int[] numNodesInColumn;

    public NodeManager() {
        nodes = new JPanel[25][25];
        coloredNodes = new boolean[25][25];
        numNodesInColumn = new int[25];
        for (int i = 0; i < 25; i++) {
            numNodesInColumn[i] = 0;
            for (int j = 0; j <25; j++) {
                nodes[i][j] = new JPanel();
                nodes[i][j].setBackground(Color.lightGray);
                nodes[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                coloredNodes[i][j] = false;

                // Add label to display x and y position
                JLabel label = new JLabel(i + "," + j);
                nodes[i][j].add(label);
            }
        }
    }

    public NodeManager(int row, int col) {
        this.row = row;
        this.col = col;
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
            System.out.println("Node at position (" + row + ", " + col + ") has been colored.");
        } else {
            numNodesInColumn[col]--;
        }
    }


    public void setNodeColor(JPanel node, Color color) {
        node.setBackground(color);
    }

    public void printNodeStates() {
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                String colorState = isNodeColored(i, j) ? "colored" : "uncolored";
                System.out.println("Node at position (" + i + ", " + j + ") is " + colorState);
            }
        }
    }

}