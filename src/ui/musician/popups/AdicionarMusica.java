package ui.musician.popups;

import data.Album;
import data.Music;
import data.Musico;
import domain.RockStarDBStatus;
import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Esta classe representa um pop-up para adicionar uma música.
 * Permite que um músico adicione uma nova música ao seu Álbum.
 * Os campos necessários incluem nome, género, preço e álbum associado à música.
 */
public class AdicionarMusica extends JDialog implements ActionListener{
    private JComboBox albumDropdown;
    private JLabel album;
    private RockstarGUI gui;
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
    private int height = 200;
    /**
     * Construtor da classe AdicionarMusica.
     * @param gui A instância da classe RockstarGUI.
     * @param parent O JFrame pai.
     */
    public AdicionarMusica(RockstarGUI gui, JFrame parent) {
        super(parent, "Adicionar Música", true);
        this.gui = gui;
//SETTINGS DA JANELA////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        setSize(width,height);
        setLayout(new BorderLayout());
        setResizable(false);
//CRIAR BOTÕES//////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        dropdown = new JComboBox<>(gui.getDb().getMusicGenrs()); //vetor de generos existentes
        nomeText = new JTextField();
        precoText = new JTextField();
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancelar");
        nome = new JLabel("Nome:");
        genero = new JLabel("Género:");
        preco = new JLabel("Preço:");
        album = new JLabel("Album:");
        albumDropdown = new JComboBox<>(gui.getDb().getMusicianAlbums(gui.getDb().getCurrentUserAsMusician()));//vetor com os albuns criados pelo musico

//(PANEL CENTER) CAMPOS LABEL/DROPDOWN/TEXT/////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        panelCenter = new JPanel(null); //Cria painel central

        panelCenter.add(nome).setBounds(80,10,50,25);
        panelCenter.add(nomeText).setBounds(nome.getX()+nome.getWidth()+5,nome.getY(),190,25);
        panelCenter.add(genero).setBounds(nome.getX(), nomeText.getY()+ nomeText.getHeight()+5,50,25);
        panelCenter.add(dropdown).setBounds(nomeText.getX(),nome.getY()+nome.getHeight()+5,190,25);
        panelCenter.add(preco).setBounds(nome.getX(), genero.getY()+ genero.getHeight()+5,50,25);;
        panelCenter.add(precoText).setBounds(nomeText.getX(), genero.getY()+ genero.getHeight()+5,190,25);
        panelCenter.add(album).setBounds(nome.getX(),preco.getY()+preco.getHeight()+5,190,25);
        panelCenter.add(albumDropdown).setBounds(nomeText.getX(),preco.getY()+preco.getHeight()+5,190,25);

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
            String escolhaPreco = precoText.getText();
            Album album = (Album) albumDropdown.getSelectedItem();

            //Verificar se há erros e distinguir.
            RockStarDBStatus db = gui.getDb().adicionarMusica(escolhaGenero,escolhaNome,escolhaPreco,album);

            if(db==RockStarDBStatus.DB_MUSIC_NAME_EMPTY){
                JOptionPane.showMessageDialog(null, "A sua música tem de ter um nome. Por exemplo: Música.");
            }else if(db==RockStarDBStatus.DB_INCORRET_FORMAT_NUMBER) {
                JOptionPane.showMessageDialog(null, "A sua música tem de ter um preço. Por exemplo: 0.00 .");
            }else if(db==RockStarDBStatus.DB_MUSIC_NAME_FAILED){
                JOptionPane.showMessageDialog(null, "O nome da música já existe.");
            }else if(db==RockStarDBStatus.DB_MUSIC_ADDED){
                JOptionPane.showMessageDialog(null, "A sua música "+escolhaNome+" foi adicionada com sucesso.");
            }
            dispose();// Fecha o pop-up.
        }
    }
}