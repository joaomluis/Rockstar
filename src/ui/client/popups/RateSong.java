package ui.client.popups;

import data.Music;
import domain.RockstarDB;
import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RateSong extends JDialog implements ActionListener {

    private JPanel panelCenter;
    private JPanel panelSouth;
    private JLabel avaliacao;
    private JComboBox<String> dropdown;
    private JButton okButton;
    private JButton cancelButton;
    private Music music;
    private RockstarGUI gui;

    public RateSong(RockstarGUI gui, JFrame parent, Music music) {
        super(parent, "Avaliar música", true);
        this.gui = gui;
        this.music = music;
        ////Especificações da janela\\\\\
        setSize(400, 150);
        setLayout(new BorderLayout());
        setResizable(false);

        ///////Painel central\\\\\\\\\
        panelCenter = new JPanel(null);

        avaliacao = new JLabel();
        avaliacao.setText("Avaliação");
        avaliacao.setFont(new Font("Arial", Font.BOLD, 18));
        avaliacao.setBounds(80,20,100,25);

        dropdown = new JComboBox<>();
        dropdown.addItem("1");
        dropdown.addItem("2");
        dropdown.addItem("3");
        dropdown.addItem("4");
        dropdown.addItem("5");
        dropdown.setBounds(avaliacao.getX() + 140, avaliacao.getY(), 50, 25);

        panelCenter.add(dropdown);
        panelCenter.add(avaliacao);

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
        if(e.getSource() == cancelButton){
            dispose();
        }
        else if(e.getSource() == okButton){
            String ratingS = (String) (dropdown).getSelectedItem();
            assert ratingS != null; //teste
            double rating = Double.parseDouble(ratingS);
            boolean sucess = gui.getDb().rateSong(music,rating);

            if(!sucess){
                JOptionPane.showMessageDialog(null, "Não foi possivel avaliar a música, pois já possui uma avaliaçao.");
            }else{
                JOptionPane.showMessageDialog(null, "Foi adicionada a sua avaliação com sucesso. Obrigado.");
            }
            dispose();
        }
    }
}
