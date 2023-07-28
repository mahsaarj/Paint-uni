package package1;

import java.awt.*;
import java.util.ArrayList;

public class SmartBot extends BotPlayer {

    SmartBot(int height, int width, Color color , Board board) {
            super(height, width, color);
            this.currentnode = board.getNodeAt(x, y);
        }


    @Override
    public void move() {
        x += dx;
        y += dy;

        // Check for contested nodes owned by other players
        ArrayList<Node> contestedNodes = new ArrayList<Node>();
        for (Node node : currentnode.getNeighbors()) {
            if (node.getContestedOwner() != null && node.getContestedOwner() != this) {
                contestedNodes.add(node);
            }
        }

        // Move towards the closest contested node
        if (!contestedNodes.isEmpty()) {
            Node closestNode = getClosestNode(contestedNodes);
            if (closestNode.getX() < x && dx != 1) {
                dx = -1;
                dy = 0;
            } else if (closestNode.getX() > x && dx != -1) {
                dx = 1;
                dy = 0;
            } else if (closestNode.getY() < y && dy != 1) {
                dx = 0;
                dy = -1;
            } else if (closestNode.getY() > y && dy != -1) {
                dx = 0;
                dy = 1;
            }
        } else {
            // If there are no contested nodes, move randomly
            double rand = Math.random();
            if (rand < 0.05 && dx != -1) {
                dx = 1;
                dy = 0;
            }else if (rand < 0.1 && dx != 1) {
                dx = -1;
                dy = 0;
            }else if (rand < 0.15 && dy != -1) {
                dx = 0;
                dy = 1;
            }else if (rand < 0.2 && dy != 1) {
                dx = 0;
                dy = -1;
            }
        }

        avoidOutOfBounds();
    }

    private Node getClosestNode(ArrayList<Node> nodes) {
        Node closestNode = null;
        double closestDistance = Double.MAX_VALUE;
        for (Node node : nodes) {
            double distance = Math.sqrt(Math.pow(x - node.getX(), 2) + Math.pow(y - node.getY(), 2));
            if (distance < closestDistance) {
                closestNode = node;
                closestDistance = distance;
            }
        }
        return closestNode;
    }

}