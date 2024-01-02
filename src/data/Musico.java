package data;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Musico extends User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1325672347L;

    private String pin;
    private ArrayList<Music> musics = new ArrayList<>();
    private ArrayList<Album> albuns = new ArrayList<>();
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
        System.out.println("add musica ao musicoMusicas");
    }
    public void addAlbum(Album album){
        albuns.add(album);
        System.out.println("add album ao musicoAlbuns");
    }
    @Override
    public String toString() {
        return super.getUsername();
    }

}
