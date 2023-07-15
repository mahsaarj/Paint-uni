package Package1;

import javax.swing.*;
import java.awt.*;

class PaintNode1 extends JPanel {
    private Color color;

    public PaintNode1(Color color) {
        this.color = color;
    }
    public PaintNode1() {
        setPreferredSize(new Dimension(50, 50)); // Set the size of the node
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground()); // Set the color of the node to its current background color
        g.fillRect(0, 0, getWidth(), getHeight()); // Paint the node
    }
    public void paint(Color color) {
        this.color = color;
        repaint();
    }
}