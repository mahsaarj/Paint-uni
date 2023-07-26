package package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Menu extends JPanel{

    private JTextField p1NameFld;
    private JTextField p2NameFld;
    private JSpinner levelSpnr;
    private JSpinner SpeedSpnr;
    private JSpinner botSpnr;
    private ActionListener actionListener;

    Menu(ActionListener actionListener){
        this.actionListener = actionListener;
        setBackground(Color.PINK);

        GridLayout gridLayout = new GridLayout(8, 2);
        setLayout(gridLayout);

        addComponents();

    }

    private void addComponents(){
        // Empty cells
        add(new JLabel(" "));
        add(new JLabel(" "));

        // Play buttons
        JButton playBtn = new JButton("Play Single player");
        JButton playMultiBtn = new JButton("Play Multiplayer");

        JButton[] buttons = {playBtn, playMultiBtn};

        // Styles and add play buttons
        for(JButton button : buttons){
            button.setSize(50, 20);
            button.addActionListener(actionListener);
            button.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 20));
            button.setBackground(Color.PINK);
            button.setForeground(Color.BLACK);
            button.setBorderPainted(true);
            button.setFocusPainted(false);

            add(button);
        }

        // Name labels
        JLabel p1name = new JLabel("Name of player 1:");
        JLabel p2name = new JLabel("Name of player 2:");

        JLabel[] labels = {p1name, p2name};

        // Style and add labels
        for(JLabel label : labels){
            label.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 17));
            label.setForeground(Color.BLACK);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            add(label);
        }

        // Name text fields
        p1NameFld = new JTextField();
        p2NameFld = new JTextField();

        JTextField[] textFields = {p1NameFld, p2NameFld};

        //Styles and adds name text fields
        for(JTextField textField : textFields){
            textField.setFont(new Font("Monospaced", Font.PLAIN, 18));
            textField.setBackground(Color.WHITE);
            textField.setForeground(Color.BLACK);
            textField.addMouseListener(new FieldMouseListener(textField));
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setCaretColor(Color.WHITE);
            add(textField);
        }
        // Setting labels and spinners
        JLabel levelLabel = new JLabel("Choose level:");
        levelSpnr = new JSpinner(new SpinnerNumberModel(3 , 1, 3 , 1));
        JLabel speedLabel = new JLabel("Game speed (1-5):");
        SpeedSpnr = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        JLabel botNumberLabel = new JLabel("Number of bots:");
        botSpnr = new JSpinner(new SpinnerNumberModel(10, 0, 25, 1));

        JLabel[] settingLabels = {levelLabel, speedLabel, botNumberLabel};
        JSpinner[] settingSpinners = { levelSpnr ,SpeedSpnr, botSpnr};

        // Style setting labels
        for(JLabel label : settingLabels){
            label.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 20));
            label.setForeground(Color.BLACK);
            label.setHorizontalAlignment(JLabel.RIGHT);
        }

        // Style setting spinners
        for(JSpinner spinner : settingSpinners){
            JTextField textField = (JTextField)spinner.getEditor().getComponent(0);
            spinner.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 20));
            textField.setBackground(Color.PINK);
            textField.setForeground(Color.BLACK);
            textField.setHorizontalAlignment(JTextField.RIGHT);
        }

        JComponent[] settingComponents = {levelLabel, levelSpnr,
                speedLabel, SpeedSpnr,
                botNumberLabel, botSpnr};

        // Add setting labels and spinners
        for(JComponent component : settingComponents){
            add(component);
        }


    }

    public String getP1Name() {
        return p1NameFld.getText();
    }

    public String getP2Name() {
        return p2NameFld.getText();
    }

    public int getGameSpeed() {
        return Integer.valueOf(((JTextField)SpeedSpnr.getEditor().getComponent(0)).getText());
    }

    public int getBotNumber() {
        return Integer.valueOf(((JTextField)botSpnr.getEditor().getComponent(0)).getText());
    }

    class FieldMouseListener implements MouseListener {

        private boolean changed = false;
        private JTextField textField;

        public FieldMouseListener(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(!changed) {
                textField.setText("");
                textField.setForeground(Color.WHITE);
            }
            changed = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }


}