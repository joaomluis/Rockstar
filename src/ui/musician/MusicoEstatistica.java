package ui.musician;

import data.Musico;
import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Painel que exibe estatísticas relacionadas aos músicos, como o total de usuários, músicos, músicas, álbuns,
 * o valor total das músicas e o valor total das músicas vendidas. Permite visualizar o total de álbuns por género.
 */
public class MusicoEstatistica extends JPanel implements ActionListener {
    /**
     * Título da classe MusicoEstatistica.
     */
    public static final String TITLE = "MusicianStats";

    private RockstarGUI gui;
    private JLabel totalAlbunsGeneroCont;
    private JComboBox<String> genero;
    private double valorTotalMusicasVendidasInt;
    private String valorTotalMusicasVendidasString;
    private JLabel valorTotalMusicasVendidas;
    private double valorTotalMusicasInt;
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
        setLayout(null);

        JLabel titulo = new JLabel("Estatisticas");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(new Color(255,255,255));
        add(titulo).setBounds(290, 5, 250, 30);

        ////TOTAL DE UTILIZZADORES//////////////////////////////////////////////////////////////////////////////////////////
        totalUserInt = gui.getDb().getTotalUsers(); //igualar ao numero total de users
        numeroUserString = String.valueOf(totalUserInt);
        totalUser = new JLabel("Total de utilizadores: "+numeroUserString);
        totalUser.setForeground(new Color(255,255,255));
        totalUser.setFont(new Font("Arial", Font.BOLD, 15));
        add(totalUser).setBounds(150,100,200,35);

        ////TOTAL DE MUSICOS////////////////////////////////////////////////////////////////////////////////////////////////
        totalMusicosInt = gui.getDb().getTotalMusician(); //igualar ao numero total de musicos
        numeroMusicosString = String.valueOf(totalMusicosInt);
        totalMusicos = new JLabel("Total de músicos: "+numeroMusicosString);
        totalMusicos.setForeground(new Color(255,255,255));
        totalMusicos.setFont(new Font("Arial", Font.BOLD, 15));
        add(totalMusicos).setBounds(totalUser.getX(),totalUser.getY()+20,500,35);

        ////TOTAL DE MUSICAS////////////////////////////////////////////////////////////////////////////////////////////////
        totalMusicasInt = gui.getDb().getTotalSongs(); //igualar ao numero total de musicas
        numeroMusicasString = String.valueOf(totalMusicasInt);
        totalMusicas = new JLabel("Total de músicas: "+numeroMusicasString);
        totalMusicas.setForeground(new Color(255,255,255));
        totalMusicas.setFont(new Font("Arial", Font.BOLD, 15));
        add(totalMusicas).setBounds(totalUser.getX(),totalMusicos.getY()+20,500,35);

        ////TOTAL DE ALBUNS/////////////////////////////////////////////////////////////////////////////////////////////////
        totalAlbunsInt = gui.getDb().getTotaAlbums(); //igualar ao numero total de albuns
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
        genero = new JComboBox<>(gui.getDb().getMusicGenrs());
        genero.addActionListener(this);
        add(genero).setBounds(totalAlbunsGenero.getX()+totalAlbunsGenero.getWidth()+5,totalAlbunsGenero.getY(),100,35);
        totalAlbunsGeneroCont =new JLabel();
        totalAlbunsGeneroCont.setForeground(new Color(255,255,255));
        totalAlbunsGeneroCont.setFont(new Font("Arial", Font.BOLD, 15));
        totalAlbunsGeneroCont.setBounds(totalUser.getX()+totalAlbunsGenero.getWidth()+genero.getWidth()+5,totalAlbunsGenero.getY(),100,35);
        add(totalAlbunsGeneroCont);

        ////VALOR TOTAL DE MÚSICAS//////////////////////////////////////////////////////////////////////////////////////////
        valorTotalMusicasInt = gui.getDb().getTotalValueSongs();; //igualar ao valor total de musicas
        valorTotalMusicasString = String.valueOf(valorTotalMusicasInt);
        valorTotalMusicas = new JLabel("Valor total de músicas: "+valorTotalMusicasString);
        valorTotalMusicas.setForeground(new Color(255,255,255));
        valorTotalMusicas.setFont(new Font("Arial", Font.BOLD, 15));
        add(valorTotalMusicas).setBounds(totalUser.getX(),totalAlbunsGenero.getY()+20,500,35);

        ////VALOR TOTAL DE MÚSICAS VENDIDAS/////////////////////////////////////////////////////////////////////////////////
        valorTotalMusicasVendidasInt = gui.getDb().getTotalValueSongs(); //igualar ao valor total de musicas vendidas
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

        int total = gui.getDb().albumByGenre(generoSelecionado);

        totalAlbunsGeneroCont.setText(String.valueOf(total) + " albuns");
    }
}



