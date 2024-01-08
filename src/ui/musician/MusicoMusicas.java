package ui.musician;


import data.Music;
import data.Musico;
import ui.RockstarGUI;
import ui.musician.popups.AdicionarMusica;
import ui.musician.popups.AlterarDisponibilidade;
import ui.musician.popups.AlterarNome;
import ui.musician.popups.AlterarPreco;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 * Representa o painel de exibição das músicas do músico na interface do RockStar.
 * Permite visualizar, adicionar,
 * editar nome, preço, disponibilidade
 * e ordenar as músicas por critérios específicos.
 */
public class MusicoMusicas extends JPanel implements ActionListener, MouseListener {
    /**
     * Título da classe MusicoMusicas.
     */
    public static final String TITLE = "MusicianSongs";

    private RockstarGUI gui;
    private final Musico musician;
    private JScrollPane scrollPane;
    private JTable tabela;
    private DefaultTableModel tabelaDefault;
    private JLabel titulo;
    private JPanel painelEast;
    private JPanel painelSuperior;
    private JButton editarDisponibilidade;
    private JButton editarPreco;
    private JButton editarNome;
    private JButton adicionar;
    private ArrayList<Music> musics;



    public MusicoMusicas(RockstarGUI gui) {

        this.gui = gui;
        musician = (Musico) gui.getDb().getCurrentUser();
        setLayout(new BorderLayout());
        setBackground(new Color(77, 24, 28));
        this.musics = new ArrayList<>();


//        ////////////////////////////////////////PAINEL SUPERIOR////////////////////////////////////////////////////////
        painelSuperior = new JPanel(); // Inicializa o painel superior
        painelSuperior.setBackground(new Color(77, 24, 28));
        painelSuperior.setPreferredSize(new Dimension(0, 40)); //Altura do painel Superior
        painelSuperior.setLayout(null);

        //Criar elementos Painel superior
        titulo = new JLabel("Minhas Músicas");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(new Color(255,255,255));
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

        tabelaDefault.addColumn("Titulo");
        tabelaDefault.addColumn("Género");
        tabelaDefault.addColumn("Preço");
        tabelaDefault.addColumn("Disponibilidade");
        tabelaDefault.addColumn("Álbum");

        tabela = new JTable(tabelaDefault);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(200);


        // Impede a movimentação das colunas.
        tabela.getTableHeader().setReorderingAllowed(false);
        // Activa o TableHeader para ser executado
        tabela.getTableHeader().addMouseListener(this);
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
        adicionar = new JButton("Adicionar");
        editarNome = new JButton("\u270E Nome");
        editarPreco = new JButton("\u270E Preço");
        editarDisponibilidade = new JButton("\u270E Disponibilidade");

        adicionar.addActionListener(this);  //adicionar o botão ao actionListener
        editarNome.addActionListener(this);    //adicionar o botão ao actionListener
        editarPreco.addActionListener(this);    //adicionar o botão ao actionListener
        editarDisponibilidade.addActionListener(this);    //adicionar o botão ao actionListener

        //Add ao painel
        painelEast.add(adicionar).setBounds(0, 125, 120, 35);
        painelEast.add(editarNome).setBounds(adicionar.getX(), adicionar.getY() + 50, 120, 35);
        painelEast.add(editarPreco).setBounds(adicionar.getX(),editarNome.getY() + 50, 120, 35);
        painelEast.add(editarDisponibilidade).setBounds(adicionar.getX(),editarPreco.getY() + 50, 120, 35);

        painelEast.setBackground(new Color(77, 24, 28));

        add(painelEast, BorderLayout.EAST);

        setVisible(true);

        carregarMusicasDoMusico();
    }
    private void atualizarTabelaMusicas() {
        // Limpar os dados existentes na tabela
        tabelaDefault.setRowCount(0);
        // Adicionar as músicas do músico à tabela
        // Adicionar as músicas do músico à tabela
        for (Music musica : musics) {
            String visibilidade="";
            if(musica.isVisibilidade()){
                visibilidade = "Disponivel";
            } else{
                visibilidade = "Indisponivel";
            }
            String albumTitle = musica.getAlbum() != null ? musica.getAlbum().getTitle() : "Sem álbum";
            Object[] rowData = {musica.getTitle(), musica.getGenre(), String.format("%1$,.2f€", musica.getPreco()), visibilidade, albumTitle};
            tabelaDefault.addRow(rowData);
        }
        // Atualizar a exibição da tabela
        tabela.repaint();
    }

    public void carregarMusicasDoMusico() {
        // Limpar a lista de álbuns
        musics.clear();
        // Obter a lista de álbuns do músico a partir do objeto RockStar
        ArrayList<Music> musicianSongs = musician.getMusicas();
        // Adicionar os álbuns do músico à lista de álbuns do MusicoMusicas
        if (musicianSongs != null) {
            musics.addAll(musicianSongs);
        }
        // Atualizar a exibição da tabela de álbuns
        atualizarTabelaMusicas();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == editarNome) {
            // Lógica para exibir detalhes da música selecionada
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow != -1) {
                // Obter a música correta com base no índice selecionado na tabela ordenada
                int modelRow = tabela.convertRowIndexToModel(selectedRow);
                //converRowIndexToModel identifica o indice real da linha selecionada, independentemente de onde ela aparece.
                Music musicaSelecionada = musics.get(modelRow);

                new AlterarNome(gui, gui.getMusicianFrame(), musicaSelecionada);
            } else {
                JOptionPane.showMessageDialog(MusicoMusicas.this, "Nenhuma música selecionada.");
            }
            carregarMusicasDoMusico();
            atualizarTabelaMusicas();

        } else if (e.getSource() == adicionar) {
            new AdicionarMusica(gui, gui.getMusicianFrame());
            carregarMusicasDoMusico();
            atualizarTabelaMusicas();

        } else if (e.getSource() == editarPreco) {
            // Lógica para exibir detalhes da música selecionada
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow != -1){
                // Obter a música correta com base no índice selecionado na tabela ordenada
                int modelRow = tabela.convertRowIndexToModel(selectedRow);
                Music musicaSelecionada = musics.get(modelRow);
                new AlterarPreco(gui, gui.getMusicianFrame(), musicaSelecionada);
            }
            else JOptionPane.showMessageDialog(MusicoMusicas.this, "Nenhuma música selecionada.");
            carregarMusicasDoMusico();
            atualizarTabelaMusicas();

        } else if (e.getSource() == editarDisponibilidade) {
            // Lógica para exibir detalhes da música selecionada
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow != -1){
                // Obter a música correta com base no índice selecionado na tabela ordenada
                int modelRow = tabela.convertRowIndexToModel(selectedRow);
                Music musicaSelecionada = musics.get(modelRow);
                new AlterarDisponibilidade(gui, gui.getMusicianFrame(), musicaSelecionada);
            }
            else JOptionPane.showMessageDialog(MusicoMusicas.this, "Nenhuma música selecionada.");
            carregarMusicasDoMusico();
            atualizarTabelaMusicas();
        }
    }
    /**
     * Método chamado quando ocorre um clique do mouse.
     * Se um clique é detectado, ordena as músicas em ordem crescente no primeiro click e em ordem decrescente no segundo click.
     * @param e Evento de mouse
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            if (e.getSource() == tabela.getTableHeader()) {
                int columnIndex = tabela.columnAtPoint(e.getPoint()); // Obtém o índice da coluna clicada
                if (columnIndex == 0) { // Verifica se o clique foi na primeira coluna (Título)
                    musics = gui.getDb().ordenarMusicasCrescente(CriteriosMusica.Nome, musics);// Ordena as músicas pelo título
                    atualizarTabelaMusicas(); // Atualiza a exibição da tabela com as músicas ordenadas
                } else if (columnIndex == 1) { // Verifica se o clique foi na primeira coluna (Genero)
                    musics = gui.getDb().ordenarMusicasCrescente(CriteriosMusica.Genero, musics); // Ordena as músicas pelo título
                    atualizarTabelaMusicas(); // Atualiza a exibição da tabela com as músicas ordenadas
                }
            }
        }else if(e.getClickCount() == 2) {
            if (e.getSource() == tabela.getTableHeader()) {
                int columnIndex = tabela.columnAtPoint(e.getPoint()); // Obtém o índice da coluna clicada
                if (columnIndex == 0) { // Verifica se o clique foi na primeira coluna (Título)
                    musics = gui.getDb().ordenarMusicasDecrescente(CriteriosMusica.Nome, musics);// Ordena as músicas pelo título
                    atualizarTabelaMusicas(); // Atualiza a exibição da tabela com as músicas ordenadas
                } else if (columnIndex == 1) { // Verifica se o clique foi na primeira coluna (Genero)
                    musics = gui.getDb().ordenarMusicasDecrescente(CriteriosMusica.Genero, musics); // Ordena as músicas pelo título
                    atualizarTabelaMusicas(); // Atualiza a exibição da tabela com as músicas ordenadas
                }
            }
        }
    }



    @Override
    public void mousePressed(MouseEvent e) {

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



