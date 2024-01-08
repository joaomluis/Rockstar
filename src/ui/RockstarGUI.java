package ui;

import data.Cliente;
import data.Playlist;
import domain.RockstarDB;
import ui.auth.*;
import ui.client.*;
import ui.musician.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
/**
 * A classe RockstarGUI atua como o controlador principal na interface gráfica do usuário (GUI).
 * É responsável por gerir a interação entre os diferentes painéis e funcionalidades da aplicação Rockstar.
 */
public class RockstarGUI {
    private final RockstarDB db;
    private final AuthRootFrame authenticationFrame;
    private final ClientRootFrame clientFrame;
    private final MusicianRootFrame musicianFrame;

    public RockstarGUI(RockstarDB db) {
        this.db = db;
        authenticationFrame = new AuthRootFrame(this);
        clientFrame = new ClientRootFrame(this);
        musicianFrame = new MusicianRootFrame(this);
    }

    /**
     * Inicia a interface gráfica da aplicação Rockstar.
     */
    public void start() {
        authenticationFrame.start();
    }

    public RockstarDB getDb() {
        return db;
    }
    /**
     * Obtém a janela da lista de reprodução atual.
     * @return A janela da lista de reprodução atual.
     */
    public MyPlaylists getMyPlaylists() {
        return clientFrame.getMyPlaylists();
    }

    ////////////////////////////////////Atualizar painel carrinho\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    /**
     * Obtém o painel do carrinho de compras.
     * @return O painel do carrinho de compras.
     */
    public ShoppingCart getShoppingCartPanel() {
        return clientFrame.getShoppingCart();
    }
    /**
     * Obtém a tabela do carrinho de compras.
     * @return A tabela do carrinho de compras.
     */
    public JTable getCartTable() {
        return getShoppingCartPanel().getPurchaseTable();
    }
    /**
     * Obtém o modelo da tabela do carrinho de compras.
     * @return O modelo da tabela do carrinho de compras.
     */
    public DefaultTableModel getCartTableModel() {
        return getShoppingCartPanel().getTableModel();
    }
    /**
     * Atualiza a tabela do carrinho de compras com os dados atualizados das músicas no carrinho.
     * @param tableModel O modelo de tabela do carrinho de compras.
     * @param jTable A tabela do carrinho de compras a ser atualizada.
     */
    public void updateCartTable(DefaultTableModel tableModel, JTable jTable) {
        tableModel.setRowCount(0); // Limpa a tabela
        getDb().addAllSongsInCartToTable(jTable); // Atualiza a tabela com as songs atualizadas
    }
    public void updateMusicSong(DefaultTableModel tableModel, JTable jTable) {
        tableModel.setRowCount(0); // Limpa a tabela
        getDb().addAllSongsInCartToTable(jTable); // Atualiza a tabela com as songs atualizadas
    }

    ///////////////////////////////////Atualizar tabela painel My Music\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Obtém o painel "My Music".
     * @return O painel "My Music".
     */
    private MyMusic getMyMusicPanel() {
        return clientFrame.getMyMusic();
    }
    /**
     * Obtém a tabela da músicas do painel "My Music".
     * @return A tabela de músicas do painel "My Music".
     */
    private JTable getMyMusicTable() {
        return getMyMusicPanel().getMusicTable();
    }
    /**
     * Obtém o modelo da tabela do painel "My Music".
     * @return O modelo da tabela do painel "My Music".
     */
    private DefaultTableModel getMyMusicTableModel() {
        return getMyMusicPanel().getTableModel();
    }
    /**
     * Atualiza a tabela de músicas do painel "My Music" com as músicas compradas pelo utilizador.
     * @param tableModel O modelo de tabela do painel "My Music".
     * @param table A tabela de músicas do painel "My Music" a ser atualizada.
     */
    public void updateMyMusicTable (DefaultTableModel tableModel, JTable table) {
        tableModel.setRowCount(0);
        getDb().addAllOwnedSongsToTable(table);
    }

    /////////////////////Atualizar painel Historico Compras\\\\\\\\\\\\\\\\\\\\\\
    /**
     * Obtém o painel de histórico de compras.
     * @return O painel de histórico de compras.
     */
    private PurchaseHistory getPurchaseHistoryPanel() {
        return clientFrame.getPurchaseHistory();
    }
    /**
     * Obtém a tabela do histórico de compras.
     * @return A tabela do histórico de compras.
     */
    private JTable getPurchaseHistoryTable() {
        return getPurchaseHistoryPanel().getPurchaseTable();
    }
    /**
     * Obtém o modelo da tabela do histórico de compras.
     * @return O modelo da tabela do histórico de compras.
     */
    private DefaultTableModel getPurchaseHistoryTableModel() {
        return getPurchaseHistoryPanel().getTableModel();
    }
    /**
     * Atualiza a tabela do histórico de compras com os dados atualizados das compras.
     * @param tableModel O modelo de tabela do histórico de compras.
     * @param table      A tabela do histórico de compras a ser atualizada.
     */
    private void updatePurchaseTable (DefaultTableModel tableModel, JTable table) {
        tableModel.setRowCount(0);
        getDb().addAllPurchasesToTable(table);
    }


    //////////////////Atualizar painel Loja\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Obtém o painel da loja.
     * @return o painel da loja.
     */
    private Store getStorePanel() {
        return clientFrame.getStore();
    }

    /**
     * Obtém o modelo de tabela do painel de loja.
     * O modelo de tabela é responsável por armazenar os dados exibidos na tabela.
     * @return o modelo da tabela do painel de loja.
     */
    private DefaultTableModel getStoreTableModel() {
        return getStorePanel().getTableModel();
    }
    /**
     * Obtém tabela da loja no painel da interface gráfica do usuário.
     * @return A tabela da loja.
     */
    private JTable getStoreTable() {
        return getStorePanel().getStoreTable();
    }
    /**
     * Atualiza a tabela da loja com as músicas disponiveis no momento.
     * @param tableModel O modelo de tabela da loja.
     * @param table      A tabela do loja a ser atualizada.
     */
    private void updateStoreTable(DefaultTableModel tableModel, JTable table) {
        tableModel.setRowCount(0);
        getDb().addAllRockstarSongsToTable(table,getDb().addAllRockstarSongsVisible());
    }

    /////////////////Atualizar painel Playlists\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    /**
     * Adiciona elementos à tabela de playlists.
     * @param table A tabela onde os elementos serão adicionados.
     * @return true se os elementos foram adicionados com sucesso, false caso contrário.
     */
    public boolean adicionarElementosTabela(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Cliente cliente = getDb().getCurrentUserAsClient();
        List<Playlist> playlistCliente = cliente.getPlaylists();

        try {
            for (Playlist playlist : playlistCliente) {
                String visibilidade = "";
                if(playlist.isVisibilidade()) {
                    visibilidade = "Pública";
                } else {
                    visibilidade = "Privada";
                }

                Object[] row = {playlist.getNome(), visibilidade};
                if (!existePlaylistNaTabela(model, playlist)) {
                    model.addRow(row);
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    /**
     * Verifica se a playlist já existe na tabela.
     * @param model    O modelo da tabela.
     * @param playlist A playlist a ser verificada.
     * @return Verdadeiro se a playlist já existe na tabela, falso caso contrário.
     */
    private boolean existePlaylistNaTabela(DefaultTableModel model, Playlist playlist) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 0).equals(playlist.getNome())) {
                return true; // Playlist já existe na tabela
            }
        }
        return false; // Playlist não encontrada na tabela
    }
    /**
     * Atualiza a tabela de playlists.
     * @param tableModel O modelo da tabela a ser atualizada.
     * @param table      A tabela a ser atualizada com as playlists.
     */
    public void atualizarTabelaPlaylists(DefaultTableModel tableModel, JTable table) {
        tableModel.setRowCount(0); // Limpa a tabela
        adicionarElementosTabela(table); // Atualiza a tabela com as playlists atualizadas
    }
    /**
     * Obtém a tabela de playlists.
     * @return A tabela de playlists.
     */
    public JTable getMyPlaylistsTable() {
        return getMyPlaylists().getPlaylistTable();
    }
    /**
     * Obtém o modelo da tabela de playlists.
     * @return O modelo da tabela de playlists.
     */
    public DefaultTableModel getMyPlaylistsTableModel() {
        return getMyPlaylists().getTableModel();
    }
    /**
     * Ativa os botões na interface para gerir as playlists.
     */
    public void getMyPlaylistsButtonsToEnable() {
        getMyPlaylists().getCreatePlaylist().setEnabled(true);
        getMyPlaylists().getDeletePlaylist().setEnabled(true);
    }

    /////////////////////METODOS PARA ABRIR PAINEIS AUTENTICAÇÃO\\\\\\\\\\\\\\\\\\\\\
    /**
     * Exibe o painel do menu principal na interface de autenticação.
     */
    public void showMainMenu() {
          authenticationFrame.showPanel(MenuInicial.TITLE);
    }
    /**
     * Exibe o painel de registo de músico na interface de autenticação.
     */
    public void showMusicianRegister() {
        authenticationFrame.showPanel(RegistarMusico.TITLE);
    }
    /**
     * Exibe o painel de registo de cliente na interface de autenticação.
     */
    public void showClientRegister() {
        authenticationFrame.showPanel(RegistarCliente.TITLE);
    }
    /**
     * Exibe o painel de login de cliente na interface de autenticação.
     */
    public void showClientLogin() {
        authenticationFrame.showPanel(LogInCliente.TITLE);
    }
    /**
     * Exibe o painel de login de músico na interface de autenticação.
     */
    public void showMusicianLogin() {
        authenticationFrame.showPanel(LogInMusico.TITLE);
    }

    ////////////////////////////METODOS PARA ABRIR FRAMES\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Exibe o frame do músico, iniciando-o se ainda não estiver inicializado, ou torna-o visível se já estiver ativo.
     * Esconde a frame de autenticação.
     */
    public void showMusicianFrame() {
        musicianFrame.start();
        musicianFrame.setVisible(true);
        authenticationFrame.setVisible(false);
    }
    /**
     * Exibe o frame do cliente, iniciando-o se ainda não estiver inicializado,
     * ou torna-o visível se já estiver ativo. Esconde a frame de autenticação.
     */
    public void showClientFrame() {
        clientFrame.start();
        clientFrame.setVisible(true);
        authenticationFrame.setVisible(false);
    }
    /**
     * Controla a exibição do frame de autenticação dependendo da frame ativa (cliente ou músico),
     * Configurando adequadamente a exibição dos painéis.
     * Esconde as frames de cliente e músico, e encerra-as.
     */
    public void showAutenticationFrame() {
        if(clientFrame.isShowing()) {
            clientFrame.showPanelClient(MainMenu.TITLE);
            clientFrame.setCurrentPanel(clientFrame.getMenuInicial());
        }else if(musicianFrame.isShowing()) {
            musicianFrame.showPanelMusician(MainMenu.TITLE);
            musicianFrame.setCurrentPanel(musicianFrame.getMusicoMenuInicial());
        }
        musicianFrame.setVisible(false); //Esconder a frame
        musicianFrame.dispose();        //Fechar a frame
        clientFrame.setVisible(false); //Esconder a frame
        clientFrame.dispose();        //Fechar a frame
        authenticationFrame.setVisible(true);
        authenticationFrame.showPanel(MenuInicial.TITLE);

    }

    /////////////////////////METODOS PARA ABRIR PAINEIS CLIENTE\\\\\\\\\\\\\\\\\\\\\\\\\
    /**
     * Exibe o painel da loja no frame do cliente, atualizando a tabela de músicas disponíveis.
     */
    public void showStore() {
        clientFrame.showPanelClient(Store.TITLE);
        clientFrame.setCurrentPanel(clientFrame.getStore());
        updateStoreTable(getStoreTableModel(), getStoreTable());
        clientFrame.getStore().setMusics(getDb().addAllRockstarSongsVisible());
    }
    /**
     * Exibe o painel de "Minhas Músicas" no frame do cliente, atualizando a tabela de músicas do usuário.
     */
    public void showMyMusic() {
        clientFrame.showPanelClient(MyMusic.TITLE);
        clientFrame.setCurrentPanel(clientFrame.getMyMusic());
        updateMyMusicTable(getMyMusicTableModel(), getMyMusicTable());
    }
    /**
     * Exibe o painel de "Minhas Playlists" no frame do cliente.
     */
    public void showMyPlaylists() {
        clientFrame.showPanelClient(MyPlaylists.TITLE);
        clientFrame.setCurrentPanel(clientFrame.getMyPlaylists());
        atualizarTabelaPlaylists(getMyPlaylistsTableModel(), getMyPlaylistsTable());
        updatePurchaseTable(getPurchaseHistoryTableModel(), getPurchaseHistoryTable());

    }
    /**
     * Exibe o histórico de compras no frame do cliente, atualizando a tabela de histórico de compras.
     */
    public void showPurchaseHistory() {
        clientFrame.showPanelClient(PurchaseHistory.TITLE);
        clientFrame.setCurrentPanel(clientFrame.getPurchaseHistory());
        updatePurchaseTable(getPurchaseHistoryTableModel(), getPurchaseHistoryTable());
    }
    /**
     * Exibe os detalhes de uma compra específica no frame do cliente.
     */
    public void showPurchaseDetails(){
        clientFrame.showPanelClient(PurchaseDetails.TITLE);
        clientFrame.setCurrentPanel(clientFrame.getPurchaseDetails());
    }
    /**
     * Exibe o carrinho de compras no frame do cliente.
     */
    public void showShoppingCart() {
        //Não atualizar o painel atual. Retroceder sem nenhuma das outras opções volta ao current
        clientFrame.showPanelClient(ShoppingCart.TITLE);
    }
    /**
     * Exibe a playlist atual no frame do cliente.
     */
    public void showCurrentPlaylist() {
        clientFrame.showPanelClient(CurrentPlaylist.TITLE);
        clientFrame.setCurrentPanel(clientFrame.getCurrentPlaylist());
    }
    /**
     * Exibe o menu principal do cliente no frame do cliente.
     */
    public void showClientMainMenu() {
        clientFrame.showPanelClient(MainMenu.TITLE);
        clientFrame.setCurrentPanel(clientFrame.getMenuInicial());
    }


    /////////////////////METODOS PARA ABRIR PAINEIS MUSICO\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    /**
     * Exibe os painéis das músicas do músico e atualiza o painel.
     */
    public void showMusicianSongs() {
        musicianFrame.showPanelMusician(MusicoMusicas.TITLE);
        musicianFrame.setCurrentPanel(musicianFrame.getMusicoMusicas());
    }
    /**
     * Exibe os painéis dos álbuns do músico. e atualiza o painel.
     */
    public void showMusicianAlbuns() {
        musicianFrame.getMusicoMeusAlbuns().carregarAlbunsDoMusico();
        musicianFrame.showPanelMusician(MusicoMeusAlbuns.TITLE);
        musicianFrame.setCurrentPanel(musicianFrame.getMusicoMeusAlbuns());
    }
    /**
     * Exibe os painéis das estatísticas atualizado pelo método updateStats e atualiza o painel.
     */
    public void showMusicianStats() {
        musicianFrame.getMusicoEstatistica().updateStats(); //atualizar os dados Atuais.
        musicianFrame.showPanelMusician(MusicoEstatistica.TITLE);
        musicianFrame.setCurrentPanel(musicianFrame.getMusicoEstatistica());
    }
    /**
     * Exibe os painéis da pesquisa do músico e atualiza o painel.
     */
    public void showMusicianSearch() {
        musicianFrame.showPanelMusician(MusicoPesquisa.TITLE);
        musicianFrame.setCurrentPanel(musicianFrame.getMusicoPesquisa());
    }
    /**
     * Exibe o menu inicial do músico e atualiza o painel.
     */
    public void showMusicianMainMenu() {
        musicianFrame.showPanelMusician(MusicoMenuInicial.TITLE);
        musicianFrame.setCurrentPanel(musicianFrame.getMusicoMenuInicial());
    }
    /**
     * Atualiza a label, após carregamento, que mostra o saldo do user
     */
    public void updateBalance() {
        clientFrame.updateBalanceLabel();
    }
    /**
     * Obtém o frame do músico.
     * @return O frame do músico.
     */
    public MusicianRootFrame getMusicianFrame() {
        return musicianFrame;
    }
    /**
     * Obtém o frame do cliente.
     * @return O frame do cliente.
     */
    public ClientRootFrame getClientFrame() {
        return clientFrame;
    }
}
