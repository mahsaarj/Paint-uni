package package1;

import java.awt.*;
import java.util.List;

class Painter{

    private int width;
    private int height;
    private final int size;
    private List<Player> players;
    private Player humanPlayer;
    private Board board;
    private boolean draw = true;

    Painter(int size, Board board, Player humanPlayer, List<Player> players){
        this.size = size;
        this.board = board;
        this.players = players;
        this.humanPlayer = humanPlayer;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    void draw(Graphics g){
        if(draw){
            height = g.getClipBounds().height;
            width = g.getClipBounds().width;
            drawplayField(g);
            drawPlayers(g);
        }
    }

    private void drawPlayers(Graphics g){
        int drawX;
        int drawY;

        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        FontMetrics fontMetrics = g.getFontMetrics();

        for (Player player : players) {
            // x and y position relative to humanPlayer at which player should be drawn
            drawX = (player.getX() - humanPlayer.getX()) * size + ((width - size) / 2);
            drawY = (player.getY() - humanPlayer.getY()) * size + ((height - size) / 2);
            if (player != humanPlayer) {
                // For all other players than humanPlayer we need to smooth animations regaurding to animation smoothing
                // of humanPlayer
                drawX += ((player.getDx() - humanPlayer.getDx()) * size
                        * ((board.getcheckCounter() + 1) / (double) board.getcheckReset()));
                drawY += ((player.getDy() - humanPlayer.getDy()) * size
                        * ((board.getcheckCounter() + 1) / (double) board.getcheckReset()));
            }

            g.setColor(Color.BLACK);
            g.drawString(player.getName(),
                    drawX + (size - fontMetrics.stringWidth(player.getName()))/2, drawY+size+16);

            // Draw player if visible
            if (!(drawX + size < 0 || drawX > width || drawY + size < 0 || drawY > height)) {
                g.setColor(player.getColor());
                g.fillRect(drawX, drawY, size, size);
            }
        }
    }

    private void drawplayField(Graphics g) {
        int drawX;
        int drawY;

        for (int y = 0; y < board.getAreaHeight(); y++) {
            for (int x = 0; x < board.getAreaWidth(); x++) {
                // x and y position relative to humanPlayer at which node should be drawn
                drawX = (x - humanPlayer.getX()) * size + ((width - size) / 2)
                        + (int) ((-humanPlayer.getDx()) * size *
                        ((board.getcheckCounter() + 1) / (double) board.getcheckReset()));
                drawY = (y - humanPlayer.getY()) * size + ((height - size) / 2)
                        + (int) ((-humanPlayer.getDy()) * size *
                        ((board.getcheckCounter() + 1) / (double) board.getcheckReset()));

                // If visible, draw first white background and then draw color on top
                if (!(drawX + size < 0 || drawX > width || drawY + size < 0 || drawY > height)) {
                    g.setColor(Color.white);
                    g.fillRect(drawX, drawY, size, size);

                    g.setColor(board.getnode(x, y).getColor());
                    g.fillRect(drawX, drawY, size, size);
                }
            }
        }
    }
}