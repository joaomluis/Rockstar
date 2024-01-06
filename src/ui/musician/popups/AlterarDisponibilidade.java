package ui.musician.popups;


import data.Music;
import domain.RockStarDBStatus;
import domain.RockstarDB;
import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Esta classe representa um pop-up para alterar a disponibilidade de uma música.
 * Permite que um músico altere o estado de disponibilidade (Disponível/Indisponível) de uma música.
 */
public class AlterarDisponibilidade extends JDialog implements ActionListener{
    private JLabel estado;
    private JComboBox<String> disponibilidade;
    private JPanel panelCenter;
    private JPanel panelSouth;
    private JButton okButton;
    private JButton cancelButton;
    private int width = 300;
    private int height = 150;
    private Music music;
    private RockstarGUI gui;
    /**
     * Construtor da classe.
     * @param gui    Referência para a instância do RockstarGUI.
     * @param parent JFrame pai associado ao pop-up.
     * @param music  Referência para a música cuja disponibilidade será alterada.
     */
    public AlterarDisponibilidade(RockstarGUI gui, JFrame parent, Music music) {
        super(parent, "Alterar Disponibilidade", true);
        this.music = music;
        this.gui = gui;

//SETTINGS DA JANELA////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        setSize(width,height);
        setLayout(new BorderLayout());
        setResizable(false);
//CRIAR BOTÕES//////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        okButton = new JButton("OK");
        okButton.addActionListener(this);
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
        if(e.getSource() == okButton){

            String escolhaEstado = (String) disponibilidade.getSelectedItem();

            boolean visibilidade = true;

            if(escolhaEstado.equals("Disponivel"))  visibilidade= true;
            else if(escolhaEstado.equals("Indisponível")) visibilidade = false;


            RockStarDBStatus db = gui.getDb().alterarDisponibilidade(music,visibilidade);

            if(db == RockStarDBStatus.DB_MUSIC_VISIBILITY_CHANGED) JOptionPane.showMessageDialog(null,"Alterou a visibilidade da sua música com sucesso.");
            else if(db == RockStarDBStatus.DB_MUSIC_VISIBILITY_FAIL) JOptionPane.showMessageDialog(null, "Não foi possivel fazer a alteração de visibilidade.");
            else JOptionPane.showMessageDialog(null, "Algo correu mal.");
            dispose(); // Fecha o pop-up.
        }
    }
}