package ui.musician;


import data.Album;
import data.Musico;
import ui.RockstarGUI;
import ui.musician.popups.CriarAlbum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MusicoMeusAlbuns extends JPanel implements ActionListener {
    public static final String TITLE = "MusicianAlbuns";

    private RockstarGUI gui;
    private final Musico musician;
    private JPanel painelEast;
    private JScrollPane scrollPane;
    private JLabel titulo;
    private JPanel painelSuperior;
    private DefaultTableModel tabelaDefault;
    private JTable tabela;
    private JButton criar;

    public MusicoMeusAlbuns(RockstarGUI gui) {
        this.gui = gui;
        musician = (Musico) gui.getDb().getCurrentUser();
        setLayout(new BorderLayout());


//        ////////////////////////////////////////PAINEL SUPERIOR////////////////////////////////////////////////////////
        painelSuperior = new JPanel(); // Inicializa o painel superior
        painelSuperior.setBackground(new Color(77, 24, 28));
        painelSuperior.setPreferredSize(new Dimension(0, 40)); //Altura do painel Superior
        painelSuperior.setLayout(null);

        //Criar elementos Painel superior
        titulo = new JLabel("Meus Albuns");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(new Color(255, 255, 255));
        painelSuperior.add(titulo).setBounds(250, 5, 250, 30);
        add(painelSuperior, BorderLayout.NORTH);

        ////////////////////////////////////////PAINEL CENTRAL////////////////////////////////////////////////////////
        // Impede alterações na tabela
        tabelaDefault = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Define as colunas da tabela
        tabelaDefault.addColumn("Nome");
        tabelaDefault.addColumn("Gênero");
        tabelaDefault.addColumn("Produtor");

//        // Adiciona as músicas ao modelo de tabela
//        for (Album album : albuns) {
//            Object[] musicaObjeto = {album.getNome(), album.getGenero(), album.getProdutor()};
//            tabelaDefault.addRow(musicaObjeto);
//        }

        // Cria a tabela com o modelo
        tabela = new JTable(tabelaDefault);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(200);
        // Impede a movimentação das colunas.
        tabela.getTableHeader().setReorderingAllowed(false);
        // SCROLL
        scrollPane = new JScrollPane(tabela);
        // ADD scroll ao Panel
        scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Define as margens
        scrollPane.setBackground(new Color(77, 24, 28));

        add(scrollPane, BorderLayout.CENTER);

        ////////////////////////////////////////PAINEL EAST////////////////////////////////////////////////////////
        painelEast = new JPanel();    //Inicializa o painel central
        painelEast.setLayout(null);
        painelEast.setPreferredSize(new Dimension(150, 0));

        //Criar elementos Painel EAST
        criar = new JButton("Criar");
        criar.addActionListener(this);  //adicionar o botão ao actionListener
        //Add elementos ao Painel Central
        painelEast.add(criar).setBounds(0, 175, 120, 35);

        painelEast.setBackground(new Color(77, 24, 28));

        add(painelSuperior, BorderLayout.NORTH);
        add(painelEast, BorderLayout.EAST);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void atualizarTabelaAlbuns() {
        // Limpar os dados existentes na tabela
        tabelaDefault.setRowCount(0);

        // Adicionar as músicas do músico à tabela
        if (gui.getDb().getCurrentUser() instanceof Musico) {

            ArrayList<Album> albuns = ((Musico) gui.getDb().getCurrentUser()).getAlbuns();

            for (Album album : albuns) {
                Object[] rowData = {album.getTitle(), album.getGenre()};
                tabelaDefault.addRow(rowData);
            }
        }

        // Atualizar a exibição da tabela
        tabela.repaint();
    }

    public void carregarAlbunsDoMusico() {
        // Limpar a lista de músicas
        if (gui.getDb().getCurrentUser() instanceof Musico) {
            ArrayList<Album> albuns = ((Musico) gui.getDb().getCurrentUser()).getAlbuns();

            albuns.clear();

            // Obter a lista de músicas do músico a partir do objeto RockStar
            Musico musico = (Musico) gui.getDb().getCurrentUser();
            ArrayList<Album> albunsDoMusico = musico.getAlbuns();

            // Adicionar as músicas do músico à lista de músicas do MusicoMusicas
            if (albunsDoMusico != null) {
                albuns.addAll(albunsDoMusico);
            }
            // Atualizar a exibição da tabela de músicas
            atualizarTabelaAlbuns();
        }
    }

    @Override
    public void actionPerformed (ActionEvent e){

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (e.getSource() == criar) {
            new CriarAlbum(gui, parent);
            carregarAlbunsDoMusico();
            atualizarTabelaAlbuns();
        }
    }
}



