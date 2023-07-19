package Package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RectangleShape extends JPanel {
    private Color color;
    private int size;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public RectangleShape(Color color, int size) {
        this.color = color;
        this.size = size;
        setPreferredSize(new Dimension(size, size));

        // Add mouse listeners
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Color the node that the rectangle enters with green
                setBackground(Color.PINK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Color the node that the rectangle exits with red
                setBackground(Color.RED);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Get the node that the rectangle is currently over
                JPanel node = (JPanel) e.getComponent().getParent();

                // Set the color of the node to green
                node.setBackground(Color.PINK);
            }
        });
    }

}