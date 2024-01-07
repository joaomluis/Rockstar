package data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * Representa um músico, que é herdada da classe User e implementando Serializable.
 */
public class Musico extends User implements Serializable {
    /**
     * Número de versão utilizado na serialização da classe para garantir compatibilidade durante a desserialização.
     * Este campo é usado para identificar a versão da classe serializada e é importante para controlar a compatibilidade
     * durante operações de serialização e desserialização de objetos.
     * Se a versão da classe que realizou a serialização for diferente da versão atual da classe durante a desserialização,
     * pode ocorrer uma InvalidClassException.
     */
    @Serial
    private static final long serialVersionUID = 1325672347L;

    private String pin;
    private ArrayList<Music> musics = new ArrayList<>();
    private ArrayList<Album> albuns = new ArrayList<>();
    /**
     * Construtor da classe Musico.
     * @param username Nome de usuário.
     * @param password Senha de acesso.
     * @param pin PIN do músico.
     */
    public Musico(String username, String password, String pin) {
        super(username, password);
        this.pin = pin;
    }
    public ArrayList<Music> getMusicas() {
        return musics;
    }
    public ArrayList<Album> getAlbuns() {
        return albuns;
    }
    public String getPin() {
        return pin;
    }
    public void addMusic(Music music) {
        musics.add(music);
    }
    public void addAlbum(Album album){
        albuns.add(album);
    }
    @Override
    public String toString() {
        return super.getUsername();
    }

}
