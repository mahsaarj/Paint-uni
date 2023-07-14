package Package1;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

class PaintNode extends JComponent {
    private Color color;
    private int thickness;

    public PaintNode(Color color, int thickness) {
        this.color = color;
        this.thickness = thickness;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(getBackground());
        g.fillRect(thickness, thickness, getWidth() - 2 * thickness, getHeight() - 2 * thickness);
    }
}
