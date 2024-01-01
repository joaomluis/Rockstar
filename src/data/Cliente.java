package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends User implements Serializable {
    private static final long serialVersionUID = 1325672347L;
    private double saldo;
    private List<Playlist> playlists;
    private List<Music> songsInCart;
    private List<Music> songsOwned;

    public Cliente(String username, String password) {
        super(username, password);
        saldo = 0.00;
        this.playlists = new ArrayList<>();
        this.songsInCart = new ArrayList<>();
        this.songsOwned = new ArrayList<>();
    }

    public Cliente() {
        super();
    }

    public double getSaldo() {
        return saldo;
    }

    public List<Music> getSongsInCart() {
        return songsInCart;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public List<Music> getSongsOwned() {
        return songsOwned;
    }

    public boolean adicionaSaldo(double valor){
        if(valor >= 0){
            saldo += valor;
            return true;
        }
        return false;
    }

    public void addPlaylistToClient(Playlist playlist) {
        playlists.add(playlist);
    }

}
