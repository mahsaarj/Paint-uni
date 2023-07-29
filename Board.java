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
    private Menu menu;
    private Color startingColor;

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

    Board(ActionListener actionListener, String p1name, int gameSpeed, Menu menu){
        this.actionListener = actionListener;
        this.areaHeight = 100;
        this.areaWidth = 100;
        this.playField = new ArrayList<>();
        this.menu = menu;
        this.botNumber = menu.getBotNumber();
        int[] speeds = {12, 10, 8, 6, 4};
        checkReset = speeds[gameSpeed - 1];
        this.startingColor = startingColor;

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

        // Initialize players based on selected level
        if (menu.isLevel1Selected()) {
            // Initialize random bots
            for (int i = 0; i < botNumber; i++) {
                if (i > 9) {
                    players.add(new BotPlayer(playField.size(), playField.get(0).size(),
                            new Color((int) (Math.random() * 0x1000000))));
                } else {
                    players.add(new BotPlayer(playField.size(), playField.get(0).size(), colorList.get(i)));
                }
            }
        } else if (menu.isLevel2Selected()) {
            // Initialize smart bots
            for (int i = 0; i < botNumber; i++) {
                if (i > 9) {
                    players.add(new SmartBot(playField.size(), playField.get(0).size(),
                            new Color((int) (Math.random() * 0x1000000)), this));
                } else {
                    players.add(new SmartBot(playField.size(), playField.get(0).size(), colorList.get(i), this));
                }
            }
        }

        // Set starting area for players
        for (Player player : players) {
            startingArea(player);
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

            // add key binding for collision check key (Space key)
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "collisionCheck");
            actionMap.put("collisionCheck", new AbstractAction() {
                private long lastCheckTime = 0;
                private final long CHECK_INTERVAL = 3000; // check every 3 seconds

                public void actionPerformed(ActionEvent evt) {
                    HumanPlayer player = humanPlayers.get(0); // assuming there's only one human player
                    int dx = player.getDx();
                    int dy = player.getDy();
                    int x = player.getX();
                    int y = player.getY();

                    // check if enough time has passed since the last collision check
                    long now = System.currentTimeMillis();
                    long timeSinceLastCheck = now - lastCheckTime; // calculate time since last check
                    if (timeSinceLastCheck < CHECK_INTERVAL) {
                        return;
                    }
                    lastCheckTime = now;

                    // print time since last check
                    System.out.println("Time since last collision check: " + timeSinceLastCheck + " ms");

                    // check for collisions with other players in the direction the player is moving
                    for (Player p : players) {
                        if (p != player && p.getAlive() && isPlayerInMovingDirection(p, x, y, dx, dy)) {
                            p.die(); // kill the other player
                            System.out.println("Player " + p.getName() + " has died!");
                        }
                    }
                }

                private boolean isPlayerInMovingDirection(Player p, int x, int y, int dx, int dy) {
                    // check if the other player is on the same x or y coordinate in the direction the player is moving
                    if (dx > 0 && p.getX() == x + dx * 10 && p.getY() >= y - 10 && p.getY() <= y + 10) {
                        System.out.println("Collision detected with player " + p.getName() + " in the positive x direction.");
                        return true;
                    }
                    if (dx < 0 && p.getX() == x + dx * 10 && p.getY() >= y - 10 && p.getY() <= y + 10) {
                        System.out.println("Collision detected with player " + p.getName() + " in the negative x direction.");
                        return true;
                    }
                    if (dy > 0 && p.getY() == y + dy * 10 && p.getX() >= x - 10 && p.getX() <= x + 10) {
                        System.out.println("Collision detected with player " + p.getName() + " in the positive y direction.");
                        return true;
                    }
                    if (dy < 0 && p.getY() == y + dy * 10 && p.getX() >= x - 10 && p.getX() <= x + 10) {
                        System.out.println("Collision detected with player " + p.getName() + " in the negative y direction.");
                        return true;
                    }
                    return false;
                }
            });

            // add key binding for shoot key (Enter key)
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "shoot");
            actionMap.put("shoot", new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    HumanPlayer player = humanPlayers.get(0); // assuming there's only one human player
                    int numShots = 5; // hardcoded number of shots for the human player
                    // check if the player has any shots left
                    if (player.getAvailableShots() <= 0) {
                        return;
                    }
                    // determine the center node position
                    int centerNodeX = player.getX() + 6 * player.getDx();
                    int centerNodeY = player.getY() + 6 * player.getDy();

                    // calculate the starting and ending positions for the nodes to color
                    int startX = centerNodeX - 1;
                    int startY = centerNodeY - 1;
                    int endX = centerNodeX + 1;
                    int endY = centerNodeY + 1;

                    // iterate over the nodes within the starting and ending positions and color the 3x3 area around the 6th node away from the player's current position
                    for (int x = startX; x <= endX; x++) {
                        for (int y = startY; y <= endY; y++) {
                            if (x < 0 || x >= areaWidth || y < 0 || y >= areaHeight) {
                                continue; // skip nodes outside the game area
                            }
                            playField.get(y).get(x).setColor(player.getColor().darker()); // color the node
                        }
                    }

                    // iterate over all players and check if any of them are occupying one of the nodes in the 3x3 area
                    for (Player p : players) {
                        if (p != player && p.getAlive() && isPlayerIn3x3Area(p, player)) {
                            p.die(); // kill the other player
                        }
                    }

                    // decrement the number of shots left for the player
                    player.useShot();
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


    private boolean isPlayerIn3x3Area(Player targetPlayer, Player shooter) {
        int targetX = targetPlayer.getX();
        int targetY = targetPlayer.getY();
        int shooterX = shooter.getX();
        int shooterY = shooter.getY();

        return (targetX >= shooterX - 1 && targetX <= shooterX + 1 &&
                targetY >= shooterY - 1 && targetY <= shooterY + 1);
    }

    private void startingArea(Player player) {
        int x = player.getX();
        int y = player.getY();
        if (!checkPoint(player)) {
            Player playerCopy;
            if (menu.isLevel2Selected()) {
                playerCopy = new SmartBot(playField.size(), playField.get(0).size(), player.getColor(), this);
            } else {
                playerCopy = new BotPlayer(playField.size(), playField.get(0).size(), player.getColor());
            }
            startingArea(playerCopy);
        } else {
            player.setnodeOwned(getnode(x, y));
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

        // Adjust playField size if player is out of bounds
        if (players.stream().anyMatch(p -> p.getX() < 0)) {
            int minX = players.stream().mapToInt(Player::getX).min().getAsInt();
            int addCols = Math.abs(minX);
            for (int i = 0; i < playField.size(); i++) {
                for (int j = 0; j < addCols; j++) {
                    Node node = new Node(playField.get(i).get(0).getX() - 1, i);
                    playField.get(i).add(0, node);
                }
            }
            areaWidth += addCols;
            for (Player p : players) {
                p.setX(p.getX() + addCols);
            }
        }
        if (players.stream().anyMatch(p -> p.getX() >= areaWidth)) {
            int maxX = players.stream().mapToInt(Player::getX).max().getAsInt();
            int addCols = maxX - areaWidth + 1;
            for (int i = 0; i < playField.size(); i++) {
                for (int j = 0; j < addCols; j++) {
                    Node node = new Node(playField.get(i).get(playField.get(i).size() - 1).getX() + 1, i);
                    playField.get(i).add(node);
                }
            }
            areaWidth += addCols;
        }
        if (players.stream().anyMatch(p -> p.getY() < 0)) {
            int minY = players.stream().mapToInt(Player::getY).min().getAsInt();
            int addRows = Math.abs(minY);
            ArrayList<ArrayList<Node>> newRows = new ArrayList<>();
            for (int i = 0; i < addRows; i++) {
                ArrayList<Node> row = new ArrayList<>();
                for (int j = 0; j < areaWidth; j++) {
                    Node node = new Node(j, playField.get(0).get(0).getY() - 1);
                    row.add(node);
                }
                playField.add(0, row);
            }
            areaHeight += addRows;
            for (Player p : players) {
                p.setY(p.getY() + addRows);
            }
        }
        if (players.stream().anyMatch(p -> p.getY() >= areaHeight)) {
            int maxY = players.stream().mapToInt(Player::getY).max().getAsInt();
            int addRows = maxY - areaHeight + 1;
            ArrayList<ArrayList<Node>> newRows = new ArrayList<>();
            for (int i = 0; i < addRows; i++) {
                ArrayList<Node> row = new ArrayList<>();
                for (int j = 0; j < areaWidth; j++) {
                    Node node = new Node(j, playField.get(playField.size() - 1).get(0).getY() + 1);
                    row.add(node);
                }
                playField.add(row);
            }
            areaHeight += addRows;
        }


        for (int i = 0; i < players.size(); i++) {
            player = players.get(i);
            player.move();

            Node node = getnode(player.getX(), player.getY());
            player.checkCollision(node);
            player.setCurrentnode(node);
            findEncounter(player, node);

            if (node.getOwner() != player && player.getAlive()) {
                player.setnodeContested(node);
            } else if (player.getnodesContested().size() > 0) {
                player.contestToOwned();
                fillEnclosure(player);
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
                Player player;
                if (menu.isLevel2Selected()) {
                    player = new SmartBot(playField.size(), playField.get(0).size(),
                            new Color((int) (Math.random() * 0x1000000)), this);
                } else {
                    player = new BotPlayer(playField.size(), playField.get(0).size(),
                            new Color((int) (Math.random() * 0x1000000)));
                }
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

    private void setBoundary(Player player) {
        int maxX = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        for (Node n : player.getnodesOwned()) {
            if (n.getX() > maxX) maxX = n.getX();
            if (n.getX() < minX) minX = n.getX();
            if (n.getY() > maxY) maxY = n.getY();
            if (n.getY() < minY) minY = n.getY();
        }

        // Adjust boundary if player is out of bounds
        if (minX < 0) {
            maxX -= minX;
            for (Node n : player.getnodesOwned()) {
                n.setX(n.getX() - minX);
            }
            minX = 0;
        }
        if (minY < 0) {
            maxY -= minY;
            for (Node n : player.getnodesOwned()) {
                n.setY(n.getY() - minY);
            }
            minY = 0;
        }

        // Set the boundary values in the player object
        player.setBoundary(minX, minY, maxX, maxY);
    }

    private void addAdjacentNodes(Player player, HashSet<Node> toCheck) {
        for (Node n : player.getnodesOwned()) {
            int y = n.getY();
            int x = n.getX();

            // Check adjacent nodes within the current bounds of the playfield
            if (y - 1 >= 0) toCheck.add(getnode(x, y - 1));
            if (y + 1 < areaHeight) toCheck.add(getnode(x, y + 1));
            if (x - 1 >= 0) toCheck.add(getnode(x - 1, y));
            if (x + 1 < areaWidth) toCheck.add(getnode(x + 1, y));

            // Check adjacent nodes outside the current bounds of the playfield
            if (y == 0) toCheck.add(new Node(x, -1));
            if (y == areaHeight - 1) toCheck.add(new Node(x, areaHeight));
            if (x == 0) toCheck.add(new Node(-1, y));
            if (x == areaWidth - 1) toCheck.add(new Node(areaWidth, y));
        }
    }

    private ArrayList<Node> doDFS(Node startNode, Player player, ArrayList<Node> visited, ArrayList<Node> outside) {
        Stack<Node> stack = new Stack<>();
        boolean cont = true;
        Node v;

        // DFS algorithm
        stack.push(startNode);
        while ((!stack.empty()) && cont) {
            v = stack.pop();
            if (!visited.contains(v) && (v.getOwner() != player)) {
                int y = v.getY();
                int x = v.getX();
                if (outside.contains(v) //If already declared as outside
                        || x < player.getMinX() || x > player.getMaxX() || y < player.getMinY() || y > player.getMaxY() //If outside of boundary
                        || x == playField.get(y).size() - 1 || x == 0 || y == 0 || y == playField.size() - 1) { // If it is a edge node
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
            return visited;
        } else {
            return outside;
        }
    }

    public void fillEnclosure(Player player) {
        // Set boundary of the enclosure
        setBoundary(player);

        // Get adjacent nodes to check for enclosure
        HashSet<Node> toCheck = new HashSet<>();
        addAdjacentNodes(player, toCheck);

        // Initialize collections for DFS algorithm
        ArrayList<Node> outside = new ArrayList<>();
        ArrayList<Node> inside = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>();

        // Loop over all nodes to do DFS from
        for (Node n : toCheck) {
            if (!inside.contains(n)) {
                visited.clear();

                // Perform DFS
                ArrayList<Node> result = doDFS(n, player, visited, outside);

                // Add nodes to inside or outside collection
                if (result == visited) {
                    inside.addAll(result);
                } else {
                    outside.addAll(result);
                }
            }
        }

        // Adjust boundary if any nodes are outside the current bounds of the playfield
        ArrayList<Node> boundaryNodes = new ArrayList<>();
        for (Node n : outside) {
            int y = n.getY();
            int x = n.getX();
            if (y == -1) {
                player.setMinX(Math.min(player.getMinX(), x));
            } else if (y == areaHeight) {
                player.setMaxX(Math.max(player.getMaxX(), x));
            } else if (x == -1) {
                player.setMinY(Math.min(player.getMinY(), y));
            } else if (x == areaWidth) {
                player.setMaxY(Math.max(player.getMaxY(), y));
            }
            boundaryNodes.add(getnode(x, y));
        }

        // Perform DFS again to determine the enclosed area within the new boundary
        visited.clear();
        outside.clear();
        for (Node n : boundaryNodes) {
            if (!visited.contains(n) && (n.getOwner() != player)) {
                ArrayList<Node> result = doDFS(n, player, visited, outside);
                if (result == visited) {
                    inside.addAll(result);
                } else {
                    outside.addAll(result);
                }
            }
        }

        // Assign ownership of enclosed nodes to player
        assignOwnership(player, inside);
    }

    private void assignOwnership(Player player, ArrayList<Node> inside) {
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
        // Adjust x and y to be within bounds
        while (x < 0) {
            for (int i = 0; i < playField.size(); i++) {
                Node newNode = new Node(0, i);
                Node nextNode = playField.get(i).get(0); // get the next node in the row
                newNode.connect(nextNode); // connect the new node to the next node
                playField.get(i).add(0, newNode);
            }
            areaWidth++;
            x++;
        }
        while (y < 0) {
            ArrayList<Node> row = new ArrayList<>();
            for (int i = 0; i < areaWidth; i++) {
                Node newNode = new Node(i, 0);
                Node nextNode = playField.get(0).get(i); // get the next node in the column
                newNode.connect(nextNode); // connect the new node to the next node
                row.add(newNode);
            }
            playField.add(0, row);
            areaHeight++;
            y++;
        }
        while (x >= areaWidth) {
            for (int i = 0; i < playField.size(); i++) {
                Node newNode = new Node(areaWidth, i);
                Node prevNode = playField.get(i).get(areaWidth - 1); // get the previous node in the row
                prevNode.connect(newNode); // connect the previous node to the new node
                playField.get(i).add(newNode);
            }
            areaWidth++;
        }
        while (y >= areaHeight) {
            ArrayList<Node> row = new ArrayList<>();
            for (int i = 0; i < areaWidth; i++) {
                Node newNode = new Node(i, areaHeight);
                Node prevNode = playField.get(areaHeight - 1).get(i); // get the previous node in the column
                prevNode.connect(newNode); // connect the previous node to the new node
                row.add(newNode);
            }
            playField.add(row);
            areaHeight++;
        }

        return playField.get(y).get(x);
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
    public Node getNodeAt(int x, int y) {
        return playField.get(y).get(x);
    }
}
