package package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaintIo extends JFrame implements ActionListener{

    private Board board;
    private Menu menu;
    private JPanel panel;

    private PaintIo(){
        initUI();
    }

    private void initUI(){

        setSize(600, 600);
        setResizable(true);
        setVisible(true);
        setTitle("Paint.io");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menu = new Menu(this);
        panel = new JPanel(new CardLayout());
        panel.add(menu, "menu");

        add(panel);
    }

    private enum STATE{
        GAME,
        MENU
    }

    private void setState(STATE s){
        CardLayout cardLayout = (CardLayout) panel.getLayout();
        if(s == STATE.GAME){
            cardLayout.show(panel, "board");
            board.setPaused(false);
        }else if(s == STATE.MENU){
            cardLayout.show(panel, "menu");
            board.setPaused(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Play Single player":
                board = new Board(this, menu.getP1Name() , menu.getGameSpeed(), menu);
                panel.add(board, "board");
                setState(STATE.GAME);
                break;
            case "Play Multiplayer":
                board = new Board(this, menu.getP1Name(), menu.getP2Name(), menu.getGameSpeed(), menu.getBotNumber());
                panel.add(board, "board");
                setState(STATE.GAME);
                break;
            case "End Game":
                setState(STATE.MENU);
                break;
        }
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(PaintIo::new);
    }

}