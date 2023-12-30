package ui.musician.popups;


import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlterarDisponibilidade extends JDialog implements ActionListener{
    private JLabel estado;
    private JComboBox<String> disponibilidade;
    private JPanel panelCenter;
    private JPanel panelSouth;
    private JButton okButton;
    private JButton cancelButton;
    private int width = 300;
    private int height = 100;

    //private Musica musica;
    public AlterarDisponibilidade(RockstarGUI gui, JFrame parent) {
        super(parent, "Alterar Disponibilidade", true);

        //this.musica = musica;
//SETTINGS DA JANELA////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        setSize(width,height);
        setLayout(new BorderLayout());
        setResizable(false);
//CRIAR BOTÕES//////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancelar");
        estado = new JLabel("Disponibilidade:");
        disponibilidade = new JComboBox<>(new String[]{"Disponivel","Indisponível"});
//(PANEL CENTER) CAMPOS LABEL/DROPDOWN/TEXT/////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        panelCenter = new JPanel(null); //Cria painel central
        panelCenter.add(estado).setBounds(20,10,110,25);
        panelCenter.add(disponibilidade).setBounds(estado.getX()+ estado.getWidth()+20, estado.getY(),130,25);
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
        if(e.getSource() == cancelButton){
            dispose();// Fecha o pop-up.
        }
        else if(e.getSource() == okButton){
            String escolhaEstado = (String) disponibilidade.getSelectedItem();

            boolean visibilidade = true;

            if(escolhaEstado.equals("Disponivel"))  visibilidade= true;
            else if(escolhaEstado.equals("Indisponível")) visibilidade = false;
            //musica.setVisibilidade(visibilidade);
            dispose(); // Fecha o pop-up.
        }
    }
}