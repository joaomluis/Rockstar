package ui.client;

import data.Cliente;
import data.Music;
import domain.RockStarDBStatus;
import ui.RockstarGUI;
import ui.client.popups.AddBalance;
import ui.client.popups.PriceHistory;
import ui.musician.CriteriosMusica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Painel onde estão todas as músicas com disponibilidade true e que com o cliente
 * pode escolher para comprar. Aqui também é possível carregar o saldo do cliente.
 */
public class Store extends JPanel implements ActionListener, MouseListener {

    public static final String TITLE = "Store";
    private RockstarGUI gui;

    private final Cliente client;
    private JPanel topPanel;
    private JPanel eastPanel;
    private JTable storeTable;
    private DefaultTableModel tableModel;
    private JButton buySong;
    private JButton addBalance;
    private JButton seePriceHistory;
    private JLabel panelTitle;
    private JTextField barraPesquisa;
    private JComboBox<CriteriosMusica> dropdown;
    private JButton pesquisar;
    private ArrayList<Music> musics;

    public Store(RockstarGUI gui) {
        this.gui = gui;
        client = (Cliente) gui.getDb().getCurrentUser();
        this.musics = gui.getDb().addAllRockstarSongsVisible(); //inicia as musics com o array de sons disponiveis no momento.

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
        CriteriosMusica[] itemsToShow = {CriteriosMusica.Nome, CriteriosMusica.Genero};
        dropdown = new JComboBox<>(itemsToShow);
        dropdown.setBounds(30, panelTitle.getY() + 45, 200, 35);

        // Barra pesquisa
        barraPesquisa = new JTextField();
        barraPesquisa.setBounds(dropdown.getX()+ 225,dropdown.getY(),240,35);
        // botão pesquisar
        pesquisar = new JButton();
        pesquisar.setText("Pesquisar");
        pesquisar.setFocusable(false);
        pesquisar.setBounds(barraPesquisa.getX() + 280, barraPesquisa.getY(), 120, 35);
        pesquisar.addActionListener(this);

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
        tableModel.addColumn("Rating");

        //adiciona as musicas da array list à table, tem que ser trocado por um método mais tarde


        storeTable = new JTable(tableModel);
        storeTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        storeTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        storeTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        storeTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        storeTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        // Impede a movimentação das colunas.
        storeTable.getTableHeader().setReorderingAllowed(false);
        storeTable.getTableHeader().addMouseListener(this);

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
        buySong.setText("<html><div style='display: table-cell; vertical-align: middle; text-align: center;'>Adicionar<br/>Carrinho</html>");
        buySong.setBounds(0, 100, 120, 45);
        buySong.setFocusable(false);
        buySong.addActionListener(this);

        //botão para adicionar saldo
        addBalance = new JButton();
        addBalance.setText("<html><div style='display: table-cell; vertical-align: middle; text-align: center;'>Adicionar<br/>Saldo</html>");
        addBalance.setBounds(0, buySong.getY() + 60, 120, 45);
        addBalance.setFocusable(false);
        addBalance.addActionListener(this);

        //botão para ver historico de preços
        seePriceHistory = new JButton();
        seePriceHistory.setText("<html><div style='display: table-cell; vertical-align: middle; text-align: center;'>Histórico<br/>Preços</html>");
        seePriceHistory.setBounds(0, addBalance.getY() + 60, 120, 45);
        seePriceHistory.setFocusable(false);
        seePriceHistory.addActionListener(this);



        eastPanel.add(buySong);
        eastPanel.add(addBalance);
        eastPanel.add(seePriceHistory);

        add(eastPanel, BorderLayout.EAST);

        gui.getDb().addAllRockstarSongsToTable(storeTable,musics);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

        if (e.getSource() == buySong) {
            int selectedRow = storeTable.getSelectedRow();
            if (selectedRow != -1) {

                int modelRow = storeTable.convertRowIndexToModel(selectedRow);
                Music musicaSelecionada = musics.get(modelRow);

                RockStarDBStatus status = gui.getDb().addSongToCart(musicaSelecionada);

                if (status == RockStarDBStatus.DB_SONG_ALREADY_IN_CART) {
                    JOptionPane.showMessageDialog(null, "Já adicionou essa música ao carrinho");
                } else if (status == RockStarDBStatus.DB_SONG_ADDED_TO_CART) {
                    JOptionPane.showMessageDialog(null, "Música adicionada ao carrinho com sucesso");
                } else if (status == RockStarDBStatus.DB_SONG_ALREADY_BOUGHT){
                    JOptionPane.showMessageDialog(null, "Música já foi comprada");
                }

                gui.updateCartTable(gui.getCartTableModel(), gui.getCartTable()); //atualiza tabela do cart

            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma música para comprar.");
            }
        } else if (e.getSource() == addBalance) {
            new AddBalance(gui, parent);
        } else if(e.getSource() == pesquisar){
            CriteriosMusica mo = (CriteriosMusica) dropdown.getSelectedItem();
            String pesquisa = barraPesquisa.getText();

            musics = gui.getDb().procurarMusicas(pesquisa,mo); //atualizar a array com as músicas pesquisadas

            atualizarTabelaMusicas(musics);
        }
        if (e.getSource() == seePriceHistory) {
            int selectedRow = storeTable.getSelectedRow();
            if (selectedRow != -1) {

                Music musicaSelecionada = musics.get(selectedRow);
                new PriceHistory(gui ,parent, musicaSelecionada);

            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma música para verificar o histórico.");
            }
        }
    }
    public void atualizarTabelaMusicas(ArrayList<Music> musicasEncontradas) {
        // Limpar a tabela atual
        tableModel.setRowCount(0);

        String rating = "";
        // Adicionar as músicas encontradas à tabela
        for (Music musica : musicasEncontradas) {
            if (musica.avaliacaoMedia() == 0) {
                rating = "Sem Rating";
            } else {
                rating = String.valueOf(musica.avaliacaoMedia());
            }
            Object[] rowData = {musica.getTitle(), musica.getArtist(), musica.getGenre(), String.format("%1$,.2f€", musica.getPreco()), rating};
            tableModel.addRow(rowData);
        }
    }

    public JTable getStoreTable() {
        return storeTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setMusics(ArrayList<Music> musics) {
        this.musics = musics;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 1) {
            if (e.getSource() == storeTable.getTableHeader()) {
                int columnIndex = storeTable.columnAtPoint(e.getPoint()); // Obtém o índice da coluna clicada
                if (columnIndex == 0) { // Verifica se o clique foi na primeira coluna (Título)
                    musics = gui.getDb().ordenarMusicasCrescente(CriteriosMusica.Nome, musics); // Ordena as músicas pelo título
                    atualizarTabelaMusicas(musics); // Atualiza a exibição da tabela com as músicas ordenadas
                } else if (columnIndex == 2) { // Verifica se o clique foi na primeira coluna (Genero)
                    musics = gui.getDb().ordenarMusicasCrescente(CriteriosMusica.Genero, musics); // Ordena as músicas pelo título
                    atualizarTabelaMusicas(musics); // Atualiza a exibição da tabela com as músicas ordenadas
                }
            }
        }else if(e.getClickCount() == 2) {
            if (e.getSource() == storeTable.getTableHeader()) {
                int columnIndex = storeTable.columnAtPoint(e.getPoint()); // Obtém o índice da coluna clicada
                if (columnIndex == 0) { // Verifica se o clique foi na primeira coluna (Título)
                    musics = gui.getDb().ordenarMusicasDecrescente(CriteriosMusica.Nome, musics); // Ordena as músicas pelo título
                    atualizarTabelaMusicas(musics); // Atualiza a exibição da tabela com as músicas ordenadas
                } else if (columnIndex == 2) { // Verifica se o clique foi na primeira coluna (Genero)
                    musics = gui.getDb().ordenarMusicasDecrescente(CriteriosMusica.Genero, musics); // Ordena as músicas pelo título
                    atualizarTabelaMusicas(musics); // Atualiza a exibição da tabela com as músicas ordenadas
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

