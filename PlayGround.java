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

                    // Update border color of nodes in border
                    updateNodesInBorderColor(newNode, Color.BLUE);

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

                        // Update border color of nodes in border
                        updateNodesInBorderColor(newNode, Color.BLUE);

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

            // Update border color of nodes in border
            updateNodesInBorderColor(newNode, Color.BLUE);

            // Repaint nodes
            currentNode.repaint();
            newNode.repaint();
        }
    }

    private RectangleShape getRectangle(JPanel node) {
        for (Component c : node.getComponents()) {
            if (c instanceof RectangleShape) {
                return (RectangleShape) c;
            }
        }
        return null;
    }

    private void updateNodeColor(JPanel node, Color color) {
        for (Component c : node.getComponents()) {
            if (c instanceof Shape) {
                Graphics2D g2d = (Graphics2D) c.getGraphics();
                g2d.setColor(Color.GREEN);
                g2d.fill((Shape) c);
            }
        }
    }

    private void updateNodesInBorderColor(JPanel node, Color color) {
        int row = nodeManager.getRow(node);
        int col = nodeManager.getColumn(node);
        List<JPanel> borderNodes = new ArrayList<>();
        if (row > 0) {
            borderNodes.add(nodeManager.getNode(row - 1, col));
        }
        if (row < 24) {
            borderNodes.add(nodeManager.getNode(row + 1, col));
        }
        if (col > 0) {
            borderNodes.add(nodeManager.getNode(row, col - 1));
        }
        if (col < 24) {
            borderNodes.add(nodeManager.getNode(row, col + 1));
        }
        for (JPanel borderNode : borderNodes) {
            updateNodeColor(borderNode, color);
        }
    }
}