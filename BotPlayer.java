package package1;

import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class BotPlayer extends Player{

    // TODO read all names from file
    private String[] names = {"Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon", "Charizard", "Squirtle",
            "Wartortle", "Blastoise", "Caterpie", "Metapod", "Butterfree", "Weedle", "Kakuna", "Beedrill", "Pidgey",
            "Dratini", "Dragonair", "Dragonite", "Mewtwo", "Mew"};


    BotPlayer(int height, int width, Color color){
        super(height, width, color);
        this.name = names[new Random().nextInt(names.length)];
    }

    @Override
    public void move() {
        x += dx;
        y += dy;
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
        avoidOutOfBounds();

    }

     void avoidOutOfBounds(){
        if(x == 0 && y == height - 1){
            if(dx == -1){
                dx = 0;
                dy = -1;
            }else {
                dx = 1;
                dy = 0;
            }
        }else if(x == width -1 && y == 0){
            if(dx == 1){
                dx = 0;
                dy = 1;
            } else {
                dx = -1;
                dy = 0;
            }
        }else if(x == width - 1 && y == height -1){
            if(dx == 1){
                dx = 0;
                dy = -1;
            }else {
                dx = -1;
                dy = 0;
            }
        }else if(x == 0 && y == 0){
            if(dx == -1){
                dx = 0;
                dy = 1;
            }else {
                dx = 1;
                dy = 0;
            }
        }else if(x == 0 && dx == -1){
            dx = 0;
            dy = 1;
        }else if(x == width -1 &&  dx == 1){
            dx = 0;
            dy = 1;
        }else if(y == 0 && dy == -1){
            dx = 1;
            dy = 0;
        }else if(y == height -1 && dy == 1){
            dx = 1;
            dy = 0;
        }
    }

    @Override
    void die() {
        super.die();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setAlive(true);
            }
        },5000);
    }
}