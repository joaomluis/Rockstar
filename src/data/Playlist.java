package data;

import java.io.Serializable;
import java.util.ArrayList;

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

    public Playlist() {
    }

    /**
     * Contrutor que contem todos os parametros na criação de uma nova Playlist.
     * @param nome
     * @param music
     */
    public Playlist(String nome, ArrayList<Music> music, Cliente cliente) {
        this.nome = nome;
        this.music = music;
        this.visibilidade = true; //Por defeito, as playlists sao visíveis.
        this.autor = cliente;
    }

    public boolean isVisibilidade() {
        return visibilidade;
    }

    public ArrayList<Music> getMusic() {
        return music;
    }

    /**
     * Adicionar uma música à Playlist.
     * Verificar se a música está na playlist antes de a adicionar.
     * @param music
     */
    public void adicionarMusica(Music music){
        if(!this.music.contains(music)) this.music.add(music);
    }
    /**
     * Remover uma música da Playlist.
     * Verificar se a música está na playlist antes de a tentar remover.
     * @param music
     */
    public void removerMusica(Music music){
        if(this.music.contains(music)) this.music.remove(music);
    }
    /**
     * Altera a visibilidade da Playlist.
     */
    public void alterarVisibilidade(){
        visibilidade = !visibilidade;
    }
    /**
     * Altera a visibilidade da Playlist para a que o usuario pedir.
     */
    public void alterarVisibilidadePlaylist(boolean visibilidade){
        this.visibilidade = visibilidade;

    }
    public String getNome() {
        return nome;
    }
}