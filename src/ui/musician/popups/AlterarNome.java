package ui.musician.popups;

import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlterarNome extends JDialog implements ActionListener{
    private JPanel panelCenter;
    private JPanel panelSouth;
    private JLabel nome;
    private JTextField textField;
    private JButton okButton;
    private JButton cancelButton;
    private int width = 300;
    private int height = 100;
    public AlterarNome(RockstarGUI gui, JFrame parent) {
        super(parent, "Alterar Nome", true);
//SETTINGS DA JANELA////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        setSize(width,height);
        setLayout(new BorderLayout());
        setResizable(false);
//CRIAR BOTÕES//////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        textField = new JTextField();
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancelar");
        nome = new JLabel("Novo nome:");

//(PANEL CENTER) CAMPOS LABEL/DROPDOWN/TEXT/////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        panelCenter = new JPanel(null); //Cria painel central

        panelCenter.add(nome).setBounds(20,10,90,25);
        panelCenter.add(textField).setBounds(nome.getX()+ nome.getWidth(), nome.getY(),170,25);
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
         String escolhaNome = textField.getText();
         //
         dispose(); // Fecha o pop-up.
        }
    }
}