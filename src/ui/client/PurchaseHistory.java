package ui.client;

import data.Cliente;
import data.Playlist;
import data.Purchase;
import ui.RockstarGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PurchaseHistory extends JPanel implements ActionListener {

    public static final String TITLE = "PurchaseHistory";

    private RockstarGUI gui;
    private final Cliente client;
    private JPanel topPanel;
    private JPanel eastPanel;
    private JTable purchaseTable;
    private DefaultTableModel tableModel;
    private JButton seePurchase;
    private JLabel panelTitle;

    /**
     * Painel com todas as compras realizadas pelo cliente, onde é
     * possível escolher uma compra e ver os seus detalhes.
     * @param gui
     */
    public PurchaseHistory(RockstarGUI gui) {

        this.gui = gui;
        client =  (Cliente) gui.getDb().getCurrentUser();

        setLayout(new BorderLayout());
        setBackground(new Color(20, 64, 88));

        ///////////Painel Superior\\\\\\\\\\\\\\\\\\\\\\\\\\\
        topPanel = new JPanel();
        topPanel.setBackground(new Color(20, 64, 88));
        topPanel.setPreferredSize(new Dimension(0, 40));
        topPanel.setLayout(null);

        //Titulo do Painel
        panelTitle = new JLabel();
        panelTitle.setText("Histórico de Compras");
        panelTitle.setFont(new Font("Arial", Font.BOLD, 22));
        panelTitle.setForeground(new Color(198,107,61));
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

        tableModel.addColumn("Compra");
        tableModel.addColumn("Data");
        tableModel.addColumn("Custo total");

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

        //botão para criar nova playlist vazia
        seePurchase = new JButton();
        seePurchase.setText("Ver");
        seePurchase.setBounds(0, 150, 120, 35);
        seePurchase.setFocusable(false);
        seePurchase.addActionListener(this);


        eastPanel.add(seePurchase);

        add(eastPanel, BorderLayout.EAST);

        gui.getDb().addAllPurchasesToTable(purchaseTable);
    }

    public JTable getPurchaseTable() {
        return purchaseTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == seePurchase) {
            int selectedRow = purchaseTable.getSelectedRow();
            if (selectedRow != -1) {

                int modelRow = purchaseTable.convertRowIndexToModel(selectedRow);
                Purchase selectedPurchase = client.getPurchasesMade().get(modelRow);

                gui.getClientFrame().getPurchaseDetails().setPurchase(selectedPurchase);
                gui.showPurchaseDetails();

            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma compra para visualizar.");
            }
        }
    }
}
