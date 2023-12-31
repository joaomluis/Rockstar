package ui.client;

import data.Cliente;
import domain.RockStarDBStatus;
import ui.RockstarGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Painel com o carrinho de compras, onde as músicas selecionadas na loja ficam
 * a aguardar confirmação para serem tornadas em objeto compra e passarem para
 * as músicas do cliente
 */
public class ShoppingCart extends JPanel implements ActionListener {

    public static final String TITLE = "ShoppingCart";
    private RockstarGUI gui;
    private final Cliente client;
    private JPanel topPanel;
    private JPanel eastPanel;
    private JTable purchaseTable;
    private DefaultTableModel tableModel;
    private JButton confirmPurchase;
    private JButton removePurchase;
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

        //botão para confirmar a compra
        confirmPurchase = new JButton();
        confirmPurchase.setText("Comprar"); //colocar um pop up a dizer o preço total e confirmar compra?
        confirmPurchase.setBounds(0, 150, 120, 35);
        confirmPurchase.setFocusable(false);
        confirmPurchase.addActionListener(this);

        //botão para remover musica da lista
        removePurchase = new JButton();
        removePurchase.setText("Remover");
        removePurchase.setBounds(0, confirmPurchase.getY() + 50, 120, 35);
        removePurchase.setFocusable(false);
        removePurchase.addActionListener(this);

        eastPanel.add(confirmPurchase);
        eastPanel.add(removePurchase);

        add(eastPanel, BorderLayout.EAST);

        gui.getDb().addAllSongsInCartToTable(purchaseTable);

        gui.updateCartTable(tableModel, purchaseTable);
    }

    public JTable getPurchaseTable() {
        return purchaseTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    /**
     * Atualiza a tabela para aparecem todas as musicas que foram adicionadas ao carrinho
     */
//    public void atualizarTabelaMusicasCarrinho() {
//        tableModel.setRowCount(0); // Limpa a tabela
//        gui.getDb().addAllSongsInCartToTable(purchaseTable); // Atualiza a tabela com as songs atualizadas
//    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        int selectedRow = purchaseTable.getSelectedRow();
        if (e.getSource() == confirmPurchase) {

            RockStarDBStatus status = gui.getDb().buyAllSongsFromCart();

            if (status == RockStarDBStatus.DB_INSUFFICIENT_BALANCE) {
                JOptionPane.showMessageDialog(null, "Saldo insuficiente.");
            } else if (status == RockStarDBStatus.DB_SONGS_PURCHASED_SUCCESSFULLY) {

                JOptionPane.showMessageDialog(null, "Compra realizada com sucesso.");
            } else if (status == RockStarDBStatus.DB_CART_EMPTY) {
                JOptionPane.showMessageDialog(null, "Carrinho vazio, adicione uma música.");
            }

            gui.updateCartTable(tableModel, purchaseTable);
            gui.updateBalance();
            //new ConfirmPurchase(gui, parent);

        }
        if (e.getSource() == removePurchase) {
            if (selectedRow != -1) {
                // Remove da tabela
                tableModel.removeRow(selectedRow);
                gui.getDb().getCurrentUserAsClient().getSongsInCart().remove(selectedRow);
                gui.getDb().saveCurrentUser();
                confirmPurchase.setEnabled(true); // faz com que o botão de avaliar não fique disabled após remover uma música
                gui.updateCartTable(tableModel, purchaseTable);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma música para remover do carrinho.");
            }
        }
    }
}
