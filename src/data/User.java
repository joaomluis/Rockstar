package data;

import java.io.Serial;
import java.io.Serializable;
/**
 * Representa um usuário genérico no sistema.
 * Fornece métodos para acessar o nome de usuário e senha.
 */
public abstract class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1325672347L;

    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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
}

