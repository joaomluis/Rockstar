package ui.client.popups;

import data.Music;
import ui.RockstarGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PriceHistory extends JDialog {

    private JTable priceTable;
    DefaultTableModel tableModel;
    private JLabel musicTitle;
    private Music selectedMusic;
    private RockstarGUI gui;

    public PriceHistory(RockstarGUI gui, JFrame parent, Music music) {

        super(parent, "Histórico de preços", true);

        this.selectedMusic = music;
        this.gui = gui;

        setSize(550, 350);
        setLayout(new BorderLayout());
        setResizable(false);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("Data atribuição");
        tableModel.addColumn("Preço");

        priceTable = new JTable(tableModel);
        priceTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        priceTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        priceTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(priceTable);

        scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Define as margens
        scrollPane.setBackground(new Color(20, 64, 88));

        add(scrollPane);
        setLocationRelativeTo(parent);

        gui.getDb().addPriceHistoryToTable(selectedMusic, priceTable);
        setVisible(true);
    }
}
