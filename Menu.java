package package1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Menu extends JPanel {

    private JTextField p1NameFld;
    private JTextField p2NameFld;
    private JSpinner SpeedSpnr;
    private JSpinner botSpnr;
    private ActionListener actionListener;
    private JCheckBox level1ChkBox;
    private JCheckBox level2ChkBox;

    public Menu(ActionListener actionListener) {
        this.actionListener = actionListener;
        setBackground(Color.PINK);

        // Set the layout of the parent panel to BorderLayout
        setLayout(new BorderLayout());

        // Add the welcome label to a new JPanel with BorderLayout
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Paint-Io");
        welcomeLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.RED.darker());
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setVerticalAlignment(JLabel.CENTER);
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

        // Add the welcome panel to the parent panel at the top
        add(welcomePanel, BorderLayout.NORTH);

        // Add the other components to the parent panel in a GridLayout
        GridLayout gridLayout = new GridLayout(8, 2);
        JPanel componentsPanel = new JPanel(gridLayout);

        // Add the empty cells
        componentsPanel.add(new JLabel(" "));
        componentsPanel.add(new JLabel(" "));

        // Add the play buttons
        JButton playBtn = new JButton("Play Single player");
        JButton playMultiBtn = new JButton("Play Multiplayer");
        JButton[] buttons = {playBtn, playMultiBtn};

        // Styles and add play buttons
        for (JButton button : buttons) {
            button.setSize(50, 20);
            button.addActionListener(actionListener);
            button.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 20));
            button.setBackground(Color.PINK);
            button.setForeground(Color.BLACK);
            button.setBorderPainted(true);
            button.setFocusPainted(false);

            componentsPanel.add(button);
        }

        // Add the level checkboxes
        level1ChkBox = new JCheckBox("Level 1");
        level2ChkBox = new JCheckBox("Level 2");
        level1ChkBox.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 18));
        level1ChkBox.setBackground(Color.PINK);
        level1ChkBox.setForeground(Color.BLACK);
        level1ChkBox.setFocusable(false);
        level1ChkBox.addActionListener(actionListener);

        level2ChkBox.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 18));
        level2ChkBox.setBackground(Color.PINK);
        level2ChkBox.setForeground(Color.BLACK);
        level2ChkBox.setFocusable(false);
        level2ChkBox.addActionListener(actionListener);

        // Add the level checkbox labels and components to the components panel
        JLabel levelLabel = new JLabel("Select Level:");
        levelLabel.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 20));
        levelLabel.setHorizontalAlignment(JLabel.LEFT);
        levelLabel.setForeground(Color.BLACK);
        componentsPanel.add(levelLabel);
        componentsPanel.add(new JLabel(" ")); // empty label for spacing
        componentsPanel.add(level1ChkBox);
        componentsPanel.add(level2ChkBox);

        ButtonGroup levelGroup = new ButtonGroup();
        levelGroup.add(level1ChkBox);
        levelGroup.add(level2ChkBox);


        // Add the name labels
        JLabel p1name = new JLabel("Name of player 1:");
        JLabel p2name = new JLabel("Name of player 2:");
        JLabel[] labels = {p1name, p2name};

        // Style and add labels
        for (JLabel label : labels) {
            label.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 17));
            label.setForeground(Color.BLACK);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            componentsPanel.add(label);
        }

        // Add the name text fields
        p1NameFld = new JTextField();
        p2NameFld = new JTextField();
        JTextField[] textFields = {p1NameFld, p2NameFld};

        //Styles and adds name text fields
        for (JTextField textField : textFields) {
            textField.setFont(new Font("Monospaced", Font.TRUETYPE_FONT, 18));
            textField.setBackground(Color.WHITE);
            textField.setForeground(Color.BLACK);
            textField.addMouseListener(new FieldMouseListener(textField));
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setCaretColor(Color.BLACK);
            componentsPanel.add(textField);
        }

        // Add the setting labels and spinners
        JLabel speedLabel = new JLabel("Game speed (1-5):");
        SpeedSpnr = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
        JLabel botNumberLabel = new JLabel("Number of bots:");
        botSpnr = new JSpinner(new SpinnerNumberModel(10, 0, 25, 1));
        JLabel[] settingLabels = {speedLabel, botNumberLabel};
        JSpinner[] settingSpinners = { SpeedSpnr, botSpnr};

        // Style setting labels
        for (JLabel label : settingLabels) {
            label.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 20));
            label.setForeground(Color.BLACK);
            label.setHorizontalAlignment(JLabel.RIGHT);
        }

        // Style setting spinners
        for (JSpinner spinner : settingSpinners) {
            JTextField textField = (JTextField) spinner.getEditor().getComponent(0);
            spinner.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 20));
            textField.setBackground(Color.PINK);
            textField.setForeground(Color.BLACK);
            textField.setHorizontalAlignment(JTextField.RIGHT);
        }

        JComponent[] settingComponents ={
                speedLabel, SpeedSpnr,
                botNumberLabel, botSpnr
        };

        // Add setting labels and spinners
        for (JComponent component : settingComponents) {
            componentsPanel.add(component);
        }

        // Add the components panel to the parent panel
        add(componentsPanel, BorderLayout.CENTER);
    }


    public String getP1Name() {
        return p1NameFld.getText();
    }

    public String getP2Name() {
        return p2NameFld.getText();
    }

    public int getGameSpeed() {
        return Integer.valueOf(((JTextField) SpeedSpnr.getEditor().getComponent(0)).getText());
    }

    public int getBotNumber() {
        return Integer.valueOf(((JTextField) botSpnr.getEditor().getComponent(0)).getText());
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
            if (!changed) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
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

    public boolean isLevel1Selected() {
        return level1ChkBox.isSelected() && !level2ChkBox.isSelected();
    }

    public boolean isLevel2Selected() {
        return level2ChkBox.isSelected() && !level1ChkBox.isSelected();
    }


}