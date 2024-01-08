package data;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Representa um álbum de músicas, implementando a interface Serializable para permitir a serialização dos objetos desta classe.
 */
public class Album implements Serializable {
    /**
     * Número de versão utilizado na serialização da classe para garantir compatibilidade durante a desserialização.
     * Este campo é usado para identificar a versão da classe serializada e é importante para controlar a compatibilidade
     * durante operações de serialização e desserialização de objetos.
     * Se a versão da classe que realizou a serialização for diferente da versão atual da classe durante a desserialização,
     * pode ocorrer uma InvalidClassException.
     */
    private static final long serialVersionUID = 1325672347L;

    private String title;
    private Musico artist;
    private String genre;
    private ArrayList<Music> music;
    /**
     * Construtor da classe Album.
     * @param title  Título do álbum
     * @param artist Artista criador do álbum
     * @param genre  Género do álbum
     */
    public Album(String title, Musico artist, String genre) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.music = new ArrayList<>();
    }

    public ArrayList<Music> getMusicas(){
        return music;
    }
    /**
     * Para obter o nome do album
     * @return o nome do album
     */
    public String getTitle() {
        if(title==null){
            return "Sem album";
        }else {
            return title;
        }
    }
    /**
     * Para obter o genero do album
     * @return o genero do album
     */
    public String getGenre() {
        return genre;
    }

    public Musico getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return title;
    }
}
