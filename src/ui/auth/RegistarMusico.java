package ui.auth;

import domain.RockStarDBStatus;
import domain.RockstarDB;
import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RegistarMusico extends JPanel implements ActionListener {

    public static String TITLE = "Registar Musico";

    // Database
    private RockstarGUI gui;
    private JButton cancelButton;
    private JButton createButton;
    private JLabel tittleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel pinLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField pinField;


    public RegistarMusico(RockstarGUI gui) {

        this.gui = gui;

        setLayout(null);
        setBackground(new Color(77, 24, 28));

        // titulo principal do painel
        tittleLabel = new JLabel();
        tittleLabel.setText("Criar uma conta");
        tittleLabel.setForeground(new Color(229, 141, 46));
        tittleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        tittleLabel.setBounds(138, 30, 223, 40);

        // caixa de texto para o username
        usernameField = new JTextField();
        usernameField.setBounds(230, 115, 150, 28);
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
        createButton = new JButton();
        createButton.setBounds(100, 295, 115, 32);
        createButton.setText("Registar");
        createButton.setFocusable(false);
        createButton.setFont(new Font("Arial", Font.BOLD, 15));
        createButton.setForeground(Color.black);
        createButton.addActionListener(this);

        // botão que cancela tarefa e volta para o painel anterior
        cancelButton = new JButton();
        cancelButton.setBounds(285, createButton.getY(), 115, 32);
        cancelButton.setText("Cancelar");
        cancelButton.setFocusable(false);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 15));
        cancelButton.setForeground(Color.black);
        cancelButton.addActionListener(this);


        add(cancelButton);
        add(createButton);
        add(tittleLabel);
        add(usernameField);
        add(passwordField);
        add(pinField);
        add(usernameLabel);
        add(passwordLabel);
        add(pinLabel);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == createButton) {
            registarMusician();
        }
        if (e.getSource() == cancelButton) {
            gui.showMainMenu();
        }
    }

    private void registarMusician() {
        String inputUsername = usernameField.getText();
        char[] passwordChar = passwordField.getPassword();
        String inputPassword = new String(passwordChar);
        char[] pinChar = pinField.getPassword();
        String inputPin = new String(pinChar);

        if(inputPassword.isEmpty() || inputUsername.isEmpty() || inputPin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduza um user, password ou pin válidos");
            return;
        }
        if (contemDigitos(inputPin)) {
            RockstarDB db = gui.getDb();
            RockStarDBStatus status = db.registarMusico(inputUsername, inputPassword, inputPin);

            if (status == RockStarDBStatus.DB_USER_ADDED) {
                JOptionPane.showMessageDialog(this, "Conta criada com sucesso");
            } else if (status == RockStarDBStatus.DB_USER_ALREADY_EXISTS) {
                JOptionPane.showMessageDialog(this, "Username já existe");
            } else {
                JOptionPane.showMessageDialog(this, "Erro desconhecido");
            }
        } else {
            JOptionPane.showMessageDialog(this, "O pin só pode conter digitos de 0 a 9.");
        }

    }
    /**
     * Método chamado no registo de conta de um Músico que verifica se o campo do pin
     * só contém digitos de 0 a 9.
     * @param input
     * @return
     */
    private boolean contemDigitos(String input) {
        for (char c : input.toCharArray()) {
            if(!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
