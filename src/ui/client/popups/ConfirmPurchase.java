package ui.client.popups;

import ui.RockstarGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Pop-up para confirmar a compra de uma música.
 */
public class ConfirmPurchase extends JDialog implements ActionListener {

    private JPanel panelCenter;
    private JPanel panelSouth;
    private JLabel addBalanceLabel;
    private JButton okButton;
    private JButton cancelButton;
    private RockstarGUI gui;
    /**
     * Construtor para o pop-up de confirmação de compra.
     * @param gui     A instância da interface gráfica principal da aplicação.
     * @param parent  O JFrame pai associado ao pop-up.
     */
    public ConfirmPurchase(RockstarGUI gui, JFrame parent) {

        super(parent, "Confirmar compra", true);
        this.gui = gui;

        ////Especificações da janela\\\\\
        setSize(400, 150);
        setLayout(new BorderLayout());
        setResizable(false);

        ///////Painel central\\\\\\\\\
        panelCenter = new JPanel(null);

        addBalanceLabel = new JLabel();
        addBalanceLabel.setText("Comprar musica x por x euros?");
        addBalanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        addBalanceLabel.setBounds(45, 20, 270, 25);

        panelCenter.add(addBalanceLabel);


        //////Painel sul\\\\\\\\\\\
        panelSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));

        okButton = new JButton();
        okButton.setText("Confirmar");
        okButton.setFocusable(false);
        okButton.addActionListener(this);

        cancelButton = new JButton();
        cancelButton.setText("Cancelar");
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(this);

        panelSouth.add(okButton);
        panelSouth.add(cancelButton);

        /////////Painel Principal\\\\\\\\\\\\\

        add(panelCenter, BorderLayout.CENTER);
        add(panelSouth, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            dispose();
        }
        DefaultTableModel modeloCarrinho = gui.getCartTableModel();
        JTable tabelaCarrinho = gui.getCartTable();

        if (e.getSource() == okButton) {
            int selectedRow = tabelaCarrinho.getSelectedRow();
            System.out.println( "tamanho da array é " + gui.getDb().getCurrentUserAsClient().getSongsInCart().size());
            System.out.println(selectedRow);
            if (selectedRow != -1) {
                gui.updateCartTable(modeloCarrinho, tabelaCarrinho);
                //gui.getDb().convertToPurchase(selectedRow);
                gui.updateCartTable(modeloCarrinho, tabelaCarrinho);
            }
        }
    }
}
