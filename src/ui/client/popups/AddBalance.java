package ui.client.popups;

import data.Cliente;
import ui.RockstarGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * Janela pop-up para adicionar saldo à conta do cliente.
 */
public class AddBalance extends JDialog {

    private JPanel panelCenter;
    private JPanel panelSouth;
    private JLabel addBalanceLabel;
    private JTextField balanceField;
    private JButton okButton;
    private JButton cancelButton;

    /**
     * Construtor para o pop-up de adição de saldo.
     * @param gui    A instância da interface gráfica principal da aplicação.
     * @param parent O JFrame pai associado à janela pop-up.
     */
    public AddBalance(RockstarGUI gui, JFrame parent) {
        super(parent, "Adicionar Saldo", true);

        ////Especificações da janela\\\\\
        setSize(400, 150);
        setLayout(new BorderLayout());
        setResizable(false);

        ///////Painel central\\\\\\\\\
        panelCenter = new JPanel(null);

        addBalanceLabel = new JLabel();
        addBalanceLabel.setText("Carregar");
        addBalanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        addBalanceLabel.setBounds(80,20,100,25);

        balanceField = new JTextField();
        balanceField.setBounds(addBalanceLabel.getX() + 120, addBalanceLabel.getY(), 80, 25);


        panelCenter.add(addBalanceLabel);
        panelCenter.add(balanceField);

        //////Painel sul\\\\\\\\\\\
        panelSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));

        okButton = new JButton();
        okButton.setText("Ok");
        okButton.setFocusable(false);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == okButton) {
                    String input = balanceField.getText();

                    if (!input.isEmpty()) {
                        try {
                            double valor = Double.parseDouble(input);
                            Cliente cliente = gui.getDb().getCurrentUserAsClient(); // vai buscar a GUI que vai buscar a DB que tem acesso aos dados

                            cliente.adicionaSaldo(valor);
                            gui.getDb().saveCurrentUser(); // outra vez ir buscar GUI para a DB e guarda a operação
                            gui.updateBalance(); // só atualiza a label

                            dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(parent, "Por favor, insira um valor numérico válido.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(parent, "O campo está vazio, insira um valor.");
                    }
                }
            }
        });

        cancelButton = new JButton();
        cancelButton.setText("Cancelar");
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        panelSouth.add(okButton);
        panelSouth.add(cancelButton);

        /////////Painel Principal\\\\\\\\\\\\\

        add(panelCenter, BorderLayout.CENTER);
        add(panelSouth, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == cancelButton) {
//            dispose();
//        }
//    }
}
