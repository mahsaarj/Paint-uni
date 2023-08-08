package Pack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private int x;
    private int y;
    private Player owner;
    private Player contestedOwner;
    private Color color = Color.white;
    private List<Node> neighbors;
    private List<Node> edges;


    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.owner = null;
        this.contestedOwner = null;
        this.color = Color.white;
        this.neighbors = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public Color getColor() {
        // If a node has an owner and node is not being contested,
        // returns owner's color darkened
        if (owner != null && contestedOwner == null) {
            return owner.getColor().darker();
        }
        // If node has no owner and is being contested,
        // returns contestedOwner's color with an alpha of 100
        else if (owner == null && contestedOwner != null) {
            return new Color(contestedOwner.getColor().getRed(), contestedOwner.getColor().getGreen(),
                    contestedOwner.getColor().getBlue(), 100);
        }
        // If node has an owner and is being contested by someone,
        // returns contestedOwner's color with an alpha of 100
        else if (owner != null && contestedOwner != owner) {
            return blendColors();
        } else {
            return color;
        }
    }

    private Color blendColors() {
        float blendedRed = ((owner.getColor().getRed() / 255f) * (contestedOwner.getColor().getRed() / 255f));
        float blendedGreen = ((owner.getColor().getGreen() / 255f) * (contestedOwner.getColor().getGreen() / 255f));
        float blendedBlue = ((owner.getColor().getBlue() / 255f) * (contestedOwner.getColor().getBlue() / 255f));

        return new Color(((blendedRed + 1) / 2), ((blendedGreen + 1) / 2), ((blendedBlue + 1) / 2));
    }

    public Player getContestedOwner() {
        return contestedOwner;
    }

    public void setContestedOwner(Player contestedOwner) {
        this.contestedOwner = contestedOwner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        if (this.owner != null) {
            this.owner.removenodeOwned(this);
        }
        this.owner = owner;
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void connect(Node node) {
        edges.add(node);
        node.edges.add(this);
        neighbors.add(node);
        node.neighbors.add(this);
    }

    public List<Node> getEdges() {
        return edges;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}