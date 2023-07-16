package Package1;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class NodeManager {
    private boolean[][] nodeColors;
    private JPanel gridPanel;

    public NodeManager() {
// Initialize the node colors
        nodeColors = new boolean[25][25];
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                nodeColors[i][j] = false;
            }
        }

// Initialize the grid panel
        gridPanel = new JPanel(new GridLayout(25, 25));
        gridPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                JPanel node = new JPanel();
                node.setBackground(Color.WHITE);
                node.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                node.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        toggleNodeColor(node);
                    }
                });
                gridPanel.add(node);
            }
        }

// Add key listener to the grid panel
        gridPanel.setFocusable(true);
        gridPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    moveNodesLeft();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moveNodesRight();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    moveNodesUp();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    moveNodesDown();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    public JPanel getGridPanel() {
        return gridPanel;
    }

    public boolean getNodeColor(int row, int col) {
        return nodeColors[row][col];
    }

    public void setNodeColor(int row, int col, boolean color) {
        nodeColors[row][col] = color;
    }

    private void toggleNodeColor(JPanel node) {
        int index = gridPanel.getComponentZOrder(node);
        int row = index / 25;
        int col = index % 25;

        if (row == 12 && col == 12) { // Center node clicked
            toggleRectangleColor();
        } else {
            nodeColors[row][col] = !nodeColors[row][col];
            node.setBackground(nodeColors[row][col] ? Color.RED : Color.WHITE);
            node.removeMouseListener(node.getMouseListeners()[0]); // remove the MouseListener to prevent further clicks
            node.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // do nothing
                }
            });

            // Check if the node is adjacent to the red node
            if ((row == 12 && (col == 11 || col == 13)) || (col == 12 && (row == 11 || row == 13))) {
                JPanel centerNode = (JPanel) gridPanel.getComponent(12 * 25 + 12);
                Component[] components = centerNode.getComponents();
                JPanel rectanglePanel = (JPanel) components[0];
                rectanglePanel.setBackground(Color.RED);
            }
        }
    }


    private void toggleRectangleColor() {
        JPanel centerNode = (JPanel) gridPanel.getComponent(12 * 25 + 12);
        Component[] components = centerNode.getComponents();
        JPanel rectanglePanel = (JPanel) components[0];
        Color color = rectanglePanel.getBackground();
        rectanglePanel.setBackground(color == Color.RED ? Color.WHITE : Color.RED);
    }

    private ArrayList<JPanel> movedNodes = new ArrayList<>();

    private void moveNodesLeft() {
        boolean[][] newColors = new boolean[25][25];
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 24; j++) {
                newColors[i][j] = nodeColors[i][j + 1];
                JPanel node = (JPanel) gridPanel.getComponent(i * 25 + j);
                if (nodeColors[i][j + 1] && !movedNodes.contains(node)) {
                    movedNodes.add(node);
                }
            }
            newColors[i][24] = nodeColors[i][0];
            JPanel node = (JPanel) gridPanel.getComponent(i * 25 + 24);
            if (nodeColors[i][0] && !movedNodes.contains(node)) {
                movedNodes.add(node);
            }
        }
        nodeColors = newColors;
        colorMovedNodes();
    }

    private void moveNodesRight() {
        boolean[][] newColors = new boolean[25][25];
        for (int i = 0; i < 25; i++) {
            for (int j = 1; j < 25; j++) {
                newColors[i][j] = nodeColors[i][j - 1];
                JPanel node = (JPanel) gridPanel.getComponent(i * 25 + j);
                if (nodeColors[i][j - 1] && !movedNodes.contains(node)) {
                    movedNodes.add(node);
                }
            }
            newColors[i][0] = nodeColors[i][24];
            JPanel node = (JPanel) gridPanel.getComponent(i * 25);
            if (nodeColors[i][24] && !movedNodes.contains(node)) {
                movedNodes.add(node);
            }
        }
        nodeColors = newColors;
        colorMovedNodes();
    }

    private void moveNodesUp() {
        boolean[][] newColors = new boolean[25][25];
        for (int j = 0; j < 25; j++) {
            for (int i = 0; i < 24; i++) {
                newColors[i][j] = nodeColors[i + 1][j];
                JPanel node = (JPanel) gridPanel.getComponent(i * 25 + j);
                if (nodeColors[i + 1][j] && !movedNodes.contains(node)) {
                    movedNodes.add(node);
                }
            }
            newColors[24][j] = nodeColors[0][j];
            JPanel node = (JPanel) gridPanel.getComponent(24 * 25 + j);
            if (nodeColors[0][j] && !movedNodes.contains(node)) {
                movedNodes.add(node);
            }
        }
        nodeColors = newColors;
        colorMovedNodes();
    }

    private void moveNodesDown() {
        boolean[][] newColors = new boolean[25][25];
        for (int j = 0; j < 25; j++) {
            for (int i = 1; i < 25; i++) {
                newColors[i][j] = nodeColors[i - 1][j];
                JPanel node = (JPanel) gridPanel.getComponent(i * 25 + j);
                if (nodeColors[i - 1][j] && !movedNodes.contains(node)) {
                    movedNodes.add(node);
                }
            }
            newColors[0][j] = nodeColors[24][j];
            JPanel node = (JPanel) gridPanel.getComponent(j);
            if (nodeColors[24][j] && !movedNodes.contains(node)) {
                movedNodes.add(node);
            }
        }
        nodeColors = newColors;
        colorMovedNodes();
    }

    private void colorMovedNodes() {
        for (JPanel node : movedNodes) {
            node.setBackground(Color.BLUE);
        }
        movedNodes.clear();
    }

    public void setNodeColors(ArrayList<ArrayList<Boolean>> colors) {
// Update nodeColors array and the color of each node in the grid panel
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                boolean color = colors.get(i).get(j);
                setNodeColor(i, j, color);
                JPanel node = (JPanel) gridPanel.getComponent(i * 25 + j);
                node.setBackground(color ? Color.BLACK : Color.WHITE);
            }
        }
    }
}