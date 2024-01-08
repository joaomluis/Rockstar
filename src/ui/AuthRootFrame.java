package ui;

import data.Cliente;
import ui.auth.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe que representa o frame principal para a interface de autenticação da plataforma.
 * Onde se pode fazer log in ou registar uma nova conta.
 */
public class AuthRootFrame extends JFrame implements ActionListener {

    RockstarGUI gui;

    // UI
    private JPanel currentPanel;
    private JPanel panelContainer;
    private CardLayout cardLayout;


    // Screens
    private RegistarCliente registarCliente;
    private LogInCliente loginCliente;
    private MenuInicial menuInicial;
    private LogInMusico logInMusico;
    private RegistarMusico registarMusico;

    public AuthRootFrame(RockstarGUI gui) {
        this.gui = gui;
    }


    public void start() {
        ImageIcon logoRockStar = new ImageIcon("logo_2.png");

        //Criação da frame inicial
        setTitle("RockStar.Inc");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(logoRockStar.getImage());


        // Criação de card layout para implementar os vários paineis
        panelContainer = new JPanel();
        cardLayout = new CardLayout();
        panelContainer.setLayout(cardLayout);

        //Inicialização dos vários paineis

        menuInicial = new MenuInicial(gui);
        loginCliente = new LogInCliente(gui);
        registarCliente = new RegistarCliente(gui);
        logInMusico = new LogInMusico(gui);
        registarMusico = new RegistarMusico(gui);

        //Junção dos paines ao card layout
        panelContainer.add(menuInicial, MenuInicial.TITLE);
        panelContainer.add(loginCliente, LogInCliente.TITLE);
        panelContainer.add(registarCliente, RegistarCliente.TITLE);
        panelContainer.add(logInMusico, LogInMusico.TITLE);
        panelContainer.add(registarMusico, RegistarMusico.TITLE);

        add(panelContainer);
        this.currentPanel = menuInicial;
        cardLayout.show(panelContainer, "Menu Inicial");
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void showPanel(String panelName) {
        cardLayout.show(panelContainer, panelName);
    }
}
