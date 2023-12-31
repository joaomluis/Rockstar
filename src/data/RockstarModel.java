package data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RockstarModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 12565672347L;

    private final List<User> users;
    private final List<Music> musics;
    private final List<Album> albums;
    public RockstarModel() {
        this.users = new ArrayList<>();
        this.musics = new ArrayList<>();
        this.albums = new ArrayList<>();
    }
    public RockstarModel(List<User> users, List<Music> musics, List<Album> albums) {
        this.users = users;
        this.musics = musics != null ? musics : new ArrayList<>();
        this.albums = albums;
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

    public List<Music> getMusics() {
        return musics;
    }

    public List<Album> getAlbums() {
        return albums;
    }

}
