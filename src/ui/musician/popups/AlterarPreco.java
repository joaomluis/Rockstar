package ui.musician.popups;

import data.Music;
import domain.RockStarDBStatus;
import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static domain.RockStarDBStatus.DB_MUSIC_PRICE_HAS_CHANGED;
/**
 * Esta classe representa um pop-up para alterar o preço de uma música.
 * Permite que um músico modifique o preço de uma música da sua lista.
 */
public class AlterarPreco extends JDialog implements ActionListener{
    private JPanel panelCenter;
    private JPanel panelSouth;
    private JLabel preco;
    private JTextField textField;
    private JButton okButton;
    private JButton cancelButton;
    private int width = 300;
    private int height = 150;
    private Music music;
    private RockstarGUI gui;
    /**
     * Construtor da classe.
     *
     * @param gui    Referência para a instância do RockstarGUI.
     * @param parent JFrame pai associado ao pop-up.
     * @param music  Referência para a música cujo preço será alterado.
     */
    public AlterarPreco(RockstarGUI gui, JFrame parent, Music music) {
        super(parent, "Alterar Preço", true);
        this.music = music;
        this.gui = gui;
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
        preco = new JLabel("Novo preço:");

//(PANEL CENTER) CAMPOS LABEL/DROPDOWN/TEXT/////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        panelCenter = new JPanel(null); //Cria painel central

        panelCenter.add(preco).setBounds(20,10,90,25);
        panelCenter.add(textField).setBounds(preco.getX()+ preco.getWidth(), preco.getY(),170,25);
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
            String escolhaPreco = textField.getText();
            if (music != null) {
                RockStarDBStatus test = gui.getDb().alterarPreco(escolhaPreco, music);
                if (test == DB_MUSIC_PRICE_HAS_CHANGED) { // O preço da música foi atualizado com sucesso
                    JOptionPane.showMessageDialog(null, "O nome da música foi atualizado para: " + escolhaPreco);
                } else {// A alteração do preço falhou
                    JOptionPane.showMessageDialog(null, "Operação incálida. Deverá inserir apenas o novo valor da Música.");
                }
            }
        }
        dispose(); // Fecha o pop-up.
    }
}