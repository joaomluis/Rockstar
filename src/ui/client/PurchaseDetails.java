package ui.client;

import data.Cliente;
import data.Music;
import data.Purchase;
import ui.RockstarGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Painel onde vão ser disponiblizados detalhes sobre a compra selecioanda
 */
public class PurchaseDetails extends JPanel {

    public static final String TITLE = "PurchaseDetails";

    private RockstarGUI gui;
    private final Cliente client;
    private JPanel topPanel;
    private JTable purchaseTable;
    private DefaultTableModel tableModel;
    private JLabel panelTitle;
    private Purchase purchase;

    public PurchaseDetails(RockstarGUI gui) {

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

        tableModel.addColumn("Música");
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


    }
    public void setPurchase(Purchase selectedPurchase) {
        if (selectedPurchase != null) {
            this.purchase = selectedPurchase;
            panelTitle.setText(purchase.getPurchaseId());

            // Limpa a tabela
            tableModel.setRowCount(0);

            // Adiciona as músicas da playlist à tabela
            for (Music music : selectedPurchase.getSongList()) {
                Object[] rowData = {music.getTitle(), music.getArtist(),  String.format("%1$,.2f€", music.getPreco())};
                tableModel.addRow(rowData);
            }
        }
    }



}
