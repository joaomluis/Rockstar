package ui.client.popups;

import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmPurchase extends JDialog implements ActionListener {

    private JPanel panelCenter;
    private JPanel panelSouth;
    private JLabel addBalanceLabel;
    private JButton okButton;
    private JButton cancelButton;

    public ConfirmPurchase(RockstarGUI gui, JFrame parent) {

        super(parent, "Confirmar compra", true);

        ////Especificações da janela\\\\\
        setSize(400, 150);
        setLayout(new BorderLayout());
        setResizable(false);

        ///////Painel central\\\\\\\\\
        panelCenter = new JPanel(null);

        addBalanceLabel = new JLabel();
        addBalanceLabel.setText("O valor total da sua compra é: xxx.xx€");
        addBalanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        addBalanceLabel.setBounds(45,20,270,25);

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
    }
}
