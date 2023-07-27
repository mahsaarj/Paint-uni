package package1;

import java.awt.*;

class Node {

    private Player owner;
    private Color color = Color.white;
    private Player contestedOwner;
    private int x;
    private int y;

    Node(int x, int y){
        this.x = x;
        this.y = y;
    }

    Color getColor(){
        // If a node has an owner and node is not being contested,
        // returns owner's color darkened
        if(owner != null && contestedOwner == null) {
            return owner.getColor().darker();
        }
        // If node has no owner and is being contested,
        // returns contestedOwner's color with an alpha of 100
        else if (owner == null && contestedOwner != null) {
            return(new Color(contestedOwner.getColor().getRed(), contestedOwner.getColor().getGreen(),
                    contestedOwner.getColor().getBlue(), 100));
        }
        // If node has an owner and is being contested by someone,
        // returns contestedOwner's color with an alpha of 100
        else if (owner != null && contestedOwner != owner){
            return blendColors();
        }else{
            return color;
        }
    }

    private Color blendColors(){
        float blendedRed = ((owner.getColor().getRed() / 255f) * (contestedOwner.getColor().getRed() / 255f));
        float blendedGreen = ((owner.getColor().getGreen() / 255f) * (contestedOwner.getColor().getGreen() / 255f));
        float blendedBlue = ((owner.getColor().getBlue() / 255f) * (contestedOwner.getColor().getBlue() / 255f));

        return(new Color(((blendedRed + 1 ) / 2),((blendedGreen + 1) / 2),((blendedBlue +1 )/ 2)));
    }

    Player getContestedOwner() {
        return contestedOwner;
    }

    void setContestedOwner(Player contestedOwner) {
        this.contestedOwner = contestedOwner;
    }

    Player getOwner() {
        return owner;
    }

    void setOwner(Player owner) {
        if(this.owner != null){
            this.owner.removenodeOwned(this);
        }
        this.owner = owner;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

}