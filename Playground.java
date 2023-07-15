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

        currentRow = 12;
        currentCol = 12;
        RectangleShape rect = new RectangleShape(Color.RED, 100);
        nodeManager.getNode(currentRow, currentCol).add(rect);
        updateNodeColor(nodeManager.getNode(currentRow, currentCol), Color.GREEN);

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
        for (Component comp : node.getComponents()) {
            if (comp instanceof PaintNode) {
                ((PaintNode) comp).Paint(color);
            }
        }
    }

    private void colorNodesInsideBorder(JPanel startNode, Color fillColor) {
        // Get the coordinates of the starting node
        int startX = nodeManager.getRow(startNode);
        int startY = nodeManager.getCol(startNode);

        // Get the color of the starting node
        Color startColor = startNode.getBackground();

        // Call the recursive flood fill method
        floodFill(startX, startY, startColor, fillColor);
    }

    private void floodFill(int row, int col, Color startColor, Color fillColor) {
        // Get the node at the current position
        JPanel node = nodeManager.getNode(row, col);

        // Check if the node is within the grid and has the starting color
        if (node != null && node.getBackground().equals(startColor)) {
            // Set the color of the node to the fill color
            node.setBackground(fillColor);

            // Recursively call the floodFill method on the adjacent nodes
            floodFill(row - 1, col, startColor, fillColor); // Up
            floodFill(row + 1, col, startColor, fillColor); // Down
            floodFill(row, col - 1, startColor, fillColor); // Left
            floodFill(row, col + 1, startColor, fillColor); // Right
        }
    }
}