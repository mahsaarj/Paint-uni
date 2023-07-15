package Package1;

import javax.swing.*;
import java.awt.*;

class PaintNode extends JPanel {
    public PaintNode() {
        setPreferredSize(new Dimension(50, 50)); // Set the size of the node
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground()); // Set the color of the node to its current background color
        g.fillRect(0, 0, getWidth(), getHeight()); // Paint the node
    }
}