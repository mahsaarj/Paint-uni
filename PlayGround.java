package Package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

class Playground extends JFrame {
    private NodeManager nodeManager;
    private JPanel gridPanel;
    private int currentRow;
    private int currentCol;
    private boolean dragging;
    private JPanel[][] nodes;

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

    private void updateNodeColor(JPanel node, Color green) {
        boolean containsRectangle = false;
        for (Component component : node.getComponents()) {
            if (component instanceof RectangleShape) {
                containsRectangle = true;
                break;
            }
        }

        if (containsRectangle) {
            node.setBackground(Color.GREEN);
            int row = -1;
            int col = -1;
            // Find the row and column index of the node in the nodes array
            for (int i = 0; i < 25; i++) {
                for (int j = 0; j < 25; j++) {
                    if (nodes[i][j] == node) {
                        row = i;
                        col = j;
                        break;
                    }
                }
                if (row != -1 && col != -1) {
                    break;
                }
            }
            // Update the coloredNodes array
            if (row != -1 && col != -1) {
                nodeManager.setNodeColored(row, col, true);
            }
        } else {
            node.setBackground(Color.lightGray);
            int row = -1;
            int col = -1;
            // Find the row and column index of the node in the nodes array
            for (int i = 0; i < 25; i++) {
                for (int j = 0; j < 25; j++) {
                    if (nodes[i][j] == node) {
                        row = i;
                        col = j;
                        break;
                    }
                }
                if (row != -1 && col != -1) {
                    break;
                }
            }
            // Update the coloredNodes array
            if (row != -1 && col != -1) {
                nodeManager.setNodeColored(row, col, false);
            }
        }
    }

    private void updateNodesInBorderColor(JPanel node, Color color) {
        List<Point> nodesInBorder = getNodesInBorder();
        for (Point nodePos : nodesInBorder) {
            JPanel borderNode = nodeManager.getNode(nodePos.x, nodePos.y);
            if (!borderNode.equals(node)) {
                updateNodeColor(borderNode, color);
            }
        }
    }

    private List<Point> getNodesInBorder() {
        List<Point> nodesInBorder = new ArrayList<>();
        int startRow = currentRow - 1;
        int startCol = currentCol - 1;
        int endRow = currentRow + 1;
        int endCol = currentCol + 1;
        if (startRow < 0) {
            startRow = 0;
        }
        if (startCol < 0) {
            startCol = 0;
        }
        if (endRow > 24) {
            endRow = 24;
        }
        if (endCol > 24) {
            endCol = 24;
        }
        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                if (row != currentRow || col != currentCol) {
                    nodesInBorder.add(new Point(row, col));
                }
            }
        }
        return nodesInBorder;
    }

    private RectangleShape getRectangle(JPanel node) {
        for (Component comp : node.getComponents()) {
            if (comp instanceof RectangleShape) {
                return (RectangleShape) comp;
            }
        }
        return null;
    }
}