package ui;

import data.Cliente;
import data.Music;
import data.Playlist;
import data.Price;
import domain.RockstarDB;
import ui.auth.*;
import ui.client.*;
import ui.musician.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class RockstarGUI {

    // Dados
    private final RockstarDB db;

    private AuthRootFrame authenticationFrame;
    private ClientRootFrame clientFrame;
    private FrameMusico musicianFrame;

    public RockstarGUI(RockstarDB db) {
        this.db = db;
        authenticationFrame = new AuthRootFrame(this);
        clientFrame = new ClientRootFrame(this);
        musicianFrame = new FrameMusico(this);
    }


    public void start() {
        authenticationFrame.start();
    }


    public RockstarDB getDb() {
        return db;
    }

    public MyPlaylists getMyPlaylists() {
        return clientFrame.getMyPlaylists();
    }


    ////////////////////////////////////Atualizar painel carrinho\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public ShoppingCart getShoppingCartPanel() {
        return clientFrame.getShoppingCart();
    }

    public JTable getCartTable() {
        return getShoppingCartPanel().getPurchaseTable();
    }
    public DefaultTableModel getCartTableModel() {
        return getShoppingCartPanel().getTableModel();
    }

    public void updateCartTable(DefaultTableModel tableModel, JTable jTable) {
        tableModel.setRowCount(0); // Limpa a tabela
        getDb().addAllSongsInCartToTable(jTable); // Atualiza a tabela com as songs atualizadas
    }

    ///////////////////////////////////Atualizar tabela painel My Music\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    private MyMusic getMyMusicPanel() {
        return clientFrame.getMyMusic();
    }

    private JTable getMyMusicTable() {
        return getMyMusicPanel().getMusicTable();
    }
    private DefaultTableModel getMyMusicTableModel() {
        return getMyMusicPanel().getTableModel();
    }

    public void updateMyMusicTable (DefaultTableModel tableModel, JTable table) {
        tableModel.setRowCount(0);
        getDb().addAllOwnedSongsToTable(table);
    }

    /////////////////////Atualizar painel Historico Compras\\\\\\\\\\\\\\\\\\\\\\

    private PurchaseHistory getPurchaseHistoryPanel() {
        return clientFrame.getPurchaseHistory();
    }
    private JTable getPurchaseHistoryTable() {
        return getPurchaseHistoryPanel().getPurchaseTable();
    }

    private DefaultTableModel getPurchaseHistoryTableModel() {
        return getPurchaseHistoryPanel().getTableModel();
    }

    private void updatePurchaseTable (DefaultTableModel tableModel, JTable table) {
        tableModel.setRowCount(0);
        getDb().addAllPurchasesToTable(table);
    }

    //////////////////Atualizar painel Loja\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    private Store getStorePanel() {
        return clientFrame.getStore();
    }
    private DefaultTableModel getStoreTableModel() {
        return getStorePanel().getTableModel();
    }
    private JTable getStoreTable() {
        return getStorePanel().getStoreTable();
    }

    private void updateStoreTable(DefaultTableModel tableModel, JTable table) {
        tableModel.setRowCount(0);
        getDb().addAllRockstarSongsToTable(table);
    }

    /////////////////Atualizar painel Playlists\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

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

    private boolean existePlaylistNaTabela(DefaultTableModel model, Playlist playlist) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 0).equals(playlist.getNome())) {
                return true; // Playlist já existe na tabela
            }
        }
        return false; // Playlist não encontrada na tabela
    }

    public void atualizarTabelaPlaylists(DefaultTableModel tableModel, JTable table) {
        tableModel.setRowCount(0); // Limpa a tabela
        adicionarElementosTabela(table); // Atualiza a tabela com as playlists atualizadas
    }

    public JTable getMyPlaylistsTable() {
        return getMyPlaylists().getPlaylistTable();
    }

    public DefaultTableModel getMyPlaylistsTableModel() {
        return getMyPlaylists().getTableModel();
    }

    public void getMyPlaylistsButtonsToEnable() {
        getMyPlaylists().getCreatePlaylist().setEnabled(true);
        getMyPlaylists().getDeletePlaylist().setEnabled(true);
    }

    /////////////////////METODOS PARA ABRIR PAINEIS AUTENTICAÇÃO\\\\\\\\\\\\\\\\\\\\\
    public void showMainMenu() {
        authenticationFrame.showPanel(MenuInicial.TITLE);
    }

    public void showMusicianRegister() {
        authenticationFrame.showPanel(RegistarMusico.TITLE);
    }

    public void showClientRegister() {
        authenticationFrame.showPanel(RegistarCliente.TITLE);
    }

    public void showClientLogin() {
        authenticationFrame.showPanel(LogInCliente.TITLE);
    }

    public void showMusicianLogin() {
        authenticationFrame.showPanel(LogInMusico.TITLE);
    }

    ////////////////////////////METODOS PARA ABRIR FRAMES\\\\\\\\\\\\\\\\\\\\\\\\\\

    /// fechar frame de log in abre menu do cliente
    public void showMusicianFrame() {
        musicianFrame.start();
        authenticationFrame.dispose();
    }

    public void showClientFrame() {
        clientFrame.start();
        authenticationFrame.dispose();
    }

    public void showAutenticationFrame() {
        authenticationFrame.start();
        clientFrame.dispose();
    }

    /////////////////////////METODOS PARA ABRIR PAINEIS CLIENTE\\\\\\\\\\\\\\\\\\\\\\\\\

    public void showStore() {
        clientFrame.showPanelClient(Store.TITLE);
        clientFrame.setCurrentPanel(clientFrame.getStore());
        updateStoreTable(getStoreTableModel(), getStoreTable());
    }

    public void showMyMusic() {
        clientFrame.showPanelClient(MyMusic.TITLE);
        clientFrame.setCurrentPanel(clientFrame.getMyMusic());
        updateMyMusicTable(getMyMusicTableModel(), getMyMusicTable());
    }

    public void showMyPlaylists() {
        clientFrame.showPanelClient(MyPlaylists.TITLE);
        clientFrame.setCurrentPanel(clientFrame.getMyPlaylists());
    }

    public void showPurchaseHistory() {
        clientFrame.showPanelClient(PurchaseHistory.TITLE);
        clientFrame.setCurrentPanel(clientFrame.getPurchaseHistory());
        updatePurchaseTable(getPurchaseHistoryTableModel(), getPurchaseHistoryTable());
    }

    public void showShoppingCart() {
        //Não atualizar o painel atual. Retroceder sem nenhuma das outras opções volta ao current
        clientFrame.showPanelClient(ShoppingCart.TITLE);
    }
    public void showCurrentPlaylist() {
        clientFrame.showPanelClient(CurrentPlaylist.TITLE);
        clientFrame.setCurrentPanel(clientFrame.getCurrentPlaylist());
    }


    public void showClientMainMenu() {
        clientFrame.showPanelClient(MainMenu.TITLE);
        clientFrame.setCurrentPanel(clientFrame.getMenuInicial());
    }


    /////////////////////METODOS PARA ABRIR PAINEIS MUSICO\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void showMusicianSongs() {
        musicianFrame.showPanelMusician(MusicoMusicas.TITLE);
        musicianFrame.setCurrentPanel(musicianFrame.getMusicoMusicas());

    }
    public void showMusicianAlbuns() {
        musicianFrame.showPanelMusician(MusicoMeusAlbuns.TITLE);
        musicianFrame.setCurrentPanel(musicianFrame.getMusicoMeusAlbuns());

    }
    public void showMusicianStats() {
        musicianFrame.showPanelMusician(MusicoEstatistica.TITLE);
        musicianFrame.setCurrentPanel(musicianFrame.getMusicoEstatistica());
    }

    public void showMusicianSearch() {
        musicianFrame.showPanelMusician(MusicoPesquisa.TITLE);
        musicianFrame.setCurrentPanel(musicianFrame.getMusicoPesquisa());
    }

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

    public FrameMusico getMusicianFrame() {
        return musicianFrame;
    }

    public ClientRootFrame getClientFrame() {
        return clientFrame;
    }
}
