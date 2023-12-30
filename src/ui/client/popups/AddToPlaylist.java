
package ui.client.popups;

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
    private JComboBox<String> dropdown;
    private JButton okButton;
    private JButton cancelButton;

    public AddToPlaylist(RockstarGUI gui, JFrame parent) {

        super(parent, "Adicionar música a playlist", true);

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

        dropdown = new JComboBox<>();
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
    }
}
