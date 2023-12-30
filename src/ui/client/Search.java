package ui.client;



import data.Cliente;
import data.Musico;
import ui.RockstarGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Search extends JPanel implements ActionListener {

    public static final String TITLE = "ClientSearch";

    private RockstarGUI gui;
    private final Cliente client;
    private JTextField barraPesquisa;
    private JComboBox<String> dropdown;
    private JPanel painelCentralSuperior;
    private JPanel painelCentral;
    private JPanel painelEast;
    private JScrollPane scrollPane;
    private JPanel painelSuperior;
    private DefaultTableModel tabelaDefault;
    private JTable tabela;
    private JButton pesquisar;


    public Search(RockstarGUI gui) {
        this.gui = gui;
        client = (Cliente) gui.getDb().getCurrentUser();
        setLayout(new BorderLayout());


//        ////////////////////////////////////////PAINEL SUPERIOR////////////////////////////////////////////////////////
        painelSuperior = new JPanel(null); // Inicializa o painel superior
        painelSuperior.setBackground(new Color(20, 64, 88));
        painelSuperior.setPreferredSize(new Dimension(0, 40)); //Altura do painel Superior

        //Criar elementos Painel superior
        JLabel titulo = new JLabel("Pesquisa");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(new Color(198,107,61));
        painelSuperior.add(titulo).setBounds(250, 5, 250, 30);
        add(painelSuperior, BorderLayout.NORTH);

        ////////////////////////////////////////PAINEL CENTRAL////////////////////////////////////////////////////////
        // Impede alterações na tabela
        painelCentral = new JPanel(new BorderLayout());
        painelCentralSuperior = new JPanel(null);
        painelCentralSuperior.setPreferredSize(new Dimension(0, 50));
        painelCentralSuperior.setBackground(new Color(20, 64, 88));
        dropdown = new JComboBox<>(new String[]{"Nome", "Género"});
        dropdown.addActionListener(this);
        barraPesquisa = new JTextField();
        dropdown.addActionListener(this);
        painelCentralSuperior.add(dropdown).setBounds(30,0,200,35);
        painelCentralSuperior.add(barraPesquisa).setBounds(dropdown.getX()+dropdown.getWidth()+15,0,280,35);

        tabelaDefault = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Define as colunas da tabela
        tabelaDefault.addColumn("Titulo");
        tabelaDefault.addColumn("Género");
        tabelaDefault.addColumn("Produtor");

//        // Adiciona as músicas ao modelo de tabela
//        for (Musica musica : musicas) {
//            Object[] musicaObjeto = {musica.getTittle(), musica.getArtist()}; //falta preço
//            tabelaDefault.addRow(musicaObjeto);
//        }

        // Cria a tabela com o modelo
        tabela = new JTable(tabelaDefault);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(200);
        // Impede a movimentação das colunas.
        tabela.getTableHeader().setReorderingAllowed(false);
        // SCROLL
        scrollPane = new JScrollPane(tabela);
        // ADD scroll ao Panel
        scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Define as margens
        scrollPane.setBackground(new Color(20, 64, 88));

        painelCentral.add(scrollPane, BorderLayout.CENTER);
        painelCentral.add(painelCentralSuperior, BorderLayout.NORTH);
        add(painelCentral,BorderLayout.CENTER);

        ////////////////////////////////////////PAINEL EAST////////////////////////////////////////////////////////
        painelEast = new JPanel(null);    //Inicializa o painel central
        painelEast.setPreferredSize(new Dimension(150, 0));

        //Criar elementos Painel EAST
        pesquisar = new JButton("Pesquisar");
        pesquisar.setFocusable(false);
        pesquisar.addActionListener(this);  //adicionar o botão ao actionListener
        //Add elementos ao Painel Central
        painelEast.add(pesquisar).setBounds(0,0,120,35);
        painelEast.setBackground(new Color(20, 64, 88));
        add(painelEast, BorderLayout.EAST);

        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == pesquisar) {
            String catgoriaAPesquisar = (String) dropdown.getSelectedItem();
            String strAPesquisar = barraPesquisa.getText();
        }
    }
}



