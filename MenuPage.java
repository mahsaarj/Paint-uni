package Package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPage extends JFrame{
    private JLabel welcomeLabel;
    private JButton playButton;

    public MenuPage() {
        super("Menu Page");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create welcome label
        welcomeLabel = new JLabel("Welcome to the Game!");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        // Create play button
        playButton = new JButton("Play");
        add(playButton, BorderLayout.CENTER);
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a new panel
                JPanel gamePanel = new JPanel();
                gamePanel.setBackground(Color.WHITE);

                // Set the new panel as the content pane of the frame
                setContentPane(gamePanel);
                revalidate();
            }
        });

        setVisible(true);
    }
}
