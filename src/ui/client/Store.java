package ui.client;

import data.Cliente;
import data.Music;
import domain.RockStarDBStatus;
import ui.RockstarGUI;
import ui.client.popups.AddBalance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Store extends JPanel implements ActionListener {

    public static final String TITLE = "Store";
    private RockstarGUI gui;

    private final Cliente client;
    private JPanel topPanel;
    private JPanel eastPanel;
    private JTable storeTable;
    private DefaultTableModel tableModel;
    private JButton buySong;
    private JButton addBalance;
    private JLabel panelTitle;
    private JTextField barraPesquisa;
    private JComboBox<String> dropdown;
    private JButton pesquisar;

    public Store(RockstarGUI gui) {
        this.gui = gui;
        client = (Cliente) gui.getDb().getCurrentUser();

        setLayout(new BorderLayout());
        setBackground(new Color(20, 64, 88));


        ///////////Painel Superior\\\\\\\\\\\\\\\\\\\\\\\\\\\
        topPanel = new JPanel();
        topPanel.setBackground(new Color(20, 64, 88));
        topPanel.setPreferredSize(new Dimension(0, 95));
        topPanel.setLayout(null);

        //Titulo do Painel
        panelTitle = new JLabel();
        panelTitle.setText("Loja");
        panelTitle.setFont(new Font("Arial", Font.BOLD, 22));
        panelTitle.setForeground(new Color(198, 107, 61));
        panelTitle.setBounds(275, 5, 250, 30);

        //Dropdown
        dropdown = new JComboBox<>(new String[]{"Nome", "Género"});
        dropdown.setBounds(30, panelTitle.getY() + 45, 200, 35);

        // Barra pesquisa
        barraPesquisa = new JTextField();
        barraPesquisa.setBounds(dropdown.getX()+ 225,dropdown.getY(),240,35);
        // botão pesquisar
        pesquisar = new JButton();
        pesquisar.setText("Pesquisar");
        pesquisar.setFocusable(false);
        pesquisar.setBounds(barraPesquisa.getX() + 280, barraPesquisa.getY(), 120, 35);


        topPanel.add(panelTitle);
        topPanel.add(dropdown);
        topPanel.add(barraPesquisa);
        topPanel.add(pesquisar);

        add(topPanel, BorderLayout.NORTH);

        ///////////Painel Central\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("Titulo");
        tableModel.addColumn("Artista");
        tableModel.addColumn("Género");
        tableModel.addColumn("Preço");

        //adiciona as musicas da array list à table, tem que ser trocado por um método mais tarde


        storeTable = new JTable(tableModel);
        storeTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        storeTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        storeTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        storeTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        // Impede a movimentação das colunas.
        storeTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(storeTable);

        // ADD scroll ao Panel
        scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Define as margens
        scrollPane.setBackground(new Color(20, 64, 88));

        add(scrollPane, BorderLayout.CENTER);

        ///////// Painel Este \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        eastPanel = new JPanel();
        eastPanel.setBackground(new Color(20, 64, 88));
        eastPanel.setPreferredSize(new Dimension(150, 0));
        eastPanel.setLayout(null);

        //botão para comprar músicas
        buySong = new JButton();
        buySong.setText("Add Carrinho");
        buySong.setBounds(0, 150, 120, 35);
        buySong.setFocusable(false);
        buySong.addActionListener(this);

        //botão para adicionar saldo
        addBalance = new JButton();
        addBalance.setText("Add Saldo");
        addBalance.setBounds(0, buySong.getY() + 50, 120, 35);
        addBalance.setFocusable(false);
        addBalance.addActionListener(this);

        eastPanel.add(buySong);
        eastPanel.add(addBalance);

        add(eastPanel, BorderLayout.EAST);

        gui.getDb().addAllRockstarSongsToTable(storeTable);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buySong) {
            int selectedRow = storeTable.getSelectedRow();
            if (selectedRow != -1) {

                int modelRow = storeTable.convertRowIndexToModel(selectedRow);
                Music musicaSelecionada = gui.getDb().getDados().getSongs().get(modelRow);

                RockStarDBStatus status = gui.getDb().addSongToCart(musicaSelecionada);
                if (status == RockStarDBStatus.DB_SONG_ALREADY_IN_CART) {
                    JOptionPane.showMessageDialog(null, "Já adicionou essa música ao carrinho");
                } else if (status == RockStarDBStatus.DB_SONG_ADDED_TO_CART) {
                    gui.getDb().getCurrentUserAsClient().getSongsInCart().add(musicaSelecionada);
                    gui.getDb().saveCurrentUser();
                    JOptionPane.showMessageDialog(null, "Música adicionada ao carrinho com sucesso");
                } else if (status == RockStarDBStatus.DB_SONG_ALREADY_BOUGHT){
                    JOptionPane.showMessageDialog(null, "Música já foi comprada");
                }

                gui.updateCartTable(gui.getCartTableModel(), gui.getCartTable()); //atualiza tabela do cart

            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma música para comprar.");
            }
        } else if (e.getSource() == addBalance) {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new AddBalance(gui, parent);
        }
    }
}

