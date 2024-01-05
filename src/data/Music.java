package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Music implements Serializable {
    private static final long serialVersionUID = 1325672347L;
    private String title;
    private Musico artist;
    private String genre;
    private double preco;
    private List<Price> priceHistory;
    private double rating;
    private ArrayList<Rating> avaliacoes = new ArrayList<>(); //<User, Avaliação>
    private boolean visibilidade;
    private Album album;

    public Music(String tittle, Musico artist, String genre, double preco) {
        this.title = tittle;
        this.artist = artist;
        this.genre = genre;
        this.visibilidade = true;
        this.priceHistory = new ArrayList<>();
        this.album = null;

        Price initialPrice = new Price(preco);
        priceHistory.add(initialPrice);

        this.preco = setCurrentPriceFromHistory();
    }
    public Music(String tittle, Musico artist, String genre, double preco, Album album) {
        this.title = tittle;
        this.artist = artist;
        this.genre = genre;
        this.visibilidade = true;
        this.priceHistory = new ArrayList<>();
        this.album = album;

        Price initialPrice = new Price(preco);
        priceHistory.add(initialPrice);

        this.preco = setCurrentPriceFromHistory();
    }
    public Music() {
    }

    public Album getAlbum() {
        return album;
    }

    public double setCurrentPriceFromHistory() {
        double value = 0;
        if (priceHistory != null && !priceHistory.isEmpty()) {
            // Get the last price from the priceHistory list
            Price lastPrice = priceHistory.get(priceHistory.size() - 1);
            // Set the currentPrice attribute to the last price's double value
            value = lastPrice.getPreco();
        }
        return value;
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
     * será bem-sucedida e adicionada ao hostirico de preços
     *
     * @param preco   novo preço
     * @param artista artista que está a fazer a alteração
     * @return true se a alteração foi bem-sucedida e false caso contrário
     */
    public boolean alterarPreco(Double preco, User artista) {
        if (artista.equals(artist) && preco >= 0) {
            priceHistory.add(new Price(preco));
            return true;
        }
        return false;
    }
    /**
     * Adiciona uma avaliação à música, verificando se o cliente já a avaliou previamente
     *
     * @param rating Avaliação do cliente em relação à música
     * @param cliente   Cliente que pretende adicionar a avaliação
     * @return true se o cliente está a avaliar a música pela 1ª vez e realiza a avaliação e false caso já tenha avaliado a música anteriormente
     */
    public boolean adicionarAvaliacao(Rating rating, Cliente cliente) {
        for (Rating a : avaliacoes) {
            if (a.getCliente().equals(cliente)) {
                return false;
            }
        }
        avaliacoes.add(rating);
        return true;
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
     * Caso não tenha avaliações, a música terá como default a avaliação de 3 que será intermédia.
     * Se tiver avaliações, fará a média de todas as avaliações da música.
     *
     * @return 3 (default) ou a média real da musica
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
    public void addAvaliacao(Rating rating){
        avaliacoes.add(rating);
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
