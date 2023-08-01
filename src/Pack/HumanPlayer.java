package Pack;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HumanPlayer extends Player {
    private int nextKey;
    private int availableShots = 5;


    HumanPlayer(int height, int width, Color color, String name) {
        super(height, width, color);
        this.name = name;
    }

    void setNextKey(int nextKey) {
        this.nextKey = nextKey;
    }

    @Override
    public void move() {
        // check if the player's position is within bounds
        if (x + dx >= 0 && y + dy >= 0) {
            x += dx;
            y += dy;
        } else {
            // adjust the player's position if it goes beyond the bounds
            if (x + dx < 0) {
                x = 0;
            }
            if (y + dy < 0) {
                y = 0;
            }
        }
    }

    void updateD() {
        //Left
        if((nextKey == KeyEvent.VK_LEFT || nextKey == KeyEvent.VK_A) && dx != 1) {
            dx = -1;
            dy = 0;
        }

        //Right
        if((nextKey == KeyEvent.VK_RIGHT || nextKey == KeyEvent.VK_D) && dx != -1) {
            dx = 1;
            dy = 0;
        }

        //Up
        if((nextKey == KeyEvent.VK_UP || nextKey == KeyEvent.VK_W) && dy != 1) {
            dx = 0;
            dy = -1;
        }

        //Down
        if((nextKey == KeyEvent.VK_DOWN || nextKey == KeyEvent.VK_S) && dy != -1) {
            dx = 0;
            dy = 1;
        }
    }

    public int getAvailableShots() {
        return availableShots;
    }

    public void useShot() {
        availableShots--;
    }

}