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
        RectangleShape rect = new RectangleShape(Color.RED, 50);
        nodeManager.getNode(currentRow, currentCol).add(rect);

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
                    moveNode(row - currentRow, col - currentCol);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // do nothing
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

            // Repaint nodes
            currentNode.repaint();
            newNode.repaint();
        }
    }


            // Paint border around nodes within the border of the moved node
            /*List<Point> nodesInBorder = getNodesInBorder();
            for (Point nodePos : nodesInBorder) {
                JPanel node = nodeManager.getNode(nodePos.x, nodePos.y);
                if (!node.equals(currentNode) && !node.equals(newNode)) {
                    PaintNode paintNode = new PaintNode(Color.BLUE, 3);
                    node.add(paintNode);
                    paintNode.setBounds(node.getBounds());
                    paintNode.revalidate();
                    paintNode.repaint();
                }
            }
        }
    }*/

            private RectangleShape getRectangle (JPanel node){
                for (Component c : node.getComponents()) {
                    if (c instanceof RectangleShape) {
                        return (RectangleShape) c;
                    }
                }
                return null;
            }
        }

    /*private List<Point> getNodesInBorder() {
        List<Point> nodesInBorder = new ArrayList<>();
        for (int i = currentRow - 1; i <= currentRow + 1; i++) {
            for (int j = currentCol - 1; j <= currentCol + 1; j++) {
                if (i >= 0 && i < 25 && j >= 0 && j < 25) {
                    nodesInBorder.add(new Point(i, j));
                }
            }
        }
        return nodesInBorder;
    }*/