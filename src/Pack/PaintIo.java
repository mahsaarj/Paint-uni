package Pack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaintIo extends JFrame implements ActionListener{

    private Board board;
    private Menu menu;
    private JPanel panel;

    private PaintIo(){
        initFrame();
    }

    private void initFrame(){

        Dimension screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
        setSize(screenSize);
        setResizable(true);
        setVisible(true);
        setTitle("Paint.io");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set the JFrame icon

        ImageIcon icon = new ImageIcon("C:\\Users\\IRAN PC\\Pictures\\PaintIo\\PaintIo.jpg");
        setIconImage(icon.getImage());

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
                board = new Board(this, menu.getP1Name(), menu.getP2Name(), menu.getGameSpeed(), menu);
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