package ui.musician;

import data.Musico;
import domain.RockstarDB;
import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MusicoEstatistica extends JPanel implements ActionListener {

    public static final String TITLE = "MusicianStats";

    private RockstarGUI gui;
    private final Musico musician;
    private JLabel totalAlbunsGeneroCont;
    private JComboBox<String> genero;
    private int valorTotalMusicasVendidasInt;
    private String valorTotalMusicasVendidasString;
    private JLabel valorTotalMusicasVendidas;
    private int valorTotalMusicasInt;
    private String valorTotalMusicasString;
    private JLabel valorTotalMusicas;
    private JLabel totalAlbunsGenero;
    private int totalAlbunsInt;
    private String numeroAlbunsString;
    private JLabel totalAlbuns;
    private String numeroMusicosString;
    private int totalMusicosInt;
    private JLabel totalMusicos;
    private JLabel totalMusicas;
    private String numeroMusicasString;
    private int totalMusicasInt;
    private String numeroUserString;
    private int totalUserInt;
    private JLabel totalUser;

    public MusicoEstatistica(RockstarGUI gui) {
        this.gui = gui;
        musician = (Musico) gui.getDb().getCurrentUser();
        setLayout(null);

        JLabel titulo = new JLabel("Estatisticas");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(new Color(255,255,255));
        add(titulo).setBounds(290, 5, 250, 30);

    ////TOTAL DE UTILIZZADORES//////////////////////////////////////////////////////////////////////////////////////////
        totalUserInt = 10; //igualar ao numero total de users
        numeroUserString = String.valueOf(totalUserInt);
        totalUser = new JLabel("Total de utilizadores: "+numeroUserString);
        totalUser.setForeground(new Color(255,255,255));
        totalUser.setFont(new Font("Arial", Font.BOLD, 15));
        add(totalUser).setBounds(150,100,200,35);
    ////TOTAL DE MUSICOS////////////////////////////////////////////////////////////////////////////////////////////////
        totalMusicosInt = 10; //igualar ao numero total de musicos
        numeroMusicosString = String.valueOf(totalMusicosInt);
        totalMusicos = new JLabel("Total de músicos: "+numeroMusicosString);
        totalMusicos.setForeground(new Color(255,255,255));
        totalMusicos.setFont(new Font("Arial", Font.BOLD, 15));
        add(totalMusicos).setBounds(totalUser.getX(),totalUser.getY()+20,500,35);
    ////TOTAL DE MUSICAS////////////////////////////////////////////////////////////////////////////////////////////////
        totalMusicasInt = 10; //igualar ao numero total de musicas
        numeroMusicasString = String.valueOf(totalMusicasInt);
        totalMusicas = new JLabel("Total de músicas: "+numeroMusicasString);
        totalMusicas.setForeground(new Color(255,255,255));
        totalMusicas.setFont(new Font("Arial", Font.BOLD, 15));
        add(totalMusicas).setBounds(totalUser.getX(),totalMusicos.getY()+20,500,35);
    ////TOTAL DE ALBUNS/////////////////////////////////////////////////////////////////////////////////////////////////
        totalAlbunsInt = 10; //igualar ao numero total de albuns
        numeroAlbunsString = String.valueOf(totalAlbunsInt);
        totalAlbuns = new JLabel("Total de albuns: "+numeroAlbunsString);
        totalAlbuns.setForeground(new Color(255,255,255));
        totalAlbuns.setFont(new Font("Arial", Font.BOLD, 15));
        add(totalAlbuns).setBounds(totalUser.getX(),totalMusicas.getY()+20,500,35);
    ////TOTAL DE ALBUNS POR GENERO//////////////////////////////////////////////////////////////////////////////////////
        totalAlbunsGenero = new JLabel("Total de albuns genero:");
        totalAlbunsGenero.setForeground(new Color(255,255,255));
        totalAlbunsGenero.setFont(new Font("Arial", Font.BOLD, 15));
        add(totalAlbunsGenero).setBounds(totalUser.getX(),totalAlbuns.getY()+20,170,35);
        genero = new JComboBox<>(new String[]{" ", "Rock", "Hip-Hop", "Jazz"});
        genero.addActionListener(this);
        add(genero).setBounds(totalAlbunsGenero.getX()+totalAlbunsGenero.getWidth()+5,totalAlbunsGenero.getY(),100,35);
        totalAlbunsGeneroCont =new JLabel();
        totalAlbunsGeneroCont.setForeground(new Color(255,255,255));
        totalAlbunsGeneroCont.setFont(new Font("Arial", Font.BOLD, 15));
        totalAlbunsGeneroCont.setBounds(totalUser.getX()+totalAlbunsGenero.getWidth()+genero.getWidth()+5,totalAlbunsGenero.getY(),100,35);
        add(totalAlbunsGeneroCont);
    ////VALOR TOTAL DE MÚSICAS//////////////////////////////////////////////////////////////////////////////////////////
        valorTotalMusicasInt = 10; //igualar ao valor total de musicas
        valorTotalMusicasString = String.valueOf(valorTotalMusicasInt);
        valorTotalMusicas = new JLabel("Valor total de músicas: "+valorTotalMusicasString);
        valorTotalMusicas.setForeground(new Color(255,255,255));
        valorTotalMusicas.setFont(new Font("Arial", Font.BOLD, 15));
        add(valorTotalMusicas).setBounds(totalUser.getX(),totalAlbunsGenero.getY()+20,500,35);
    ////VALOR TOTAL DE MÚSICAS VENDIDAS/////////////////////////////////////////////////////////////////////////////////
        valorTotalMusicasVendidasInt = 10; //igualar ao valor total de musicas vendidas
        valorTotalMusicasVendidasString = String.valueOf(valorTotalMusicasVendidasInt);
        valorTotalMusicasVendidas = new JLabel("Valor total de músicas vendidas: "+valorTotalMusicasVendidasString);
        valorTotalMusicasVendidas.setForeground(new Color(255,255,255));
        valorTotalMusicasVendidas.setFont(new Font("Arial", Font.BOLD, 15));
        add(valorTotalMusicasVendidas).setBounds(totalUser.getX(),valorTotalMusicas.getY()+20,500,35);
    ////JANELA//////////////////////////////////////////////////////////////////////////////////////////////////////////
        setBackground(new Color(77, 24, 28));
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String generoSelecionado = (String) genero.getSelectedItem();
        int total = albunsDoGenero(generoSelecionado);
        totalAlbunsGeneroCont.setText(String.valueOf(total) + " albuns");
    }
    public int albunsDoGenero(String genero){
        //metodo para calcular o numero total de musiscas do genero escolhido.
        if(genero.equals("Rock")){
            return 20;
        }
        else if(genero.equals("Hip-Hop")){
            return 10;
        }
        else if(genero.equals("Jazz")){
            return 50;
        }
        else return -1;
    }
    //metodos para os cálculos.
}



