package ui.auth;


import domain.RockStarDBStatus;
import domain.RockstarDB;
import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Painel que recebe os dados do utilizador para que este possa
 * ser autenticado na plataforma e aceder ao frame de músico.
 */
public class LogInMusico extends JPanel implements ActionListener {

    public static String TITLE = "Login Musico";

    private RockstarGUI gui;
    private JLabel title;
    private JLabel subtitle;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel pinLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField pinField;
    private JButton logInButton;
    private JButton cancelButton;



    public LogInMusico(RockstarGUI gui) {

        this.gui = gui;

        setLayout(null);
        setBackground(new Color(77, 24, 28));

        // titulo principal do painel
        title = new JLabel();
        title.setText("RockStar.Inc");
        title.setForeground(new Color(229, 141, 46));
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setBounds(138, 30, 223, 40);

        //subtitulo do painel, contem slogan
        subtitle = new JLabel();
        subtitle.setText("Ready to Rock your world!");
        subtitle.setForeground(new Color(229, 141, 46));
        subtitle.setFont(new Font("Arial", Font.BOLD, 24));
        subtitle.setBounds(title.getX() - 43, title.getY() + 40, 310, 30);

        // caixa de texto para o username
        usernameField = new JTextField();
        usernameField.setBounds(230, subtitle.getY() + 67, 150, 28);
        usernameField.setFont(new Font("Arial", Font.BOLD, 13));

        //caixa de texto para a password
        passwordField = new JPasswordField();
        passwordField.setBounds(230, usernameField.getY() + 45, 150, 28);
        passwordField.setFont(new Font("Arial", Font.BOLD, 13));

        //caixa de texto para o Pin
        pinField = new JPasswordField();
        pinField.setBounds(230, passwordField.getY() + 45, 150, 28);
        pinField.setFont(new Font("Arial", Font.BOLD, 13));

        // label do Username
        usernameLabel = new JLabel();
        usernameLabel.setText("Username");
        usernameLabel.setForeground(new Color(229, 141, 46));
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        usernameLabel.setBounds(usernameField.getX() - 80, usernameField.getY(), 70, 28);

        //label da passaword
        passwordLabel = new JLabel();
        passwordLabel.setText("Password");
        passwordLabel.setForeground(new Color(229, 141, 46));
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 13));
        passwordLabel.setBounds(passwordField.getX() - 80, passwordField.getY(), 70, 28);

        //label do pin
        pinLabel = new JLabel();
        pinLabel.setText("Pin");
        pinLabel.setForeground(new Color(229, 141, 46));
        pinLabel.setFont(new Font("Arial", Font.BOLD, 13));
        pinLabel.setBounds(pinField.getX() - 40, pinField.getY(), 40, 28);

        //botão para avançar com o log in
        logInButton = new JButton();
        logInButton.setBounds(100, 295, 115, 32);
        logInButton.setText("Log In");
        logInButton.setFocusable(false);
        logInButton.setFont(new Font("Arial", Font.BOLD, 15));
        logInButton.setForeground(Color.black);
        logInButton.addActionListener(this);

        // botão que cancela tarefa e volta para o painel anterior
        cancelButton = new JButton();
        cancelButton.setBounds(285, logInButton.getY(), 115, 32);
        cancelButton.setText("Cancelar");
        cancelButton.setFocusable(false);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 15));
        cancelButton.setForeground(Color.black);
        cancelButton.addActionListener(this);

        add(cancelButton);
        add(title);
        add(subtitle);
        add(usernameField);
        add(logInButton);
        add(passwordField);
        add(pinField);
        add(usernameLabel);
        add(passwordLabel);
        add(pinLabel);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == logInButton) {
            login();
        }
        if (e.getSource() == cancelButton) {
            gui.showMainMenu();
        }
    }
    /**
     * Metodo que faz verificações do input recebido do utilizador e depois
     * chama o método do loginMusico() para autentitcar o usuário
     */
    private void login() {
        String inputUsername = usernameField.getText();
        char[] passwordChar = passwordField.getPassword();
        String inputPassword = new String(passwordChar);
        char[] pinChar = pinField.getPassword();
        String inputPin = new String(pinChar);

        if (inputPassword.isEmpty() || inputUsername.isEmpty() || inputPin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduza um user e password válidos");
            return;
        }

        RockStarDBStatus status = gui.getDb().loginMusico(inputUsername, inputPassword, inputPin);

        if (status == RockStarDBStatus.DB_USER_LOGIN_SUCCESS) {
            gui.showMusicianFrame();
            usernameField.setText("");
            passwordField.setText("");
            pinField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Login falhou verifique as credenciais");
        }
    }
}
