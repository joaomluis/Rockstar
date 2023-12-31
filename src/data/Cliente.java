package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends User implements Serializable {
    private static final long serialVersionUID = 1325672347L;
    private double saldo;
    private List<Playlist> playlists = new ArrayList<>();

    public Cliente(String username, String password) {
        super(username, password);
        saldo = 0.00;
        this.playlists = new ArrayList<>();
    }

    public Cliente() {
        super();
    }

    public double getSaldo() {
        return saldo;
    }

    public List<Playlist> getPlaylists() {
        if (playlists == null){
            playlists = new ArrayList<>();
        }
        return playlists;
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
