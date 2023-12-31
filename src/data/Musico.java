package data;


import java.io.Serializable;
import java.util.ArrayList;

public class Musico extends User implements Serializable {
    private static final long serialVersionUID = 1325672347L;

    private String pin;
    private ArrayList<Music> music = new ArrayList<>();
    private ArrayList<Album> albuns = new ArrayList<>();
    public Musico(String username, String password, String pin) {
        super(username, password);
        this.pin = pin;
    }

    public Musico() {
        super();
        this.pin = "";
    }

    public ArrayList<Music> getMusicas() {
        return music;
    }
    public ArrayList<Album> getAlbuns() {
        //caso os albuns sejam null, cria um novo array
        if(albuns == null){
            albuns = new ArrayList<>();
        }
        return albuns;
    }
    public String getPin() {
        return pin;
    }
    public void addMusic(Music music) {
        if(this.music == null) {
            this.music = new ArrayList<>();
            System.out.println("add (Musico) Musicas do musico");
        }
        this.music.add(music);
        for(Music m: this.music){
            System.out.println(m);
        }
    }
    public void addAlbum(Album album){
        albuns.add(album);
        System.out.println("add album ao musicoAlbuns");
    }
    @Override
    public String toString() {
        return super.getUsername();
    }

//    public void setAlbuns() {
//        this.albuns = new ArrayList<>();
//    }
}
