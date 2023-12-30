package ui.auth;

import ui.RockstarGUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuInicial extends JPanel implements ActionListener {

    public static String TITLE = "Menu Inicial";

    private JLabel labelWelcomeText;
    private JButton logInButton;
    private JButton createAccButton;
    private JRadioButton radioButtonClient;
    private JRadioButton radioButtonMusician;
    private RockstarGUI gui;

    public MenuInicial(RockstarGUI gui) {

        this.gui = gui;
        setLayout(null);
        setBackground(new Color(77, 24, 28));

        //Texto da plataforma RockStar
        labelWelcomeText = new JLabel();
        labelWelcomeText.setText("RockStar.Inc");
        labelWelcomeText.setForeground(new Color(229, 141, 46));
        labelWelcomeText.setBorder(new LineBorder(new Color(229, 141, 46)));
        labelWelcomeText.setFont(new Font("Arial", Font.BOLD, 36));
        labelWelcomeText.setBounds(138, 80, 223, 40);

        // Botão de log in
        logInButton = new JButton();
        logInButton.setBounds(100, 200, 115, 32);
        logInButton.setText("Log In");
        logInButton.setFocusable(false);
        logInButton.setFont(new Font("Arial", Font.BOLD, 15));
        logInButton.setForeground(Color.black);
        logInButton.addActionListener(this);

        //Botão de criar nova conta
        createAccButton = new JButton();
        createAccButton.setBounds(285, 200, 115, 32);
        createAccButton.setText("Registar");
        createAccButton.setFocusable(false);
        createAccButton.setFont(new Font("Arial", Font.BOLD, 15));
        createAccButton.setForeground(Color.black);
        createAccButton.addActionListener(this);

        ButtonGroup userGroup = new ButtonGroup();
        //Botão seleção interface Cliente
        radioButtonClient = new JRadioButton();
        radioButtonClient.setBounds(160, 280, 100, 30);
        radioButtonClient.setText("Cliente");
        radioButtonClient.setFont(new Font("Arial", Font.BOLD, 15));
        radioButtonClient.setForeground(new Color(229, 141, 46));
        radioButtonClient.setBackground(new Color(77, 24, 28));
        radioButtonClient.setFocusable(false);
        radioButtonClient.addActionListener(this);

        //Botão seleção interface Musico
        radioButtonMusician = new JRadioButton();
        radioButtonMusician.setBounds(260, 280, 82, 30);
        radioButtonMusician.setText("Músico");
        radioButtonMusician.setFont(new Font("Arial", Font.BOLD, 15));
        radioButtonMusician.setForeground(new Color(229, 141, 46));
        radioButtonMusician.setBackground(new Color(77, 24, 28));
        radioButtonMusician.setFocusable(false);

        userGroup.add(radioButtonClient);
        userGroup.add(radioButtonMusician);

        //Criação do painel e adição dos diferentes componentes

        add(labelWelcomeText);
        add(logInButton);
        add(createAccButton);
        add(radioButtonClient);
        add(radioButtonMusician);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logInButton) {
            if (radioButtonMusician.isSelected()) {
                gui.showMusicianLogin();
            } else if (radioButtonClient.isSelected()) {
                gui.showClientLogin();
            }

        }
        if (e.getSource() == createAccButton) {
            if (radioButtonMusician.isSelected()) {
                gui.showMusicianRegister();
            } else if (radioButtonClient.isSelected()) {
                gui.showClientRegister();
            }
        }
    }
}
