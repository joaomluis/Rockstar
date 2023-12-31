package ui.client;


import data.Cliente;
import data.Music;
import data.Playlist;
import ui.ClientRootFrame;
import ui.RockstarGUI;
import ui.client.popups.ChangeVisibilityPlaylist;
import ui.client.popups.MakePlaylist;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Painel que exibe as playlists do cliente e permite criar, remover e alterar a visibilidade das playlists.
 */

public class MyPlaylists extends JPanel implements ActionListener {

    public static final String TITLE = "MyPlaylists";

    private RockstarGUI gui;
    private final Cliente client;
    private JPanel topPanel;
    private JPanel eastPanel;
    private JTable playlistTable;
    private DefaultTableModel tableModel;
    private JButton seePlaylist;
    private JButton createPlaylist;
    private JButton deletePlaylist;
    private JButton changeVisibility;
    private JLabel panelTitle;


    /**
     * Painel onde vão estar todas as playlists do cliente e onde este vai
     * poder criar playlists vazias, apagar, mudar a sua visibilidade ou
     * abrir a playlist para ver as suas músicas.
     * @param gui
     */
    public MyPlaylists(RockstarGUI gui) {

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
        panelTitle.setText("Minhas Playlists");
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

        tableModel.addColumn("Nome");
        tableModel.addColumn("Visibilidade");


        playlistTable = new JTable(tableModel);
        playlistTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        playlistTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        // Impede a movimentação das colunas.
        playlistTable.getTableHeader().setReorderingAllowed(false);


        gui.adicionarElementosTabela(playlistTable);

        playlistTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Para evitar eventos duplicados
                    boolean isRowSelected = playlistTable.getSelectedRow() != -1;
                    deletePlaylist.setEnabled(isRowSelected); // Habilita ou desabilita o botão com base na seleção
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(playlistTable);

        // ADD scroll ao Panel
        scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Define as margens
        scrollPane.setBackground(new Color(20, 64, 88));

        add(scrollPane, BorderLayout.CENTER);

        ///////// Painel Este \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        eastPanel = new JPanel();
        eastPanel.setBackground(new Color(20, 64, 88));
        eastPanel.setPreferredSize(new Dimension(150, 0));
        eastPanel.setLayout(null);

        //botão para criar nova playlist vazia
        createPlaylist = new JButton();
        createPlaylist.setText("Criar Playlist");
        createPlaylist.setBounds(0, 120, 120, 35);
        createPlaylist.setFocusable(false);
        createPlaylist.addActionListener(this);

        //botão para abrir playlist selecionada
        seePlaylist = new JButton();
        seePlaylist.setText("Ver");
        seePlaylist.setBounds(0, createPlaylist.getY() + 50, 120, 35);
        seePlaylist.setFocusable(false);
        seePlaylist.addActionListener(this);

        //botão para apagar playlist
        deletePlaylist = new JButton();
        deletePlaylist.setText("Remover");
        deletePlaylist.setBounds(0, seePlaylist.getY() + 50, 120, 35);
        deletePlaylist.setFocusable(false);
        deletePlaylist.addActionListener(this);

        //botão para mudar visibilidade
        changeVisibility = new JButton();
        changeVisibility.setText("<html><div style='display: table-cell; vertical-align: middle; text-align: center;'>Alterar<br/>Visibilidade</html>");
        changeVisibility.setBounds(deletePlaylist.getX(), deletePlaylist.getY() + 50, 120, 45);
        changeVisibility.setFocusable(false);
        changeVisibility.addActionListener(this);

        eastPanel.add(createPlaylist);
        eastPanel.add(seePlaylist);
        eastPanel.add(deletePlaylist);
        eastPanel.add(changeVisibility);

        add(eastPanel, BorderLayout.EAST);
    }

    public JTable getPlaylistTable() {
        return playlistTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getCreatePlaylist() {
        return createPlaylist;
    }

    public JButton getDeletePlaylist() {
        return deletePlaylist;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

        if (e.getSource() == seePlaylist) {
            int selectedRow = playlistTable.getSelectedRow();
            if (selectedRow != -1) {
                Playlist playlistSelecionada = client.getPlaylists().get(selectedRow);

                gui.getClientFrame().getCurrentPlaylist().setPlaylist(playlistSelecionada);     //selecionar a playlist para a CurrentPlaylist
                gui.showCurrentPlaylist();                                                      //abrir o painel

            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma playlist.");
            }
        } else if (e.getSource() == createPlaylist) {
            new MakePlaylist(gui, parent);

        } else if (e.getSource() == deletePlaylist) {
            int selectedRow = playlistTable.getSelectedRow();
            if (selectedRow != -1) {
                // Remove da tabela
                tableModel.removeRow(selectedRow);
                client.getPlaylists().remove(selectedRow);
                gui.getDb().saveCurrentUser();
                createPlaylist.setEnabled(true); // faz com que o botão de avaliar não fique disabled após remover uma música
                deletePlaylist.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma playlist.");
            }
        }
        if (e.getSource() == changeVisibility) {
            int selectedRow = playlistTable.getSelectedRow();
            if (selectedRow != -1) {
                Playlist playlistSelecionada = client.getPlaylists().get(selectedRow);
                new ChangeVisibilityPlaylist(gui, parent, playlistSelecionada);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma playlist.");
            }
        }
    }
}
