package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Representa um cliente na aplicação, herdando a classe User e implementando Serializable.
 */
public class Cliente extends User implements Serializable {
    /**
     * Número de versão utilizado na serialização da classe para garantir compatibilidade durante a desserialização.
     * Este campo é usado para identificar a versão da classe serializada e é importante para controlar a compatibilidade
     * durante operações de serialização e desserialização de objetos.
     * Se a versão da classe que realizou a serialização for diferente da versão atual da classe durante a desserialização,
     * pode ocorrer uma InvalidClassException.
     */
    private static final long serialVersionUID = 1325672347L;
    private double saldo;
    private List<Playlist> playlists;
    private List<Music> songsInCart;
    private List<Music> songsOwned;
    private List<Purchase> purchasesMade;
    /**
     * Construtor para inicializar um Cliente com um nome de usuário e senha.
     * Inicializa as listas de músicas no carrinho, músicas possuídas e compras feitas.
     * @param username Nome de usuário do cliente.
     * @param password Senha do cliente.
     */
    public Cliente(String username, String password) {
        super(username, password);
        saldo = 0.00;
        this.playlists = new ArrayList<>();
        this.songsInCart = new ArrayList<>();
        this.songsOwned = new ArrayList<>();
        this.purchasesMade = new ArrayList<>();
    }

    public double getSaldo() {
        return saldo;
    }
    /**
     * Define o saldo do cliente.
     * @param saldo O novo saldo a ser definido para o cliente.
     */
    public void setSaldo(double saldo) {
        this.saldo = saldo;
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

    public List<Purchase> getPurchasesMade() {
        return purchasesMade;
    }

    /**
     * Adiciona saldo à conta do cliente.
     * @param valor O valor a ser adicionado ao saldo. Deve ser maior ou igual a zero.
     * @return true se o valor for adicionado com sucesso, false caso contrário.
     */
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
    /**
     * Verifica se o cliente já avaliou uma música específica.
     * @param music A música a verificar.
     * @return true se o cliente já avaliou a música, false caso contrário.
     */
    public boolean hasRatedMusic(Music music) {
        for (Rating avaliacao : music.getAvaliacoes()) {
            if (avaliacao.getCliente() == this) {
                return true; //se já avaliou a musica
            }
        }
        return false;
    }
}
