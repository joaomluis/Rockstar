package ui.auth;

import domain.RockStarDBStatus;
import domain.RockstarDB;
import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Painel para realizar o registo de uma nova conta de cliente
 */
public class RegistarCliente extends JPanel implements ActionListener {

    public static String TITLE = "Registar Cliente";

    // Database
    private RockstarGUI gui;
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton cancelButton;
    private JButton createButton;
    private JTextField usernameField;
    private JPasswordField passwordField;


    public RegistarCliente(RockstarGUI gui) {
        this.gui = gui;
        createUI();
    }

    private void createUI() {
        setLayout(null);
        setBackground(new Color(77, 24, 28));

        titleLabel = new JLabel();
        titleLabel.setText("Criar uma conta");
        titleLabel.setBounds(138, 50, 223, 40);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(229, 141, 46));

        // caixa de texto para o username
        usernameField = new JTextField();
        usernameField.setBounds(230, 145, 150, 28);
        usernameField.setFont(new Font("Arial", Font.BOLD, 13));

        //caixa de texto para a password
        passwordField = new JPasswordField();
        passwordField.setBounds(230, usernameField.getY() + 45, 150, 28);
        passwordField.setFont(new Font("Arial", Font.BOLD, 13));


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

        //botão de confirmar registo
        createButton = new JButton();
        createButton.setBounds(100, 280, 115, 32);
        createButton.setText("Registar");
        createButton.setFocusable(false);
        createButton.setFont(new Font("Arial", Font.BOLD, 15));
        createButton.setForeground(Color.black);
        createButton.addActionListener(this);

        //botão de cancelar e retrocede para o painel anterior
        cancelButton = new JButton();
        cancelButton.setBounds(285, 280, 115, 32);
        cancelButton.setText("Cancelar");
        cancelButton.setFocusable(false);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 15));
        cancelButton.setForeground(Color.black);
        cancelButton.addActionListener(this);


        add(cancelButton);
        add(createButton);
        add(titleLabel);
        add(usernameField);
        add(passwordField);
        add(usernameLabel);
        add(passwordLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createButton) {
            registarClient();
        }
        if (e.getSource() == cancelButton) {
            gui.showMainMenu();
        }
    }

    /**
     * Método que corre verificações sobre o input do utilizador e chama
     * o método registarCliente para criar um novo objeto Cliente e adiciona-lo
     * à Lista de Users da plataforma.
     */
    private void registarClient() {
        String inputUsername = usernameField.getText().trim();
        char[] passwordChar = passwordField.getPassword();
        String inputPassword = new String(passwordChar);

        if (inputPassword.isEmpty() || inputUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduza um user e password válidos");
            return;
        }

        RockStarDBStatus status = gui.getDb().registarCliente(inputUsername, inputPassword);

        if (status == RockStarDBStatus.DB_USER_ADDED) {
            JOptionPane.showMessageDialog(this, "Conta criada com sucesso");
            usernameField.setText("");
            passwordField.setText("");
            gui.showClientLogin();
        } else if (status == RockStarDBStatus.DB_USER_ALREADY_EXISTS) {
            JOptionPane.showMessageDialog(this, "Username já existe");
        } else {
            JOptionPane.showMessageDialog(this, "Erro desconhecido");
        }
    }

}
