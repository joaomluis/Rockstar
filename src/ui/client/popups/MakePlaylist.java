package ui.client.popups;

import data.Cliente;
import data.Playlist;
import ui.RockstarGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Classe que representa a janela de criação de playlist.
 */
public class MakePlaylist extends JDialog implements ActionListener {

    private JPanel panelCenter;
    private JPanel panelSouth;
    private JLabel playlistName;
    private JTextField nameField;
    private JCheckBox visibilidadePlaylist;
    private JButton okButton;
    private JButton cancelButton;
    /**
     * Construtor da classe MakePlaylist.
     * @param gui A instância da classe RockstarGUI.
     * @param parent O JFrame pai.
     */
    public MakePlaylist(RockstarGUI gui, JFrame parent) {
        super(parent, "Criar Playlist", true);

        ////Especificações da janela\\\\\
        setSize(400, 160);
        setLayout(new BorderLayout());
        setResizable(false);

        ///////Painel central\\\\\\\\\
        panelCenter = new JPanel(null);

        playlistName = new JLabel();
        playlistName.setText("Nome");
        playlistName.setFont(new Font("Arial", Font.BOLD, 18));
        playlistName.setBounds(80,20,80,25);

        nameField = new JTextField();
        nameField.setBounds(playlistName.getX() + 80, 20, 120, 25);

        visibilidadePlaylist = new JCheckBox();
        visibilidadePlaylist.setText("Privado");
        visibilidadePlaylist.setBounds(playlistName.getX() + 60, playlistName.getY() + 30, 90, 20);
        visibilidadePlaylist.setFocusable(false);

        panelCenter.add(playlistName);
        panelCenter.add(nameField);
        panelCenter.add(visibilidadePlaylist);

        //////Painel sul\\\\\\\\\\\
        panelSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));

        okButton = new JButton();
        okButton.setText("Ok");
        okButton.setFocusable(false);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = nameField.getText();

                DefaultTableModel myPlaylistsTableModel = gui.getMyPlaylistsTableModel();
                JTable myPlaylistsTable = gui.getMyPlaylistsTable();
                if (!input.isEmpty()) {

                    Cliente cliente = (Cliente) gui.getDb().getCurrentUser(); // vai buscar a GUI que vai buscar a DB que tem acesso aos dados
                    Playlist novaPlaylist = new Playlist(input, cliente);

                    if (visibilidadePlaylist.isSelected()) {
                        novaPlaylist.setVisibilidade(false);
                    }

                    if (!gui.getDb().addPlaylist(novaPlaylist)) {
                        JOptionPane.showMessageDialog(null, "Playlist com esse nome já existe na sua lista.");
                    } else {
                        gui.getDb().addPlaylist(novaPlaylist);
                        gui.getDb().saveCurrentUser(); // outra vez ir buscar GUI para a DB e guarda a operação
                        System.out.println("Playlist criada");
                        if(gui.getMyPlaylists() != null) {
                            gui.atualizarTabelaPlaylists(myPlaylistsTableModel, myPlaylistsTable);
                        }

                    }

                    dispose();

                } else {
                    JOptionPane.showMessageDialog(parent, "O campo está vazio, insira um valor.");
                }
            }
        });

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
    }
}
