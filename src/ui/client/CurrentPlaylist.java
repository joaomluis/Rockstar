package ui.client;



import data.Cliente;
import data.Music;
import data.Playlist;
import ui.RockstarGUI;
import ui.client.popups.RateSong;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

public class CurrentPlaylist extends JPanel implements ActionListener {

    public static final String TITLE = "CurrentPlaylist";

    private RockstarGUI gui;
    private final Cliente client;
    private JPanel topPanel;
    private JPanel eastPanel;
    private JTable musicTable;
    private DefaultTableModel tableModel;
    private JButton removeMusic;
    private JButton rateMusic;
    private JCheckBox visibilidadePlaylist;
    private JLabel panelTitle;
    private Playlist playlist;

    public CurrentPlaylist(RockstarGUI gui) {

        this.gui = gui;
        client = (Cliente) gui.getDb().getCurrentUser();
        setLayout(new BorderLayout());
        setBackground(new Color(20, 64, 88));


        ///////////Painel Superior\\\\\\\\\\\\\\\\\\\\\\\\\\\
        topPanel = new JPanel();
        topPanel.setBackground(new Color(20, 64, 88));
        topPanel.setPreferredSize(new Dimension(0, 40));
        topPanel.setLayout(null);

        //Titulo do Painel
        panelTitle = new JLabel();
        panelTitle.setText("Nome da playlist");
        panelTitle.setFont(new Font("Arial", Font.BOLD, 22));
        panelTitle.setForeground(new Color(198, 107, 61));
        panelTitle.setBounds(250, 5, 250, 30);

        topPanel.add(panelTitle);

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

        // Adicionando TableRowSorter à JTable - ordena a tabela
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        musicTable.setRowSorter(sorter);

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
        rateMusic.setBounds(0, 150, 120, 35);
        rateMusic.setFocusable(false);
        rateMusic.addActionListener(this);

        //botão de remover músicas das adquiridas
        removeMusic = new JButton();
        removeMusic.setText("Remover");
        removeMusic.setBounds(0, rateMusic.getY() + 50, 120, 35);
        removeMusic.setFocusable(false);
        removeMusic.addActionListener(this);

        //checkbox para definir a visibildade da playlist
        visibilidadePlaylist = new JCheckBox();
        visibilidadePlaylist.setText("Privado");
        visibilidadePlaylist.setBounds(removeMusic.getX() + 30, removeMusic.getY() + 45, 90, 20);
        visibilidadePlaylist.setBackground(new Color(20, 64, 88));
        visibilidadePlaylist.setForeground(new Color(198, 107, 61));
        visibilidadePlaylist.setFocusable(false);

        eastPanel.add(rateMusic);
        eastPanel.add(removeMusic);
        eastPanel.add(visibilidadePlaylist);

        add(eastPanel, BorderLayout.EAST);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (e.getSource() == rateMusic) {
            int selectedRow = musicTable.getSelectedRow();
            if (selectedRow != -1) {

                Music selectedMusic = playlist.getMusic().get(selectedRow);
                new RateSong(gui, parent, selectedMusic);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma música para avaliar.");
            }
        } else if (e.getSource() == removeMusic) {
            int selectedRow = musicTable.getSelectedRow();
            if (selectedRow != -1) {
                // Remove da tabela
                tableModel.removeRow(selectedRow);
                // Remove da lista de músicas
                //musicas.remove(selectedRow);
                rateMusic.setEnabled(true); // faz com que o botão de avaliar não fique disabled após remover uma música
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma música para remover.");
            }
        }
    }

    public void setPlaylist(Playlist playlistSelecionada) {
        if (playlistSelecionada != null) {

            DefaultTableModel myPlaylistsTableModel = gui.getMyPlaylistsTableModel();
            JTable myPlaylistsTable = gui.getMyPlaylistsTable();

            this.playlist = playlistSelecionada;
            panelTitle.setText(playlist.getNome());

            // Limpa a tabela
            tableModel.setRowCount(0);
            visibilidadePlaylist.setSelected(!playlistSelecionada.isVisibilidade());

            visibilidadePlaylist.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {

                    playlistSelecionada.setVisibilidade(false);
                    gui.atualizarTabelaPlaylists(myPlaylistsTableModel, myPlaylistsTable);
                    gui.getMyPlaylistsButtonsToEnable();
                    gui.getDb().saveCurrentUser();

                } else {

                    playlistSelecionada.setVisibilidade(true);
                    gui.atualizarTabelaPlaylists(myPlaylistsTableModel, myPlaylistsTable); //atualiza a tabela para mostrar as alterações 
                    gui.getMyPlaylistsButtonsToEnable(); // volta a ativar os botões após usar porque ficavam desativados
                    gui.getDb().saveCurrentUser();

                }
            });

            // Adiciona as músicas da playlist à tabela
            for (Music music : playlistSelecionada.getMusic()) {
                Object[] rowData = {music.getTitle(), music.getArtist(), music.getGenre()};
                tableModel.addRow(rowData);
            }
        }
    }
}
