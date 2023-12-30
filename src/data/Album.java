package data;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private static final long serialVersionUID = 1325672347L;

    private String title;
    private Musico artist;
    private String genre;
    private ArrayList<Music> music;

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
        return title;
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
}
