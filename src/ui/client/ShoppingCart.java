package ui.client;

import data.Cliente;
import ui.RockstarGUI;
import ui.client.popups.ConfirmPurchase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ShoppingCart extends JPanel implements ActionListener {

    public static final String TITLE = "ShoppingCart";
    private RockstarGUI gui;
    private final Cliente client;
    private JPanel topPanel;
    private JPanel eastPanel;
    private JTable purchaseTable;
    private DefaultTableModel tableModel;
    private JButton confirmPurchase;
    private JLabel panelTitle;


    public ShoppingCart(RockstarGUI gui) {

        this.gui = gui;
        client = (Cliente) gui.getDb().getCurrentUser();

        setLayout(new BorderLayout());
        setBackground(new Color(20, 64, 88));


        ///////////Painel Superior\\\\\\\\\\\\\\\\\\\\\\\\\\\
        topPanel = new JPanel();
        topPanel.setBackground(new Color(20, 64, 88));
        topPanel.setPreferredSize(new Dimension(0, 40));
        topPanel.setLayout(null);

        //Titulo do Painel
        panelTitle = new JLabel();
        panelTitle.setText("Carrinho de compras");
        panelTitle.setFont(new Font("Arial", Font.BOLD, 22));
        panelTitle.setForeground(new Color(198, 107, 61));
        panelTitle.setBounds(250, 5, 250, 30);

        topPanel.add(panelTitle);

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
        tableModel.addColumn("Preço");

        purchaseTable = new JTable(tableModel);
        purchaseTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        purchaseTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        purchaseTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        // Impede a movimentação das colunas.
        purchaseTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(purchaseTable);

        // ADD scroll ao Panel
        scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Define as margens
        scrollPane.setBackground(new Color(20, 64, 88));

        add(scrollPane, BorderLayout.CENTER);

        ///////// Painel Este \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        eastPanel = new JPanel();
        eastPanel.setBackground(new Color(20, 64, 88));
        eastPanel.setPreferredSize(new Dimension(150, 0));
        eastPanel.setLayout(null);

        //botão de remover músicas das adquiridas
        confirmPurchase = new JButton();
        confirmPurchase.setText("Comprar"); //colocar um pop up a dizer o preço total e confirmar compra?
        confirmPurchase.setBounds(0, 150, 120, 35);
        confirmPurchase.setFocusable(false);
        confirmPurchase.addActionListener(this);

        eastPanel.add(confirmPurchase);

        add(eastPanel, BorderLayout.EAST);

        gui.getDb().addAllSongsInCartToTable(purchaseTable);

        System.out.println(gui.getDb().getCurrentUserAsClient().getSongsInCart().size());
        atualizarTabelaMusicasCarrinho();
    }

    /**
     * Atualiza a tabela para aparecem todas as musicas que foram adicionadas ao carrinho
     */
    public void atualizarTabelaMusicasCarrinho() {
        tableModel.setRowCount(0); // Limpa a tabela
        gui.getDb().addAllSongsInCartToTable(purchaseTable); // Atualiza a tabela com as songs atualizadas
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (e.getSource() == confirmPurchase) {
            new ConfirmPurchase(gui, parent);
        }
    }
}
