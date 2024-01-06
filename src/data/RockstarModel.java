package data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RockstarModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 12565672347L;

    private final List<User> users;
    private final List<Music> allSongsAvailable;
    private final List<Album> albums;
    private final List<Purchase> allPurchases;
    public RockstarModel() {
        this.users = new ArrayList<>();
        this.allSongsAvailable = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.allPurchases = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    /**
     * Adiciona um user à Lista de Users da plataforma
     * @param user User a adicionar
     * @return true se sucesso, false se não
     */
    public boolean addUser(User user) {
        return users.add(user);
    }

    /**
     * Percorre a Lista de Users e se encontrar um com o mesmo username que
     * o do user que é passado no parâmetro substitui-os na Lista de de users
     * @param user User a verificar
     */
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                break;
            }
        }
    }

    public List<Music> getAllSongsAvailable() {
        return allSongsAvailable;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public List<Purchase> getAllPurchases() {
        return allPurchases;
    }

}
