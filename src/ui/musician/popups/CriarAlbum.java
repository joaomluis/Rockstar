package ui.musician.popups;


import data.Album;
import data.Musico;
import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;

public class CriarAlbum extends JDialog implements ActionListener {
    private JPanel panelCenter;
    private JPanel panelSouth;
    private JLabel genero;
    private JLabel nome;
    private JComboBox<String> dropdown;
    private JTextField textField;
    private JButton okButton;
    private JButton cancelButton;
    private int width = 400;
    private int height = 200;
    private RockstarGUI gui;


    public CriarAlbum(RockstarGUI gui, JFrame parent) {
        super(parent, "Criar Album", true);
        this.gui = gui;
//SETTINGS DA JANELA////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        setSize(width, height);
        setLayout(new BorderLayout());
        setResizable(false);
//CRIAR BOTÕES//////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        dropdown = new JComboBox<>(gui.getDb().getMusicGenrs());
        textField = new JTextField();
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancelar");
        nome = new JLabel("Nome:");
        genero = new JLabel("Genero:");
//(PANEL CENTER) CAMPOS LABEL/DROPDOWN/TEXT/////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        panelCenter = new JPanel(null); //Cria painel central

        panelCenter.add(nome).setBounds(80, 10, 50, 25);
        panelCenter.add(textField).setBounds(nome.getX() + nome.getWidth() + 5, nome.getY(), 190, 25);
        panelCenter.add(genero).setBounds(nome.getX(), textField.getY() + textField.getHeight() + 5, 50, 25);
        panelCenter.add(dropdown).setBounds(textField.getX(), nome.getY() + nome.getHeight() + 5, 190, 25);
//(PANEL SOUTH) BOTÕES OK E CANCELAR////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        panelSouth = new JPanel(new FlowLayout(FlowLayout.CENTER)); //Cria painel sul

        panelSouth.add(okButton);
        panelSouth.add(cancelButton);
        //Adicionar ao listener.
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
//(PAINEL PRINCIPAL) ADD AO PAINEL//////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        add(panelCenter, BorderLayout.CENTER);
        add(panelSouth, BorderLayout.SOUTH);
        setLocationRelativeTo(parent); // Centraliza o diálogo em relação à frame principal. (Para ñ aparecer ao canto)
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            dispose();// Fecha o pop-up.
        } else if (e.getSource() == okButton) {
            String escolhaGenero = (String) dropdown.getSelectedItem();
            String escolhaNome = textField.getText();
            Musico musico = (Musico) gui.getDb().getCurrentUser();

            Album novaAlbum = new Album(escolhaNome, musico, escolhaGenero);

            if (!gui.getDb().addAlbum(novaAlbum)) {
                JOptionPane.showMessageDialog(null, "O nome do Album já existe na sua lista.");
            } else {

                gui.getDb().addAlbum(novaAlbum); //chama o metodo do DB que por sua vez adiciona o album no musico e na DB.

                dispose(); // Fecha o pop-up.
            }
        }
    }
}