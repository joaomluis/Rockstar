package ui;

import domain.RockstarDB;
import ui.auth.*;
import ui.client.*;
import ui.musician.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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

    public MyMusic getMyMusicPanel() {
        return clientFrame.getMyMusic();
    }

    public JTable getMyMusicTable() {
        return getMyMusicPanel().getMusicTable();
    }
    public DefaultTableModel getMyMusicTableModel() {
        return getMyMusicPanel().getTableModel();
    }

    public void updateMyMusicTable (DefaultTableModel tableModel, JTable table) {
        tableModel.setRowCount(0);
        getDb().addAllOwnedSongsToTable(table);
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
    }

    public void showMyMusic() {
        clientFrame.showPanelClient(MyMusic.TITLE);
    }

    public void showMyPlaylists() {
        clientFrame.showPanelClient(MyPlaylists.TITLE);
    }

    public void showPurchaseHistory() {
        clientFrame.showPanelClient(PurchaseHistory.TITLE);
    }

    public void showShoppingCart() {
        clientFrame.showPanelClient(ShoppingCart.TITLE);
    }
    public void showCurrentPlaylist() {
        clientFrame.showPanelClient(CurrentPlaylist.TITLE);
    }


    public void showClientMainMenu() {
        clientFrame.showPanelClient(MainMenu.TITLE);
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


}
