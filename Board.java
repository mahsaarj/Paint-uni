package package1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Timer;
import java.util.*;

public class Board extends JPanel {

    private int areaHeight;
    private int areaWidth;
    private ArrayList<ArrayList<Node>> playField;
    private final int size = 20;

    private int botNumber;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<HumanPlayer> humanPlayers = new ArrayList<>();
    private HashMap<Node, Player> nodePlayerMap = new HashMap<>();

    private int checkCounter = 0;
    private final int checkReset;

    private ArrayList<Player> deadBots = new ArrayList<>();
    private boolean paused = true;
    private ActionListener actionListener;

    private ArrayList<Painter> painters = new ArrayList<>();
    private HashMap<Player, Painter> player_painter = new HashMap<>();

    private List<Color> colorList = new ArrayList<>(Arrays.asList(Color.magenta, Color.green, Color.red,
            Color.blue, Color.orange, Color.yellow, Color.pink, new Color(142,12,255),
            new Color(255,43,119), new Color(100,255,162)));

    Board(ActionListener actionListener, String p1name, int gameSpeed, int botNumber){
        this.actionListener = actionListener;
        this.areaHeight = 100;
        this.areaWidth = 100;
        this.playField = new ArrayList<>();
        this.botNumber = botNumber;
        int[] speeds = {12, 10, 8, 6, 4};
        checkReset = speeds[gameSpeed - 1];

        for (int i = 0; i < areaHeight; i++) {
            ArrayList<Node> row = new ArrayList<>();
            for (int j = 0; j < areaWidth; j++) {
                row.add(new Node(j, i));
            }
            playField.add(row);
        }

        players.add(new HumanPlayer(areaHeight, areaWidth, new Color((int)(Math.random() * 0x1000000)), p1name));
        humanPlayers.add((HumanPlayer)players.get(0));

        initBoard();

        painters.add(new Painter(size, this, humanPlayers.get(0), players));
        player_painter.put(humanPlayers.get(0), painters.get(0));
    }

    Board(ActionListener actionListener, String p1name, String p2name, int gameSpeed, int botNumber) {
        this.actionListener = actionListener;
        this.areaHeight = 100;
        this.areaWidth = 100;
        this.botNumber = botNumber;
        int[] speeds = {12, 10, 8, 6, 4};
        checkReset = speeds[gameSpeed - 1];

        players.add(new HumanPlayer(areaHeight, areaWidth, new Color((int)(Math.random() * 0x1000000)), p1name));
        players.add(new HumanPlayer(areaHeight, areaWidth, new Color((int)(Math.random() * 0x1000000)), p2name));
        humanPlayers.add((HumanPlayer)players.get(0));
        humanPlayers.add((HumanPlayer)players.get(1));

        initBoard();

        painters.add(new Painter(size, this, humanPlayers.get(0), players));
        painters.add(new Painter(size, this, humanPlayers.get(1), players));
        player_painter.put(humanPlayers.get(0), painters.get(0));
        player_painter.put(humanPlayers.get(1), painters.get(1));
    }

    private void initBoard() {

        this.playField = new ArrayList<>();
        for (int i = 0; i < areaHeight; i++) {
            ArrayList<Node> row = new ArrayList<>();
            for (int j = 0; j < areaWidth; j++) {
                row.add(new Node(j, i));
            }
            playField.add(row);
        }

        keyActions();
        setBackground(Color.WHITE);

        // Adds new bots and give them a color either from colorList or randomized
        for (int i = 0; i < botNumber; i++) {
            if (i > 9) {
                players.add(new BotPlayer(playField.size(), playField.get(0).size(),
                        new Color((int) (Math.random() * 0x1000000))));
            } else {
                players.add(new BotPlayer(playField.size(), playField.get(0).size(), colorList.get(i)));
            }
        }

        // Gives each player a starting area and makes sure that they don't spawn too close to each other
        for (int i = 0; i < players.size(); i++) {
            // If bot is too close to another bot, remove it and create a new one instead
            if (!checkPoint(players.get(i))) {
                players.remove(players.get(i));
                i--;
                if (botNumber > 9) {
                    players.add(new BotPlayer(playField.size(), playField.get(0).size(),
                            new Color((int) (Math.random() * 0x1000000))));
                } else {
                    players.add(new BotPlayer(playField.size(), playField.get(0).size(), colorList.get(i)));
                }
                continue;
            } else {
                startingArea(players.get(i));
            }
        }

        // Starts a timer to check the game logic
        final int INITIAL_DELAY = 0;
        final int PERIOD_INTERVAL = 1000 / 60;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(),
                INITIAL_DELAY, PERIOD_INTERVAL);
    }


    private void keyActions(){
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        if(humanPlayers.size() == 1){
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUP");
            actionMap.put("moveUP", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_UP);
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDOWN");
            actionMap.put("moveDOWN", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {humanPlayers.get(0).setNextKey(KeyEvent.VK_DOWN);
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLEFT");
            actionMap.put("moveLEFT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_LEFT);
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRIGHT");
            actionMap.put("moveRIGHT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_RIGHT);
                }
            });
        }else if(humanPlayers.size() == 2){
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveP1UP");
            actionMap.put("moveP1UP", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(1).setNextKey(KeyEvent.VK_UP);
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveP1DOWN");
            actionMap.put("moveP1DOWN", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {humanPlayers.get(1).setNextKey(KeyEvent.VK_DOWN);
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveP1LEFT");
            actionMap.put("moveP1LEFT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(1).setNextKey(KeyEvent.VK_LEFT);
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveP1RIGHT");
            actionMap.put("moveP1RIGHT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(1).setNextKey(KeyEvent.VK_RIGHT);
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "moveP2UP");
            actionMap.put("moveP2UP", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_W);
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "moveP2DOWN");
            actionMap.put("moveP2DOWN", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {humanPlayers.get(0).setNextKey(KeyEvent.VK_S);
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "moveP2LEFT");
            actionMap.put("moveP2LEFT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_A);
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "moveP2RIGHT");
            actionMap.put("moveP2RIGHT", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) { humanPlayers.get(0).setNextKey(KeyEvent.VK_D);
                }
            });
        }

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pause");
        actionMap.put("pause", new AbstractAction() {
            public void actionPerformed(ActionEvent evt) {
                ActionEvent action = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "pause");
                actionListener.actionPerformed(action);
            }
        });
    }

    private void startingArea(Player player) {
        int x = player.getX();
        int y = player.getY();
        if (!checkPoint(player)) {
            Player playerCopy = new BotPlayer(playField.size(), playField.get(0).size(), player.getColor());
            startingArea(playerCopy);
        }
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                player.setnodeOwned(getnode(i, j));
            }
        }
    }

    private boolean checkPoint(Player player) {
        int x = player.getX();
        int y = player.getY();
        for (int i = x - 3; i <= x + 3; i++) {
            for (int j = y - 3; j <= y + 3; j++) {
                if (getnode(i, j).getOwner() != null || getnode(i, j).getContestedOwner() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i = 0; i < painters.size(); i++){
            //Set clipping area for painter
            g.setClip(getWidth()/painters.size() * i,0,getWidth()/painters.size(),getHeight());

            //Move graphics to top-left of clipping area
            g.translate(getWidth()/painters.size() * i,0);

            //Painter paints area
            painters.get(i).draw(g);

            //Move graphics back to top-left of window
            g.translate(-getWidth()/painters.size() * i,0);
        }
        try {
            scoreboard(g);

        } catch(IndexOutOfBoundsException ignored){
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void scoreboard(Graphics g) {
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        FontMetrics fontMetrics = g.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        int barWidth;
        int barHeight = fontHeight + 4;

        Player player;
        String string;
        Color color;

        double highestPercentOwned = players.get(0).getPercentOwned();
        Collections.sort(players);
        for(int i = 0; i < Integer.min(5, players.size()); i++){
            player = players.get(i);
            string = String.format("%.2f%% - " + player.getName(), player.getPercentOwned());
            color = player.getColor();

            barWidth = (int)((player.getPercentOwned() / highestPercentOwned)*(getWidth()/4));
            g.setColor(player.getColor());
            g.fillRect(getWidth() - barWidth,  barHeight*i, barWidth,barHeight);
            // If color is perceived as dark set the font color to white, else black
            if(0.299*color.getRed() + 0.587*color.getGreen() + 0.114*color.getBlue() < 127){
                g.setColor(Color.WHITE);
            }else{
                g.setColor(Color.BLACK);
            }
            g.drawString(string, 2+getWidth() -  barWidth,  barHeight*i + fontHeight);
        }
    }
    private void check() {
        Player player;
        nodePlayerMap.clear();
        for (int i = 0; i < players.size(); i++) {
            player = players.get(i);
            player.move();

            if (player.getX() < 0 || player.getX() >= areaWidth || player.getY() < 0 || player.getY() >= areaHeight) {

                int newWidth = Math.max(player.getX() + 1, areaWidth);
                int newHeight = Math.max(player.getY() + 1, areaHeight);

                // Create new nodes for the expanded area
                for (int y = areaHeight; y < newHeight; y++) {
                    for (int x = areaWidth; x < newWidth; x++) {
                        Node node = new Node(x, y);
                        playField.add(new ArrayList<>(Collections.nCopies(newWidth, node)));
                    }
                }

                areaWidth = newWidth;
                areaHeight = newHeight;
            }

            Node node = getnode(player.getX(), player.getY());
            player.checkCollision(node);
            player.setCurrentnode(node);
            findEncounter(player, node);

            // If player is outside their owned territory
            if (node.getOwner() != player && player.getAlive()) {
                player.setnodeContested(node);
                // If player arrives back to an owned node
            } else if (player.getnodesContested().size() > 0) {
                player.contestToOwned();
                fillEnclosure(player);
            }

            // If BotPlayer is killed, add it to deadBots list
            if (player instanceof BotPlayer && !player.getAlive()) {
                deadBots.add(player);
            }
        }
        botsPoints();

        boolean allKilled = true;
        for (HumanPlayer humanPlayer : humanPlayers) {
            humanPlayer.updateD();
            // Sets painter to stop drawing if humanPlayer is dead
            player_painter.get(humanPlayer).setDraw(humanPlayer.getAlive());
            allKilled = allKilled && !humanPlayer.getAlive();
        }
        if (allKilled) {
            endGame();
        }

        // Remove dead players
        players.removeIf(p -> !p.getAlive());
    }

    private void endGame(){
        JOptionPane.showMessageDialog(this, "You lost, game over", "GAME OVER", JOptionPane.PLAIN_MESSAGE);
        actionListener.actionPerformed(new ActionEvent(this, 0, "End Game"));
    }

    private void botsPoints() {
        for (int i = 0; i < deadBots.size(); i++) {
            if (deadBots.get(i).getAlive()) {
                Player player = new BotPlayer(playField.size(), playField.get(0).size(),
                        new Color((int) (Math.random() * 0x1000000)));
                startingArea(player);
                players.add(player);
                deadBots.remove(deadBots.get(i));
            }
        }
    }

    private void findEncounter(Player player, Node node) {
        // If corresponding node is found in nodePlayerMap
        if(nodePlayerMap.containsKey(node)) {

            for(Map.Entry<Node, Player> entry : nodePlayerMap.entrySet()) {
                if (entry.getKey() == node) {
                    if (entry.getValue().getnodesContested().size() > player.getnodesContested().size()) {
                        entry.getValue().die();
                    } else if (entry.getValue().getnodesContested().size() < player.getnodesContested().size()) {
                        player.die();
                    } else if (entry.getValue().getnodesContested().size() == player.getnodesContested().size()) {
                        if (entry.getValue().getnodesOwned().size() > player.getnodesOwned().size()) {
                            entry.getValue().die();
                        } else {
                            player.die();
                        }
                    }
                }
            }
        }else { // If no corresponding node is found, add node and player to nodePlayerMap
            nodePlayerMap.put(node, player);
        }
        // Remove dead players
        players.removeIf(p -> !p.getAlive());
    }

    private void updateCheck(){
        checkCounter++;
        checkCounter %= checkReset;
    }

    private void fillEnclosure(Player player) {
        // Set boundary
        int maxX = 0;
        int minX = playField.get(0).size();
        int maxY = 0;
        int minY = playField.size();
        for (Node n : player.getnodesOwned()) {
            if (n.getX() > maxX) maxX = n.getX();
            if (n.getX() < minX) minX = n.getX();
            if (n.getY() > maxY) maxY = n.getY();
            if (n.getY() < minY) minY = n.getY();
        }

        // Necessary collections for DFS to work
        ArrayList<Node> outside = new ArrayList<>();
        ArrayList<Node> inside = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>();
        HashSet<Node> toCheck = new HashSet<>();

        // Add all adjacent nodes
        int y;
        int x;
        for (Node n : player.getnodesOwned()) {
            y = n.getY();
            x = n.getX();
            if (y - 1 >= 0) toCheck.add(getnode(x, y - 1));
            if (y + 1 < playField.size()) toCheck.add(getnode(x, y + 1));
            if (x - 1 >= 0) toCheck.add(getnode(x - 1, y));
            if (x + 1 < playField.get(y).size()) toCheck.add(getnode(x + 1, y));
        }

        // Loop over all nodes to do DFS from
        for (Node n : toCheck) {
            if (!inside.contains(n)) {
                Stack<Node> stack = new Stack<>();
                boolean cont = true;
                Node v;
                visited.clear();

                // DFS algorithm
                stack.push(n);
                while ((!stack.empty()) && cont) {
                    v = stack.pop();
                    if (!visited.contains(v) && (v.getOwner() != player)) {
                        y = v.getY();
                        x = v.getX();
                        if (outside.contains(v) //If already declared as outside
                                || x < minX || x > maxX || y < minY || y > maxY //If outside of boundary
                                || x == playField.get(0).size() - 1 || x == 0 || y == 0 || y == playField.size() - 1) { // If it is a edge node
                            cont = false;
                        } else {
                            visited.add(v);
                            if (y - 1 >= 0) stack.push(getnode(x, y - 1));
                            if (y + 1 < playField.size()) stack.push(getnode(x, y + 1));
                            if (x - 1 >= 0) stack.push(getnode(x - 1, y));
                            if (x + 1 < playField.get(y).size()) stack.push(getnode(x + 1, y));
                        }
                    }
                }
                if (cont) { // If DFS don't find boundary
                    inside.addAll(visited);
                } else {
                    outside.addAll(visited);
                }
            }
        }

        // Set all enclosed nodes to be owned by player
        for (Node n : inside) {
            player.setnodeOwned(n);
        }
    }

    void setPaused(Boolean b){
        paused = b;
    }

    int getAreaHeight() {
        return areaHeight;
    }

    int getAreaWidth() {
        return areaWidth;
    }

    int getcheckCounter() {
        return checkCounter;
    }

    int getcheckReset() {
        return checkReset;
    }

    Node getnode(int x, int y) {
        if (y < 0 || y >= playField.size() || x < 0 || x >= playField.get(y).size()) {
            // Return a new Node object with no owner and a contested owner of null
            return new Node(x, y);
        } else {
            return playField.get(y).get(x);
        }
    }

    private class ScheduleTask extends TimerTask {

        @Override
        public void run() {
            if(!paused) {
                updateCheck();
                if (checkCounter == 0) {
                    check();
                }
                repaint();
            }
        }
    }
}