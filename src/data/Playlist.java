package data;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Representa uma playlist de músicas associada a um cliente.
 */
public class Playlist implements Serializable {
    /**
     * Número de versão utilizado na serialização da classe para garantir compatibilidade durante a desserialização.
     * Este campo é usado para identificar a versão da classe serializada e é importante para controlar a compatibilidade
     * durante operações de serialização e desserialização de objetos.
     * Se a versão da classe que realizou a serialização for diferente da versão atual da classe durante a desserialização,
     * pode ocorrer uma InvalidClassException.
     */
    private static final long serialVersionUID = 1325672347L;
    private String nome;
    private ArrayList<Music> music;
    private boolean visibilidade;

    /**
     * Criará uma Playlist para o Cliente, apenas com um nome.
     * @param nome
     */
    public Playlist(String nome, Cliente cliente) {
        this.nome = nome;
        this.music = new ArrayList<>();
        this.visibilidade = true;
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