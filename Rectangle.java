package Package1;

import javax.swing.*;
import java.awt.*;

class RectangleShape extends JComponent {
    private Color color;
    private int rectSize;

    public RectangleShape(Color color, int rectSize) {
        this.color = color;
        this.rectSize = rectSize;
        setPreferredSize(new Dimension(rectSize, rectSize));
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public int getRectSize() {
        return rectSize;
    }

    public void setRectSize(int rectSize) {
        this.rectSize = rectSize;
        setPreferredSize(new Dimension(rectSize, rectSize));
        revalidate();
        repaint();
    }
}