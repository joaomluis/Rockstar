package ui.client;

import data.Music;
import ui.RockstarGUI;
import ui.client.popups.AddToPlaylist;
import ui.client.popups.RateSong;
import ui.musician.CriteriosMusica;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Painel onde as músicas compradas pelo cliente são mostradas, com as várias funcionalidades
 * que se podem fazer com as músicas
 */
public class MyMusic extends JPanel implements ActionListener, MouseListener {

    /**
     * Título utilizado para identificar essa seção na interface gráfica.
     */
    public static final String TITLE = "MyMusic";

    private RockstarGUI gui;
    private JPanel topPanel;
    private JPanel eastPanel;
    private JTable musicTable;
    private DefaultTableModel tableModel;
    private JButton removeMusic;
    private JButton rateMusic;
    private JButton addToPlaylist;
    private JLabel panelTitle;
    private JComboBox<CriteriosMusica> dropdown;
    private JTextField searchField;
    private JButton searchButton;
    private ArrayList<Music> musics;


    public MyMusic(RockstarGUI gui) {

        this.gui = gui;
        this.musics = (ArrayList<Music>) gui.getDb().getCurrentUserAsClient().getSongsOwned(); //retorna o array com todas musicas visiveis
        setLayout(new BorderLayout());
        setBackground(new Color(20, 64, 88));

        ///////////Painel Superior\\\\\\\\\\\\\\\\\\\\\\\\\\\
        topPanel = new JPanel();
        topPanel.setBackground(new Color(20, 64, 88));
        topPanel.setPreferredSize(new Dimension(0, 95));
        topPanel.setLayout(null);

        //Titulo do Painel
        panelTitle = new JLabel();
        panelTitle.setText("Minhas Músicas");
        panelTitle.setFont(new Font("Arial", Font.BOLD, 22));
        panelTitle.setForeground(new Color(198, 107, 61));
        panelTitle.setBounds(275, 5, 250, 30);

        //Dropdown
        CriteriosMusica[] itemsToShow = {CriteriosMusica.Nome, CriteriosMusica.Genero};
        dropdown = new JComboBox<>(itemsToShow);
        dropdown.setBounds(30, panelTitle.getY() + 45, 200, 35);

        // Barra pesquisa
        searchField = new JTextField();
        searchField.setBounds(dropdown.getX()+ 225,dropdown.getY(),240,35);
        // botão pesquisar
        searchButton = new JButton();
        searchButton.setText("Pesquisar");
        searchButton.setFocusable(false);
        searchButton.setBounds(searchField.getX() + 280, searchField.getY(), 120, 35);
        searchButton.addActionListener(this);

        topPanel.add(panelTitle);
        topPanel.add(dropdown);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        add(topPanel, BorderLayout.NORTH);

        ///////////Painel Central\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("Titulo");
        tableModel.addColumn("Artista");
        tableModel.addColumn("Género");

        //adiciona as musicas da array list à table, tem que ser trocado por um método mais tarde


        musicTable = new JTable(tableModel);
        musicTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        musicTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        musicTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        // Impede a movimentação das colunas.
        musicTable.getTableHeader().setReorderingAllowed(false);
        musicTable.getTableHeader().addMouseListener(this);

        musicTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Para evitar eventos duplicados
                    boolean isRowSelected = musicTable.getSelectedRow() != -1;
                    rateMusic.setEnabled(isRowSelected); // Habilita ou desabilita o botão com base na seleção
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(musicTable);

        // ADD scroll ao Panel
        scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Define as margens
        scrollPane.setBackground(new Color(20, 64, 88));

        add(scrollPane, BorderLayout.CENTER);

        ///////// Painel Este \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        eastPanel = new JPanel();
        eastPanel.setBackground(new Color(20, 64, 88));
        eastPanel.setPreferredSize(new Dimension(150, 0));
        eastPanel.setLayout(null);

        //botão de avaliar músicas
        rateMusic = new JButton();
        rateMusic.setText("Avaliar Música");
        rateMusic.setBounds(0, 120, 120, 35);
        rateMusic.setFocusable(false);
        rateMusic.addActionListener(this);

        //botão de remover músicas das adquiridas
        removeMusic = new JButton();
        removeMusic.setText("Remover");
        removeMusic.setBounds(0, rateMusic.getY() + 50, 120, 35);
        removeMusic.setFocusable(false);
        removeMusic.addActionListener(this);

        //botão para adicionar música a uma playlist;
        addToPlaylist = new JButton();
        addToPlaylist.setText("<html><div style='display: table-cell; vertical-align: middle; text-align: center;'>Adicionar<br/>Playlist</html>");
        addToPlaylist.setBounds(0, removeMusic.getY() + 50, 120, 45);
        addToPlaylist.setFocusable(false);
        addToPlaylist.addActionListener(this);

        eastPanel.add(rateMusic);
        eastPanel.add(removeMusic);
        eastPanel.add(addToPlaylist);

        add(eastPanel, BorderLayout.EAST);


        atualizarTabelaMusicas(musics);
        setVisible(true);
    }

    public JTable getMusicTable() {
        return musicTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        int selectedRow = musicTable.getSelectedRow();
        if (e.getSource() == rateMusic) {
            if (selectedRow != -1) {

                int modelRow = musicTable.convertRowIndexToModel(selectedRow);
                Music musica = gui.getDb().getCurrentUserAsClient().getSongsOwned().get(modelRow);

                new RateSong(gui, parent, musica);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma música para avaliar.");
            }
        } else if (e.getSource() == removeMusic) {
            if (selectedRow != -1) {
                // Remove da tabela
                tableModel.removeRow(selectedRow);
                gui.getDb().getCurrentUserAsClient().getSongsOwned().remove(selectedRow);
                gui.getDb().saveCurrentUser();
                gui.updateMyMusicTable(tableModel, musicTable);
                rateMusic.setEnabled(true); // faz com que o botão de avaliar não fique disabled após remover uma música
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma música para remover.");
            }
        } else if (e.getSource() == addToPlaylist) {
            if (selectedRow != -1) {
                // Obtenha os detalhes da música selecionada
                int modelRow = musicTable.convertRowIndexToModel(selectedRow);
                Music musica = gui.getDb().getCurrentUserAsClient().getSongsOwned().get(modelRow);
                if (musica.isVisibilidade()) {
                    new AddToPlaylist(gui, parent,musica);

                } else {
                    JOptionPane.showMessageDialog(this, "Música indisponível para adicionar a uma nova playlist.");
                }

                rateMusic.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma música para adicionar.");
            }
        }
        if (e.getSource() == searchButton) {
            CriteriosMusica mo = (CriteriosMusica) dropdown.getSelectedItem();
            String pesquisa = searchField.getText();

            musics = gui.getDb().procurarMinhasMusicasCliente(pesquisa,mo); //atualizar a array com as músicas pesquisadas

            atualizarTabelaMusicas(musics);
        }
    }

    /**
     * Atualiza tabala onde são mostradas as músicas do cliente
     * @param musicasEncontradas Lista com as musicas que vão ser mostradas
     */
    public void atualizarTabelaMusicas(ArrayList<Music> musicasEncontradas) {
        // Limpar a tabela atual
        tableModel.setRowCount(0);
        // Adicionar as músicas encontradas à tabela
        for (Music musica : musicasEncontradas) {
            Object[] rowData = {musica.getTitle(), musica.getArtist(), musica.getGenre()};
            tableModel.addRow(rowData);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 1) {
            if (e.getSource() == musicTable.getTableHeader()) {
                int columnIndex = musicTable.columnAtPoint(e.getPoint()); // Obtém o índice da coluna clicada
                if (columnIndex == 0) { // Verifica se o clique foi na primeira coluna (Título)
                    musics = gui.getDb().ordenarMusicasCrescente(CriteriosMusica.Nome, musics); // Ordena as músicas pelo título
                    atualizarTabelaMusicas(musics); // Atualiza a exibição da tabela com as músicas ordenadas
                } else if (columnIndex == 2) { // Verifica se o clique foi na primeira coluna (Genero)
                    musics = gui.getDb().ordenarMusicasCrescente(CriteriosMusica.Genero, musics); // Ordena as músicas pelo título
                    atualizarTabelaMusicas(musics); // Atualiza a exibição da tabela com as músicas ordenadas
                }
            }
        }else if(e.getClickCount() == 2) {
            if (e.getSource() == musicTable.getTableHeader()) {
                int columnIndex = musicTable.columnAtPoint(e.getPoint()); // Obtém o índice da coluna clicada
                if (columnIndex == 0) { // Verifica se o clique foi na primeira coluna (Título)
                    musics = gui.getDb().ordenarMusicasDecrescente(CriteriosMusica.Nome, musics); // Ordena as músicas pelo título
                    atualizarTabelaMusicas(musics); // Atualiza a exibição da tabela com as músicas ordenadas
                } else if (columnIndex == 2) { // Verifica se o clique foi na primeira coluna (Genero)
                    musics = gui.getDb().ordenarMusicasDecrescente(CriteriosMusica.Genero, musics); // Ordena as músicas pelo título
                    atualizarTabelaMusicas(musics); // Atualiza a exibição da tabela com as músicas ordenadas
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
