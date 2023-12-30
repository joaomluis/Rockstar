package ui.musician.popups;

import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdicionarMusica extends JDialog implements ActionListener{
    private JLabel preco;
    private JTextField precoText;
    private JPanel panelCenter;
    private JPanel panelSouth;
    private JLabel genero;
    private JLabel nome;
    private JComboBox<String> dropdown;
    private JTextField nomeText;
    private JButton okButton;
    private JButton cancelButton;
    private int width = 400;
    private int height = 170;

    public AdicionarMusica(RockstarGUI gui, JFrame parent) {
        super(parent, "Adicionar Música", true);
//SETTINGS DA JANELA////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        setSize(width,height);
        setLayout(new BorderLayout());
        setResizable(false);
//CRIAR BOTÕES//////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        dropdown = new JComboBox<>(new String[]{" ", "Rock", "Hip-Hop", "Jazz"});
        nomeText = new JTextField();
        precoText = new JTextField();
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancelar");
        nome = new JLabel("Nome:");
        genero = new JLabel("Genero:");
        preco = new JLabel("Preço:");

//(PANEL CENTER) CAMPOS LABEL/DROPDOWN/TEXT/////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        panelCenter = new JPanel(null); //Cria painel central

        panelCenter.add(nome).setBounds(80,10,50,25);
        panelCenter.add(nomeText).setBounds(nome.getX()+nome.getWidth()+5,nome.getY(),190,25);
        panelCenter.add(genero).setBounds(nome.getX(), nomeText.getY()+ nomeText.getHeight()+5,50,25);
        panelCenter.add(dropdown).setBounds(nomeText.getX(),nome.getY()+nome.getHeight()+5,190,25);
        panelCenter.add(preco).setBounds(nome.getX(), genero.getY()+ genero.getHeight()+5,50,25);;
        panelCenter.add(precoText).setBounds(nomeText.getX(), genero.getY()+ genero.getHeight()+5,190,25);

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
         String escolhaGenero = (String) dropdown.getSelectedItem();
         String escolhaNome = nomeText.getText();
         //
         dispose(); // Fecha o pop-up.
        }

    }
}