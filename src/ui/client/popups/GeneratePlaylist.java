package ui.client.popups;

import domain.RockStarDBStatus;
import ui.RockstarGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Classe que representa a janela de gerar playlist.
 */
public class GeneratePlaylist extends JDialog implements ActionListener {

    private JPanel panelCenter;
    private JPanel panelSouth;
    private JLabel playlistName;
    private JLabel playlistGenre;
    private JLabel playlistSize;
    private JTextField nameField;
    private JTextField sizeField;
    private JComboBox<String> dropdown;
    private JButton okButton;
    private JButton cancelButton;
    private RockstarGUI gui;
    /**
     * Construtor da classe GeneratePlaylist.
     * @param gui A instância da classe RockstarGUI.
     * @param parent O JFrame pai.
     */
    public GeneratePlaylist(RockstarGUI gui, JFrame parent) {
        super(parent, "Gerar Playlist", true);

        this.gui = gui;

        ////Especificações da janela\\\\\
        setSize(400, 220);
        setLayout(new BorderLayout());
        setResizable(false);

        ///////Painel central\\\\\\\\\
        panelCenter = new JPanel(null);

        playlistName = new JLabel();
        playlistName.setText("Nome");
        playlistName.setFont(new Font("Arial", Font.BOLD, 18));
        playlistName.setBounds(100,20,65,25);

        nameField = new JTextField();
        nameField.setBounds(playlistName.getX() + 80, playlistName.getY(), 130, 25);

        playlistSize = new JLabel();
        playlistSize.setText("Tamanho");
        playlistSize.setFont(new Font("Arial", Font.BOLD, 18));
        playlistSize.setBounds(playlistName.getX(), playlistName.getY() + 35, 90, 25);

        sizeField = new JTextField();
        sizeField.setBounds(playlistSize.getX() + 90, playlistSize.getY(), 120, 25);

        playlistGenre = new JLabel();
        playlistGenre.setText("Género");
        playlistGenre.setFont(new Font("Arial", Font.BOLD, 18));
        playlistGenre.setBounds(playlistSize.getX(), playlistSize.getY() + 35, 90, 25);

        dropdown = new JComboBox<>(gui.getDb().getMusicGenrs());
        dropdown.setBounds(playlistGenre.getX() + 90, playlistGenre.getY(), 120, 25);

        panelCenter.add(playlistName);
        panelCenter.add(nameField);
        panelCenter.add(playlistSize);
        panelCenter.add(sizeField);
        panelCenter.add(playlistGenre);
        panelCenter.add(dropdown);

        //////Painel sul\\\\\\\\\\\
        panelSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));

        okButton = new JButton();
        okButton.setText("Ok");
        okButton.setFocusable(false);
        okButton.addActionListener(this);

        cancelButton = new JButton();
        cancelButton.setText("Cancelar");
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(this);

        panelSouth.add(okButton);
        panelSouth.add(cancelButton);

        /////////Painel Principal\\\\\\\\\\\\\

        add(panelCenter, BorderLayout.CENTER);
        add(panelSouth, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String escolhaGenero = (String) dropdown.getSelectedItem();
        String escolhaNome = nameField.getText().trim();
        String quantidade = sizeField.getText();
        if(e.getSource() == cancelButton){
            dispose();
        } else if (e.getSource() == okButton) {
            DefaultTableModel myPlaylistsTableModel = gui.getMyPlaylistsTableModel();
            JTable myPlaylistsTable = gui.getMyPlaylistsTable();
            try {
                int tamanho = Integer.parseInt(quantidade);
                RockStarDBStatus status = gui.getDb().generatePlaylist(escolhaNome, tamanho, escolhaGenero);

                if (status == RockStarDBStatus.DB_PLAYLIST_NAME_ALREADY_EXISTS) {
                    JOptionPane.showMessageDialog(null, "Playlist com esse nome já existe.");
                } else if (status == RockStarDBStatus.DB_SOME_FIELD_IS_EMPTY) {
                    JOptionPane.showMessageDialog(null, "Deixou um campo vazio.");
                } else if (status == RockStarDBStatus.DB_PLAYLIST_GENERATED_SUCCESSFULLY) {
                    gui.atualizarTabelaPlaylists(myPlaylistsTableModel, myPlaylistsTable);
                    JOptionPane.showMessageDialog(null, "Playlist gerada com sucesso.");
                    dispose();
                } else if (status == RockStarDBStatus.DB_PLAYLIST_GENERATED_BUT_WITHOUT_WANTED_SIZE) {
                    gui.atualizarTabelaPlaylists(myPlaylistsTableModel, myPlaylistsTable);
                    JOptionPane.showMessageDialog(null, "Playlist gerada com sucesso, mas sem quantidade pretendida.");
                    dispose();
                } else if (status == RockStarDBStatus.DB_NO_SONGS_ADDED_TO_PLAYLIST) {
                    JOptionPane.showMessageDialog(null, "Erro inesperado, tente novamente.");
                }

            } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira um valor numérico válido.");
            }
        }
    }
}
