package data;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Representa uma playlist de músicas associada a um cliente.
 */
public class Playlist implements Serializable {
    private static final long serialVersionUID = 1325672347L;
    private String nome;
    private ArrayList<Music> music;
    private boolean visibilidade;
    private Cliente autor;

    /**
     * Criará uma Playlist para o Cliente, apenas com um nome.
     * @param nome
     */
    public Playlist(String nome, Cliente cliente) {
        this.nome = nome;
        this.music = new ArrayList<>();
        this.visibilidade = true;
        this.autor = cliente;
    }

    public boolean isVisibilidade() {
        return visibilidade;
    }

    public void setVisibilidade(boolean visibilidade) {
        this.visibilidade = visibilidade;
    }

    public ArrayList<Music> getMusic() {
        return music;
    }

    public String getNome() {
        return nome;
    }

    public boolean getVisibilidade() {
        return visibilidade;
    }

    @Override
    public String toString() {
        return nome;
    }
}