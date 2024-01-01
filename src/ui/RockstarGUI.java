package ui;

import data.Album;
import data.Playlist;
import domain.RockstarDB;
import ui.auth.*;
import ui.client.*;
import ui.musician.*;

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

    public ShoppingCart getShoppingCart() {
        return clientFrame.getShoppingCart();
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
    }
    public void showMusicianAlbuns() {
        musicianFrame.showPanelMusician(MusicoMeusAlbuns.TITLE);
    }
    public void showMusicianStats() {
        musicianFrame.showPanelMusician(MusicoEstatistica.TITLE);
    }

    public void showMusicianSearch() {
        musicianFrame.showPanelMusician(MusicoPesquisa.TITLE);
    }

    public void showMusicianMainMenu() {
        musicianFrame.showPanelMusician(MusicoMenuInicial.TITLE);
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
