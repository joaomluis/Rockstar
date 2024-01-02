package data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RockstarModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 12565672347L;

    private final List<User> users;
    private final List<Music> songs;
    private final List<Album> albums;
    private final List<Purchase> allPurchases;
    public RockstarModel() {
        this.users = new ArrayList<>();
        this.songs = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.allPurchases = new ArrayList<>();
    }
    public RockstarModel(List<User> users, List<Music> musics, List<Album> albums, List<Purchase> allPurchases) {
        this.users = users;
        this.songs = musics != null ? musics : new ArrayList<>();
        this.albums = albums;
        this.allPurchases = allPurchases;
    }

    public List<User> getUsers() {
        return users;
    }


    public boolean addUser(User user) {
        return users.add(user);
    }

    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                break;
            }
        }
    }

    public List<Music> getSongs() {
        return songs;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public List<Purchase> getAllPurchases() {
        return allPurchases;
    }

}
