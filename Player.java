package package1;

import java.awt.*;
import java.util.ArrayList;

abstract class Player implements Comparable<Player> {

    int x;
    int y;
    int minX , minY;
    int maxX , maxY;
    int dx;
    int dy;
    private Color color;
    private ArrayList<Node> nodesOwned = new ArrayList<>();
    private ArrayList<Node> nodesContested = new ArrayList<>();
    int height;
    int width;
    String name;

    private Boolean isAlive = true;

    Node currentnode;

    Player(int height, int width, Color color){
        x = (int)(Math.random() * (width - 2) +1);
        y = (int)(Math.random() * (height - 2) +1);

        if(x < 5){
            x += 5;
        }else if(x > (width -5)){
            x-= 5;
        }
        if(y < 5){
            y+= 5;
        }else if(y > (height) - 5){
            y -= 5;
        }
        this.color = color;
        this.height = height;
        this.width = width;

        double rand = Math.random();
        if (rand < 0.25) {
            dx = 1;
            dy = 0;
        } else if (rand < 5) {
            dx = -1;
            dy = 0;
        } else if (rand < 0.75) {
            dx = 0;
            dy = 1;
        } else {
            dx = 0;
            dy = -1;
        }
    }

    int getX(){
        return x;
    }

    int getY(){
        return y;
    }

    Color getColor(){
        return color;
    }

    abstract void move();

    void die() {
        isAlive = false;
        ArrayList<Node> ownednodesCopy = (ArrayList<Node>)nodesOwned.clone();
        ArrayList<Node> contestednodesCopy = (ArrayList<Node>)nodesContested.clone();
        for(int i = 0; i < ownednodesCopy.size(); i++){
            ownednodesCopy.get(i).setOwner(null);
        }

        for(int i = 0; i < contestednodesCopy.size(); i++){
            contestednodesCopy.get(i).setContestedOwner(null);
        }
        nodesOwned.clear();
        nodesContested.clear();
        currentnode = null;

    }

    void setnodeOwned(Node n){
        nodesOwned.add(n);
        n.setOwner(this);
        n.setContestedOwner(null);
    }

    void removenodeOwned(Node n){
        nodesOwned.remove(n);
    }

    ArrayList<Node> getnodesOwned(){
        return nodesOwned;
    }

    double getPercentOwned(){
        return 100 * getnodesOwned().size() / (double)(height*width);
    }

    void setnodeContested(Node n){
        nodesContested.add(n);
        n.setContestedOwner(this);
    }

    ArrayList<Node> getnodesContested(){
        return nodesContested;
    }

    void contestToOwned(){
        for (Node n : nodesContested) {
            setnodeOwned(n);
        }
        nodesContested.clear();
    }

    void checkCollision(Node n){
        if (n != null && n.getContestedOwner() != null && n.getContestedOwner() != this) {
            n.getContestedOwner().die();
        }
    }

    void setCurrentnode(Node currentnode) {
        this.currentnode = currentnode;
    }

    public void setBoundary(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    int getDx() {
        return dx;
    }

    int getDy() {
        return dy;
    }

    String getName() {
        return name;
    }

    public int getMaxY() {
        return maxY;
    }


    public int getMaxX() {
        return maxX;
    }


    public int getMinY() {
        return minY;
    }


    public int getMinX() {
        return minX;
    }


    Boolean getAlive() {
        return isAlive;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    public int compareTo(Player player){
        return Integer.compare(player.getnodesOwned().size(), nodesOwned.size());
    }
}