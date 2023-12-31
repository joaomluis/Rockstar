package data;

import java.io.Serial;
import java.io.Serializable;

public abstract class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1325672347L;

    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public User() {
        this.username = "";
        this.password = "";

    }

    // Getters para username e password
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getPin() {
        return "";
    }
    public void listarMusicas(){

    }


    public boolean criaPlaylist() {
        return false;
    }
}
