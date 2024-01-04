
package ui.client.popups;

import data.Music;
import data.Playlist;
import domain.RockStarDBStatus;
import domain.RockstarDB;
import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddToPlaylist extends JDialog implements ActionListener {

    private JPanel panelCenter;
    private JPanel panelSouth;
    private JLabel playlistName;
    private JComboBox<Playlist> dropdown;
    private JButton okButton;
    private JButton cancelButton;
    private RockstarGUI gui;
    private Music music;

    public AddToPlaylist(RockstarGUI gui, JFrame parent, Music music) {

        super(parent, "Adicionar música a playlist", true);
        this.gui = gui;
        this.music = music;
        ////Especificações da janela\\\\\
        setSize(400, 150);
        setLayout(new BorderLayout());
        setResizable(false);

        ///////Painel central\\\\\\\\\
        panelCenter = new JPanel(null);

        playlistName = new JLabel();
        playlistName.setText("Escolha a playlist");
        playlistName.setFont(new Font("Arial", Font.BOLD, 16));
        playlistName.setBounds(40,20,150,25);


        dropdown = new JComboBox<>(gui.getDb().getClientPlaylist(gui.getDb().getCurrentUserAsClient()));
        dropdown.setBounds(playlistName.getX() + 160, playlistName.getY(), 150, 25);

        panelCenter.add(dropdown);
        panelCenter.add(playlistName);

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
        if (e.getSource() == cancelButton) {
            dispose();
        }
        else if(e.getSource() == okButton){
            //metodo para adicionar musica à playlist
            Playlist playlist = (Playlist) dropdown.getSelectedItem();

            RockStarDBStatus db = gui.getDb().addMusicaPlaylist(music,playlist);

            if(db==RockStarDBStatus.DB_MUSIC_ALREADY_EXISTS_IN_THE_PLAYLIST){
                JOptionPane.showMessageDialog(null, "A música já existe na Playlist.");
            }else if(db==RockStarDBStatus.DB_MUSIC_ADDED){
                JOptionPane.showMessageDialog(null, "A música foi adiciona com sucesso à Playlist!");
            } else if (db == RockStarDBStatus.DB_MUSIC_CANT_BE_ADDED_TO_PLAYLISTS) {
                JOptionPane.showMessageDialog(null, "Música indisponível para adicionar a playlists.");
            } else{
                JOptionPane.showMessageDialog(null,"Algo inesperado aconteceu.");
            }
            dispose();
        }
    }
}
