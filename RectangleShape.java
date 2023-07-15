package Package1;

import javax.swing.*;
import java.awt.*;

public class RectangleShape extends JPanel {
    private Color color;
    private int size;

    public RectangleShape(Color color, int size) {
        this.color = color;
        this.size = size;
        setPreferredSize(new Dimension(size, size));
    }

    @Override
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

    //@Override
    //public void Paint(Color color) {
      //  this.color = color;
        //repaint();
    //}

}