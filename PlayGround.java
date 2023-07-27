package Package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Playground extends JFrame {
    private NodeManager nodeManager;
    private JPanel gridPanel;
    private int currentRow;
    private int currentCol;
    private boolean dragging;

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

        currentRow = 1;
        currentCol = 1;

        RectangleShape rect = new RectangleShape(Color.RED, 100);
        nodeManager.getNode(currentRow, currentCol).add(rect);
        updateNodeColor(nodeManager.getNode(currentRow, currentCol), Color.PINK);

        // Add grid panel to content pane
        setContentPane(gridPanel);
        // Set frame properties
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Add mouse and keyboard listeners

        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gridPanel.requestFocusInWindow();
                int x = e.getX();
                int y = e.getY();
                int nodeWidth = gridPanel.getWidth() / 25;
                int nodeHeight = gridPanel.getHeight() / 25;
                int row = y / nodeHeight;
                int col = x / nodeWidth;

                if (row != currentRow || col != currentCol) {
                    JPanel currentNode = nodeManager.getNode(currentRow, currentCol);
                    JPanel newNode = nodeManager.getNode(row, col);
                    // Move rectangle to new node
                    RectangleShape rect = getRectangle(currentNode);
                    currentNode.remove(rect);
                    newNode.add(rect);
                    // Update current row and column
                    currentRow = row;
                    currentCol = col;
                    // Update color of new node
                    updateNodeColor(newNode, Color.RED);
                    // Repaint nodes
                    currentNode.repaint();
                    newNode.repaint();
                }

                dragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
                // Color the nodes inside the border shape with the fill color
                colorNodesInsideBorder(nodeManager.getNode(currentRow, currentCol), Color.BLUE);
                nodeManager.printNodeStates();
            }
        });

        gridPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    int x = e.getX();
                    int y = e.getY();
                    int nodeWidth = gridPanel.getWidth() / 25;
                    int nodeHeight = gridPanel.getHeight() / 25;
                    int row = y / nodeHeight;
                    int col = x / nodeWidth;
                    if (row != currentRow || col != currentCol) {
                        JPanel currentNode = nodeManager.getNode(currentRow, currentCol);
                        JPanel newNode = nodeManager.getNode(row, col);
                        // Move rectangle to new node
                        RectangleShape rect = getRectangle(currentNode);
                        currentNode.remove(rect);
                        newNode.add(rect);
                        // Update current row and column
                        currentRow = row;
                        currentCol = col;
                        // Update color of new node
                        updateNodeColor(newNode, Color.RED);
                        // Repaint nodes
                        currentNode.repaint();
                        newNode.repaint();
                    }

                }

            }

        });

        gridPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        moveNode(-1, 0);
                        break;
                    case KeyEvent.VK_DOWN:
                        moveNode(1, 0);
                        break;
                    case KeyEvent.VK_LEFT:
                        moveNode(0, -1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveNode(0, 1);
                        break;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        nodeManager.printNodeStates();
        nodeManager.setNodeColored(currentRow, currentCol, true);
    }

    private void moveNode(int rowOffset, int colOffset) {
        int newRow = currentRow + rowOffset;
        int newCol = currentCol + colOffset;
        // Check if new row and column are within bounds
        if (newRow >= 0 && newRow < 25 && newCol >= 0 && newCol < 25) {
// Get current node and new node
            JPanel currentNode = nodeManager.getNode(currentRow, currentCol);
            JPanel newNode = nodeManager.getNode(newRow, newCol);
            // Move rectangle to new node
            RectangleShape rect = getRectangle(currentNode);
            currentNode.remove(rect);
            newNode.add(rect);
            // Update current row and column
            currentRow = newRow;
            currentCol = newCol;
            // Update color of new node
            updateNodeColor(newNode, Color.RED);
            // Repaint nodes
            currentNode.repaint();
            newNode.repaint();
        }
    }

    private RectangleShape getRectangle(JPanel node) {
        for (Component comp : node.getComponents()) {
            if (comp instanceof RectangleShape) {
                return (RectangleShape) comp;
            }
        }
        return null;
    }


    private void updateNodeColor(JPanel node, Color color) {
        boolean containsRectangle = false;
        for (Component component : node.getComponents()) {
            if (component instanceof RectangleShape) {
                containsRectangle = true;
                break;
            }
        }
        if (containsRectangle) {
            node.setBackground(Color.PINK);
        } else {
            node.setBackground(Color.lightGray);
        }
    }

    private void colorNodesInsideBorder(JPanel startNode, Color fillColor) {
        // Get the coordinate of the starting node
        int startX = nodeManager.getRow(startNode);
        int startY = nodeManager.getCol(startNode);

        // Initialize the inside array
        boolean[][] inside = new boolean[25][25];

        // Mark the nodes that are inside the pink shape or pink nodes themselves
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                JPanel node = nodeManager.getNode(i, j);
                boolean isPink = node.getBackground().equals(Color.PINK);
                boolean isInside = i >= startX && i < startX + 2 && j >= startY && j < startY + 2;
                if (isPink || isInside) {
                    inside[i][j] = true;
                }
            }
        }

        // Perform DFS to color all nodes inside the pink border
        dfs(startX, startY, inside, fillColor);
    }

    private void dfs(int row, int col, boolean[][] inside, Color fillColor) {
        // Check if the current node is inside the pink border and not already colored
        if (inside[row][col] && !nodeManager.isNodeColored(row, col)) {
            // Color the current node with the fill color
            JPanel node = nodeManager.getNode(row, col);
            nodeManager.setNodeColor(node, fillColor);
            nodeManager.setNodeColored(row, col, true);

            // Recursively color all adjacent nodes
            if (row > 0) {
                dfs(row - 1, col, inside, fillColor); // up
            }
            if (row < 24) {
                dfs(row + 1, col, inside, fillColor); // down
            }
            if (col > 0) {
                dfs(row, col - 1, inside, fillColor); // left
            }
            if (col < 24) {
                dfs(row, col + 1, inside, fillColor); // right
            }
        }
    }
}