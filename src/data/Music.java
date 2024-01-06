package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Representa uma música na aplicação RockStar.
 */
public class Music implements Serializable {
    private static final long serialVersionUID = 1325672347L;
    private String title;
    private Musico artist;
    private String genre;
    private double preco;
    private List<Price> priceHistory;
    private ArrayList<Rating> avaliacoes = new ArrayList<>(); //<User, Avaliação>
    private boolean visibilidade;
    private Album album;
    /**
     * Construtor para uma música com informações básicas sem Álbum.
     * @param tittle Título da música.
     * @param artist Artista da música.
     * @param genre Género da música.
     * @param preco Preço inicial da música.
     */
    public Music(String tittle, Musico artist, String genre, double preco) {
        this.title = tittle;
        this.artist = artist;
        this.genre = genre;
        this.visibilidade = true;
        this.priceHistory = new ArrayList<>();
        this.album = null;

        Price initialPrice = new Price(preco);
        priceHistory.add(initialPrice);

        this.preco = getPreco();
    }
    /**
     * Construtor para uma música associada a um álbum.
     * @param tittle Título da música.
     * @param artist Artista da música.
     * @param genre Género da música.
     * @param preco Preço inicial da música.
     * @param album Álbum ao qual a música pertence.
     */
    public Music(String tittle, Musico artist, String genre, double preco, Album album) {
        this.title = tittle;
        this.artist = artist;
        this.genre = genre;
        this.visibilidade = true;
        this.priceHistory = new ArrayList<>();
        this.album = album;

        Price initialPrice = new Price(preco);
        priceHistory.add(initialPrice);

        this.preco = getPreco();
    }

    public Album getAlbum() {
        return album;
    }
    public String getTitle() {
        return title;
    }
    public String getGenre() {
        return genre;
    }
    public Musico getArtist() {
        return artist;
    }
    /**
     * GetPreço, para saber o preço da música
     *
     * @return Preco
     */
    public double getPreco() {
        //return preco;
        if (!priceHistory.isEmpty()) {
            return priceHistory.get(priceHistory.size() - 1).getPreco();
        } else return preco;
    }
    /**
     * Ver o historico de precos da musica
     *
     * @return ArrayList com todos os preços anteriores e data de modificação
     */
    public List<Price> getHistoricoPreco() {
        return priceHistory;
    }
    /**
     * Caso a alteração de preço seja feita pelo artista da música,
     * será bem-sucedida e adicionada ao histórico de preços
     * @param preco   novo preço
     * @param artista musico que está a fazer a alteração
     * @return true se a alteração foi bem-sucedida e false caso contrário
     */
    public boolean alterarPreco(Double preco, User artista) {
        if (artista.equals(artist) && preco >= 0) {
            priceHistory.add(new Price(preco));
            return true;
        }
        return false;
    }

    public boolean isVisibilidade() {
        return visibilidade;
    }

    public void setVisibilidade(boolean visibilidade) {
        this.visibilidade = visibilidade;
    }
    public boolean alterarTitulo(String novoNome) {
        if (novoNome != null && !novoNome.equals(" ")) {
            title = novoNome;
            return true;
        }
        return false;
    }
    public ArrayList<Rating> getAvaliacoes() {
        return avaliacoes;
    }
    /**
     * Avaliação média da música.
     * Caso não tenha avaliações, a música terá como default a avaliação de 0.
     * Se tiver avaliações, fará a média de todas as avaliações da música.
     *
     * @return 0 (default) ou a média real da musica.
     */
    public double avaliacaoMedia() {
        double soma = 0;
        if (avaliacoes.isEmpty()) {
            return 0;
        } else {
            for (Rating a : avaliacoes) {
                soma += a.getAvaliacao();
            }
        }
        return soma / avaliacoes.size();
    }
    @Override
    public String toString() {
        return "Musica{" +
                "title='" + title + '\'' +
                ", artista=" + artist +
                ", genero='" + genre + '\'' +
                ", preco=" + getPreco() +
                ", avaliacao=" + avaliacaoMedia() +
                ", visibilidade=" + visibilidade +
                '}';
    }
}
