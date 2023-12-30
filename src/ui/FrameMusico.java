package ui;

import data.Musico;
import domain.RockstarDB;
import ui.musician.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameMusico extends JFrame implements ActionListener {

// private MusicoEstatistica musicoEstatistica;
// private MusicoPesquisa musicoPesquisa;
// private MusicoAlbum musicoAlbum;
// private MusicoMusicas musicoMusicas;
// private MusicoMenuInicial musicoMenuInicial;
// private MusicoMeusAlbuns musicoMeusAlbuns;

    public static String TITLE = "Musico";

    private RockstarGUI gui;
    private final Musico musician;
    private JPanel northPanel;
    private JLabel username;
    private JButton back;
    private JButton home;
    private CardLayout cardLayout;
    private JPanel panelContainer;
    private MusicoMenuInicial musicoMenuInicial;
    private MusicoMusicas musicoMusicas;
    private MusicoMeusAlbuns musicoMeusAlbuns;
    private MusicoEstatistica musicoEstatistica;
    private MusicoPesquisa musicoPesquisa;

    public FrameMusico(RockstarGUI gui){
        this.gui = gui;
        musician = (Musico) gui.getDb().getCurrentUser();
    }

    public void start() {
        RockstarDB db= gui.getDb();

        Color fundo = new Color(77, 24, 28);
        Color letras = new Color(255,255,255);
        ImageIcon logoRockStar = new ImageIcon("logo_2.png");

        //especificações básicas do frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setTitle("RockStar.Inc - Musico");
        setIconImage(logoRockStar.getImage());
        setLocationRelativeTo(null);
        setResizable(false);

        // Criação de card layout para implementar os vários paineis
        panelContainer = new JPanel();
        cardLayout = new CardLayout();
        panelContainer.setLayout(cardLayout);

        // Cria os painéis que serão exibidos no cardLayout (todos os paineis serão criados aqui)
        musicoMenuInicial = new MusicoMenuInicial(gui);
        musicoMusicas = new MusicoMusicas(gui);
        musicoMeusAlbuns = new MusicoMeusAlbuns(gui);
        musicoEstatistica = new MusicoEstatistica(gui);
        musicoPesquisa = new MusicoPesquisa(gui);


        // Adicione os painéis ao painel de conteúdo (todos os paineis serão adicionados aqui)
        panelContainer.add(musicoMenuInicial, MusicoMenuInicial.TITLE);
        panelContainer.add(musicoMusicas, MusicoMusicas.TITLE);
        panelContainer.add(musicoMeusAlbuns, MusicoMeusAlbuns.TITLE);
        panelContainer.add(musicoEstatistica, MusicoEstatistica.TITLE);
        panelContainer.add(musicoPesquisa, MusicoPesquisa.TITLE);

        // Adicione os paineis de conteúdo ao frame. Os inseridos anteriormente.
        add(panelContainer);

        //NESTA FRAME //////////////////////////////////////////////////////////////////////////////////////////////////
        // Cria botões sempre visiveis nesta frame
        home = new JButton("⌂");
        home.setFont(new Font("Arial", Font.BOLD, 26));
        home.setFocusable(false);
        back = new JButton("←");
        back.setFont(new Font("Arial", Font.BOLD, 26));
        back.setFocusable(false);
        username = new JLabel("username");
        username.setFont(new Font("Arial", Font.BOLD, 12));
        username.setText(db.getCurrentUser().getUsername());
        username.setForeground(new Color(255,255,255));
        home.addActionListener(this);
        back.addActionListener(this);

        // Crie um painel para os botões e adicione-os ao frame
        northPanel = new JPanel();
        northPanel.setLayout(null);
        northPanel.setPreferredSize(new Dimension(0, 40));

        // Adicione os botões ao painel de botões
        // Define as coordenadas e o dimensoes dos botões e label.
        northPanel.add(back).setBounds(10, 5, 60, 25);
        northPanel.add(home).setBounds(back.getX() + 70, back.getY(), 60, 25);
        northPanel.add(username).setBounds(150, 5, 200, 25);

        // Adicione o painel de botões ao frame
        add(northPanel, BorderLayout.NORTH);
        northPanel.setBackground(fundo);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==home){
            gui.showMusicianMainMenu();
        }
    }

    public void showPanelMusician(String panelName) {
        cardLayout.show(panelContainer, panelName);
    }

    public void updateAlbumTable() {
    }
}


