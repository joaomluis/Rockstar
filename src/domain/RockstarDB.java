package domain;

import data.*;
import ui.musician.CriteriosMusica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RockstarDB {

    static String dbPath = "baseDadosRockstar.ser";
    private int commit;

    private RockstarModel dados;
    private User currentUser;

    public RockstarDB(RockstarModel dados) {
        this.dados = dados;
    }

    public RockstarModel getDados() {
        return dados;
    }

    public void init() {
        if (checkIfDBExists()) {
            System.out.println("DB exists");
            loadDB();
            System.out.println("DB loaded");
        } else {
            saveDB();
        }
    }

    private boolean checkIfDBExists() {
        try {
            File f = new File(dbPath);
            return f.exists();
        } catch (Exception e) {
            return false;
        }
    }

    private void saveDB() {
        try (FileOutputStream fos = new FileOutputStream(dbPath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(dados);
            oos.close();
            fos.close();
            System.out.println(dbPath + " serialized");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void loadDB() {
        try (FileInputStream fis = new FileInputStream(dbPath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object obj = ois.readObject();
            if (obj instanceof RockstarModel) {
                dados = (RockstarModel) obj;
                System.out.println("RockStar deserialized");
            } else {
                System.out.println("Failed to deserialize. Invalid data type.");
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load file check stacktrace");
            e.printStackTrace();
        }
    }

    public boolean hasLoggedUser() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Cliente getCurrentUserAsClient() {
        return (Cliente) getCurrentUser();
    }

    public void updateUser(User user) {
        dados.updateUser(user);
        currentUser = user;
        saveDB();
    }

    public void saveCurrentUser() {
        dados.updateUser(currentUser);
        saveDB();
    }

    public void logOut() {
        currentUser = null;
    }

    public RockStarDBStatus registarCliente(String inputUsername, String inputPassword) {

        List<User> users = dados.getUsers();

        for (User user : users) {
            if (user.getUsername().equals(inputUsername)) {
                saveDB();
                return RockStarDBStatus.DB_USER_ALREADY_EXISTS;
            }
        }

        if (dados.addUser(new Cliente(inputUsername, inputPassword))) {
            saveDB();
            return RockStarDBStatus.DB_USER_ADDED;
        } else {
            return RockStarDBStatus.DB_USER_FAILED_TO_SAVE;
        }
    }

    public RockStarDBStatus registarMusico(String inputUsername, String inputPassword, String inputPin) {
        List<User> users = dados.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(inputUsername)) {
                saveDB();
                return RockStarDBStatus.DB_USER_ALREADY_EXISTS;
            }
        }
        if (dados.addUser(new Musico(inputUsername, inputPassword, inputPin))) {
            saveDB();
            return RockStarDBStatus.DB_USER_ADDED;
        } else {
            return RockStarDBStatus.DB_USER_FAILED_TO_SAVE;
        }
    }

    public RockStarDBStatus loginCliente(String inputUsername, String inputPassword) {
        List<User> users = dados.getUsers();
        for (User user : users) {
            if (user instanceof Cliente) {
                if (user.getUsername().equals(inputUsername)) {
                    if (user.getPassword().equals(inputPassword)) {
                        currentUser = user;
                        return RockStarDBStatus.DB_USER_LOGIN_SUCCESS;
                    } else {
                        return RockStarDBStatus.DB_USER_LOGIN_FAILED;
                    }
                }
            }
        }

        return RockStarDBStatus.DB_USER_LOGIN_FAILED;
    }

    public RockStarDBStatus loginMusico(String inputUsername, String inputPassword, String inputPin) {
        List<User> users = dados.getUsers();
        for (User user : users) {
            if (user.getUsername().equals(inputUsername)) {
                if (user.getPassword().equals(inputPassword)) {
                    if (user.getPin().equals(inputPin)) {
                        currentUser = user;
                        return RockStarDBStatus.DB_USER_LOGIN_SUCCESS;
                    }
                } else {
                    return RockStarDBStatus.DB_USER_LOGIN_FAILED;
                }
            }
        }

        return RockStarDBStatus.DB_USER_LOGIN_FAILED;
    }


    public boolean addMusica(Music music) {
        //Este if é irrelevante
        if (!validSongName(music.getTitle())) {
            return false;
        }
        getCurrentUserAsMusician().addMusic(music);
        dados.getAllSongsAvailable().add(music);
        System.out.println("adicionada nova musica");
        saveDB();
        System.out.println("gravado");
        return true;
    }

    public boolean addAlbum(Album album) {
        if (currentUser instanceof Musico) {
            Musico musico = getCurrentUserAsMusician();
            if (!validAlbumName(album.getTitle())) {
                return false; // já existe um album c/ o mesmo titulo na lista de álbuns do músico
            }
            musico.addAlbum(album);
            dados.getAlbums().add(album);
            System.out.println("adicionado novo album");
            saveDB();
            System.out.println("gravado");

            return true;
        }
        return false; // O usuário atual não é um músico ou a lista de dados é nula
    }


    //devolve vetor com os generos das musicas
    ////////////////////////////ESTATISTICAS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public int getTotalUsers() {
        if (dados.getUsers() != null) {
            return dados.getUsers().size();
        }
        return -1;
    }

    public int getTotalMusician() {
        int totalMusicos = 0;
        if (dados.getUsers() != null) {
            for (User u : dados.getUsers()) {
                if (u instanceof Musico) {
                    totalMusicos++;
                }
            }
            return totalMusicos;
        }
        return -1;
    }

    public int getTotalSongs() {
        List<Music> musics = dados.getAllSongsAvailable();
        if (musics != null) {
            return musics.size();
        } else {
            return -1;
        }
    }

    public int getTotaAlbums() {
        List<Album> albums = dados.getAlbums();
        if (albums != null) {
            return albums.size();
        } else {
            return -1;
        }
    }

    public double getTotalValueSongs() {
        double valorTotalMusicas = 0;
        if (dados.getAllSongsAvailable() != null) {
            for (Music m : dados.getAllSongsAvailable()) {
                valorTotalMusicas += m.getPreco();
            }
            return valorTotalMusicas;
        }
        return -1;
    }

    public Album[] getMusicianAlbums(Musico musico) {
        int aux = musico.getAlbuns().size() + 1;
        Album[] dropDown = new Album[aux];
        dropDown[0] = null;
        int i = 1;
        for (Album a : musico.getAlbuns()) {
            dropDown[i] = a;
            i++;
        }
        return dropDown;
    }
    public Playlist[] getClientPlaylist(Cliente cliente) {
        int aux = cliente.getPlaylists().size();
        Playlist[] dropDown = new Playlist[aux];
        int i = 0;
        for (Playlist a : cliente.getPlaylists()) {
            dropDown[i] = a;
            i++;
        }
        return dropDown;
    }


    //devolve vetor com a quantidade de albuns de X genero
    public int albumByGenre(String genero) {
        int albumByGenre = 0;
        //verifica se dados é null ou os albums são null antes de percorrer os albums
        if (dados != null && dados.getAlbums() != null) {
            for (Album a : dados.getAlbums()) {
                if (a.getGenre().equalsIgnoreCase(genero)) {
                    albumByGenre++;
                }
            }
        }
        return albumByGenre;
    }

    //lê os generos de musicas que há disponiveis
    public String[] getMusicGenrs() {
        Genre[] generos = Genre.values();
        String[] generosMusicais = new String[generos.length];

        for (int i = 0; i < generos.length; i++) {
            generosMusicais[i] = generos[i].toString();
        }
        return generosMusicais;
    }


    //verifica se existe um nome igual na lista de músicas
    public boolean validSongName(String nome) {
        if (currentUser instanceof Musico && getCurrentUserAsMusician().getMusicas() != null && nome != null && !nome.isEmpty()) {
            for (Music m : ((Musico) currentUser).getMusicas()) {
                if (m.getTitle().equalsIgnoreCase(nome)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validAlbumName(String nome) {
        if (currentUser instanceof Musico && getCurrentUserAsMusician().getMusicas() != null && nome != null && !nome.isEmpty()) {
            for (Album a : ((Musico) currentUser).getAlbuns()) {
                if (a.getTitle().equalsIgnoreCase(nome)) {
                    return false;
                }
            }
        }
        return true;
    }
    //!= null para não dar null exception quando é um musico s/ musicas na sua lista.


    //falta método para calcular o valor total de músicas vendidas.


    ///////////////////////////PLAYLISTS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Método que verifica se a Playlist passada no parâmetro tem um nome igual a alguma que
     * já exista na conta do cliente através de checkPlaylistNameExists. Se não exisitir a
     * playlist é adicionada à coleção do cliente.
     * @param playlist
     * @return true se o nome não estiver em uso e a playlist é adicionada à coleção de playlists
     * do cliente, false se o nome estiver em uso.
     */
    public boolean addPlaylist(Playlist playlist) {

        Cliente cliente = getCurrentUserAsClient();

        if (checkPlaylistNameExists(playlist)) {
            return false;
        }
        cliente.addPlaylistToClient(playlist);
        return true;
    }

    /**
     * Método que serve para gerar uma playlist com músicas aleatórias ja compradas pelo Cliente.
     * Cria um objeto Playlist com os pârametros que são especificados pelo input do user, depois
     * faz uma série de verificações para ver se é possível fazer a dita playlist.
     * Verifica se algum campo do input estava vazio, se já existe alguma Playlist na
     * coleção do Cliente que o mesmo nome com o método checkPlaylistNameExists. Verifica também
     * se a playlist criada não tem músicas repetidas com checkIfSongAlreadyAdded.
     * O generatePlaylist vai adicionando músicas à nova playlist até chegar ao limite que o user
     * deu ou até não existirem mais músicas do género especificado pelo user.
     * @param name
     * @param size
     * @param genre
     * @return São vários tipos do Enum RockStarDBStatus que conforme as condições são respeitadas,
     * ou não, o return varia. Por exemplo, se já existir uma playlist com o nome dado na coleção do user,
     * o return será DB_PLAYLIST_NAME_ALREADY_EXISTS.
     */
    public RockStarDBStatus generatePlaylist(String name, int size, String genre) {
        Cliente currentClient = getCurrentUserAsClient();
        List<Music> originalOwnedSongs = currentClient.getSongsOwned();
        List<Music> ownedSongs = new ArrayList<>(originalOwnedSongs);
        Playlist newPlaylist = new Playlist(name, currentClient);

        if (!name.isEmpty() && size > 0) {
            if (!checkPlaylistNameExists(newPlaylist)) {
                int songsAdded = 0;

                Collections.shuffle(ownedSongs);
                for (Music music : ownedSongs) {
                    if (music.getGenre().equals(genre) && !checkIfSongAlreadyAdded(newPlaylist, music)) {
                        newPlaylist.getMusic().add(music);
                        songsAdded++;

                        if (songsAdded == size) { //aqui ele vai adicionando dentro do loop e compara cada vez que adiciona se chegou ao size
                            currentClient.addPlaylistToClient(newPlaylist);
                            saveCurrentUser();
                            return RockStarDBStatus.DB_PLAYLIST_GENERATED_SUCCESSFULLY;
                        }
                    }
                }
                if (songsAdded > 0) {
                    currentClient.addPlaylistToClient(newPlaylist);
                    saveCurrentUser();
                    return RockStarDBStatus.DB_PLAYLIST_GENERATED_BUT_WITHOUT_WANTED_SIZE;
                } else {
                    return RockStarDBStatus.DB_NO_SONGS_ADDED_TO_PLAYLIST;
                }
            } else {
                return RockStarDBStatus.DB_PLAYLIST_NAME_ALREADY_EXISTS;
            }
        } else {
            return RockStarDBStatus.DB_SOME_FIELD_IS_EMPTY;
        }
    }
    
    /**
     * Verifica se a música passada no parâmetro já se encontra na playlist chamda no parâmetro.
     * Faz isto ao chamar o makeSongTempID criando um ID unico da musica. De seguida, no for loop, são percorridas as músicas
     * da playlist e para cada música é feito esse mesmo ID, se o ID for igual significa que a música passada no parâmetro
     * já faz parte da playlist verificada.
     * @param playlist
     * @param song
     * @return true se a música verificada já se encontra na playlist a verificar, false se não.
     */
    private boolean checkIfSongAlreadyAdded (Playlist playlist, Music song) {
        String songIdentifier = makeSongTempID(song);

        for (Music playlistMusic : playlist.getMusic()) {
            String playlistMusicIdentifier = makeSongTempID(playlistMusic);

            if (playlistMusicIdentifier.equals(songIdentifier)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cria um "ID" com a junção do nome e do artista para poder servir para verificações.
     * @param music
     * @return Uma String que é a concatenação do título da música, mais um traço, mais o autor.
     */
    private String makeSongTempID(Music music) {
        return music.getTitle() + "-" + music.getArtist();
    }

    /**
     * Método que verifica se o nome da playlist passada como pârametro já existe na lista de playlists
     * do cliente que está logado no sistema.
     * @param newPlaylist
     * @return true se o nome já existe na lista do Cliente, false se não.
     */
    private boolean checkPlaylistNameExists(Playlist newPlaylist) {
        Cliente currentUser = getCurrentUserAsClient();
        String newPlaylistName = newPlaylist.getNome();

        for (Playlist playlist : currentUser.getPlaylists()) {
            if (playlist.getNome().equalsIgnoreCase(newPlaylistName)) {
                return true; // Playlist name already exists
            }
        }
        return false; // Playlist name doesn't exist
    }

    public RockStarDBStatus addMusicaPlaylist(Music music, Playlist playlist) {
        if (music.isVisibilidade()) {
            if (checkIfSongAlreadyAdded(playlist, music)) {
                return RockStarDBStatus.DB_MUSIC_ALREADY_EXISTS_IN_THE_PLAYLIST;
            }
            playlist.getMusic().add(music);
            saveDB();
            return RockStarDBStatus.DB_MUSIC_ADDED;
        }
        return RockStarDBStatus.DB_MUSIC_CANT_BE_ADDED_TO_PLAYLISTS;
    }


    //////////////////Adicionar histórico preços à JDialog\\\\\\\\\\\\\\\\\\\\\\\\

    public void addPriceHistoryToTable(Music song, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        List<Price> priceList = song.getHistoricoPreco();

        for (Price price: priceList) {
            Object[] row = {formatLocalDateTime(price.getData()), String.format("%1$,.2f€", song.getPreco())};
                model.addRow(row);
        }
    }
    private boolean priceExistsOnTable(DefaultTableModel model, Price price) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 0).equals(price.getData())) {
                return true;
            }
        }
        return false;
    }

    //////////////////////Histórico Compras\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void addAllPurchasesToTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        List<Purchase> purchasesMade = getCurrentUserAsClient().getPurchasesMade();

        for (Purchase purchase : purchasesMade) {

            Object[] row = {purchase.getPurchaseId(), formatLocalDateTime(purchase.getDataCompra()),String.format("%1$,.2f€", purchase.getPrice())};
            if(!purchaseExistsOnTable(model, purchase)) {
                model.addRow(row);
            }
        }
    }

    /**
     * Formata uma variável LocalDateTime para o formato mais refinado yyyy-MM-dd HH:mm:ss
     * @param dateTime
     * @return
     */
    private String formatLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    private boolean purchaseExistsOnTable(DefaultTableModel model, Purchase purchase) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 0).equals(purchase.getPurchaseId())) {
                return true;
            }
        }
        return false;
    }


    ///////////////////////////LOJA\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void addAllRockstarSongsToTable(JTable table, ArrayList<Music> musics) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        String rating = "";
        for (Music song : musics) {
            if (song.avaliacaoMedia() == 0) {
                rating = "Sem Rating";
            } else {
                rating = String.valueOf(song.avaliacaoMedia());
            }
            Object[] row = {song.getTitle(), song.getArtist(), song.getGenre(), String.format("%1$,.2f€", song.getPreco()), rating};
            if (!songExistsOnTable(model, song)) {
                model.addRow(row);
            }
        }
    }

    /**
     * Procura todas as músicas disponiveis para o cliente efetuar uma compra
     * @return musicas visiveis no momento
     */
    public ArrayList<Music> addAllRockstarSongsVisible() {
        List<Music> musicasPlataforma = dados.getAllSongsAvailable();
        ArrayList<Music> musicasVisiveis = new ArrayList<>();

        for(Music m : musicasPlataforma){
            if(m.isVisibilidade()) musicasVisiveis.add(m);
        }
        return musicasVisiveis;
    }

    private boolean songExistsOnTable(DefaultTableModel model, Music song) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 0).equals(song.getTitle())) {
                return true;
            }
        }
        return false;
    }

    //////////////////////////////////CARRINHO\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public RockStarDBStatus addSongToCart(Music song) {
        if (isSongOnCart(song)) {
            return RockStarDBStatus.DB_SONG_ALREADY_IN_CART;
        } else if (isSongAlreadyOwned(song)) {
            return RockStarDBStatus.DB_SONG_ALREADY_BOUGHT;
        } else {
            return RockStarDBStatus.DB_SONG_ADDED_TO_CART;
        }
    }

    /**
     * Compara o nome e autor da música que se quer adicionar ao carrinho com as musicas que
     * já estão na ArrayList do carrinho do cliente
     * @param song
     * @return true se a música verificada já se encontra na ArrayList do carrinho, false se não.
     */
    private boolean isSongOnCart (Music song) {
        List<Music> songsInCart = getCurrentUserAsClient().getSongsInCart();
        for (Music music : songsInCart) {
            if (music.getTitle().equals(song.getTitle()) && music.getArtist().equals(song.getArtist())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compara o nome e autor da música que se quer adicionar ao carrinho com as músicas que
     * já estão na ArrayList das músicas que já pertencem ao cliente
     * @param song
     * @return true se a música verificada já se encontra na ArrayList de músicas ja compradas, false se não.
     */
    private boolean isSongAlreadyOwned(Music song) {
        List<Music> songsOwned = getCurrentUserAsClient().getSongsOwned();
        for (Music music : songsOwned) {
            if (music.getTitle().equals(song.getTitle()) && music.getArtist().equals(song.getArtist())) {
                return true;
            }
        }
        return false;
    }


    public void addAllSongsInCartToTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        List<Music> songsInCart = getCurrentUserAsClient().getSongsInCart();

        for (Music song : songsInCart) {
            Object[] row = {song.getTitle(), song.getArtist(),String.format("%1$,.2f€", song.getPreco())};
            if(!songExistsOnTable(model, song)) {
                model.addRow(row);
            }
        }
    }


    /**
     *
     * @param rowIndex
     * @return
     */
    public RockStarDBStatus buyAllSongsFromCart(int rowIndex) {

        Cliente currentUser = getCurrentUserAsClient();
        double currentBalance = getCurrentUserAsClient().getSaldo();
        double totalCartPrice = 0.0;

        if(!getCurrentUserAsClient().getSongsInCart().isEmpty()) {
            // Calcular o preço total das músicas no carrinho
            for (Music music : getCurrentUserAsClient().getSongsInCart()) {
                totalCartPrice += music.getPreco();
            }

            // Verificar se o saldo é suficiente para comprar todas as músicas
            if (totalCartPrice <= currentBalance) {
                Purchase newPurchase = new Purchase(getCurrentUserAsClient(), totalCartPrice);
                for (Music music : getCurrentUserAsClient().getSongsInCart()) {
                    newPurchase.getSongList().add(music); // Adicionar música à lista de músicas compradas
                    addSongToMyMusic(music);
                }

                // Deduzir o preço total do saldo
                getCurrentUserAsClient().setSaldo(currentBalance - totalCartPrice);
                getCurrentUserAsClient().getSongsInCart().clear();
                currentUser.getPurchasesMade().add(newPurchase); //adiciona compra à lista de compras do user
                getDados().getAllPurchases().add(newPurchase);
                saveCurrentUser();

                return RockStarDBStatus.DB_SONGS_PURCHASED_SUCCESSFULLY;
            } else {
                return RockStarDBStatus.DB_INSUFFICIENT_BALANCE;
            }
        }
        return RockStarDBStatus.DB_CART_EMPTY;
    }


    /**
     * Adiciona a música à lista de músicas que o cliente possui.
     * @param song
     */
    private void addSongToMyMusic(Music song) {
        getCurrentUserAsClient().getSongsOwned().add(song);
    }

    ////////////////////////////////////MINHA MUSICA\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void addAllOwnedSongsToTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        List<Music> musicasCompradas = getCurrentUserAsClient().getSongsOwned();

        for (Music song : musicasCompradas) {
            Object[] row = {song.getTitle(), song.getArtist(), song.getGenre(), song.getGenre()};
            if(!songExistsOnTable(model, song)) {
                model.addRow(row);
            }
        }
    }
    public ArrayList<Music> addAllOwnedSongsToTable() {
        ArrayList<Music> musics = new ArrayList<>();
        ArrayList<Music> ownedSongs = (ArrayList<Music>) getCurrentUserAsClient().getSongsOwned();
        for(Music m : ownedSongs){
            if(m.isVisibilidade()) musics.add(m);
        }
        return musics;
    }


    ////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public Musico getCurrentUserAsMusician() {
        return (Musico) currentUser;
    }

    public RockStarDBStatus alterarPreco(String novoPreco, Music music) {
        try{
            double preco = Double.parseDouble(novoPreco);
            music.alterarPreco(preco,music.getArtist());
            saveDB();
            return RockStarDBStatus.DB_MUSIC_PRICE_HAS_CHANGED;
        } catch (NumberFormatException e){
            return RockStarDBStatus.DB_INCORRET_FORMAT_NUMBER;
        }
    }

    public RockStarDBStatus adicionarMusica(String escolhaGenero, String escolhaNome, String escolhaPreco, Album album) {
        double valor = 0;
        if(escolhaNome.isEmpty()){
            return RockStarDBStatus.DB_MUSIC_NAME_EMPTY;
        }else if(validSongName(escolhaNome)){
            try{
                valor = Double.parseDouble(escolhaPreco);
            }
            catch (NumberFormatException e){
                return RockStarDBStatus.DB_INCORRET_FORMAT_NUMBER;
            }
        }
        else if(!validSongName(escolhaNome)) return RockStarDBStatus.DB_MUSIC_NAME_FAILED;

        Music newMusic = new Music(escolhaNome,getCurrentUserAsMusician(),escolhaGenero,valor,album);
        addMusica(newMusic);
        return RockStarDBStatus.DB_MUSIC_ADDED;
    }

    public RockStarDBStatus alterarNome(String escolhaNome, Music music) {
        if(!validSongName(escolhaNome)){
            return RockStarDBStatus.DB_MUSIC_NAME_FAILED;
        }
        else{
            music.alterarTitulo(escolhaNome);
            saveDB();
            return RockStarDBStatus.DB_MUSIC_NAME_HAS_CHANGED;
        }
    }

    public RockStarDBStatus criarAlbum(String escolhaNome, String escolhaGenero) {
        Musico musico = getCurrentUserAsMusician();
        if(!validAlbumName(escolhaNome)){
            return RockStarDBStatus.DB_ALBUM_NAME_FAILED;
        }
        Album novoAlbum = new Album(escolhaNome,getCurrentUserAsMusician(),escolhaGenero);
        addAlbum(novoAlbum);

        return RockStarDBStatus.DB_ALBUM_NAME_HAS_CHANGED;
    }

    public RockStarDBStatus alterarDisponibilidade(Music music, boolean visibilidade) {
        //Verificação do nome de Musico é o mesmo que o do artista da música.
        if(music.getArtist().getUsername().equals(getCurrentUserAsMusician().getUsername())){
            music.setVisibilidade(visibilidade);
            saveDB();
            return RockStarDBStatus.DB_MUSIC_VISIBILITY_CHANGED;
        }else{
            return RockStarDBStatus.DB_MUSIC_VISIBILITY_FAIL;
        }
    }


    //////////////////////////// PESQUISA DE MÚSICAS   \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public ArrayList<Music> procurarMusicas(String nome, CriteriosMusica cm){
        ArrayList<Music> musics = new ArrayList<>();
        ArrayList<Music> songs = new ArrayList<>();
        if(currentUser instanceof Musico) {
            songs = getCurrentUserAsMusician().getMusicas();
        }
        else if(currentUser instanceof Cliente){
            songs = addAllRockstarSongsVisible();
        }

        for(Music m : songs){
            if(cm == CriteriosMusica.NAME){
                if(m.getTitle().toLowerCase().contains(nome.toLowerCase())){
                    musics.add(m);
                }
            }else if(cm == CriteriosMusica.GENRE){
                if(m.getGenre().toLowerCase().contains(nome.toLowerCase())){
                    musics.add(m);
                }
            }
        }
        return musics;
    }
    ////////////////////////////////////////////////////////// ARRAYS DE ORDENAÇÃO DE TABELAS \\\\\\\\\\\\\\\\\\\\\\\\\
    public ArrayList<Music> ordenarMusicasCrescente(CriteriosMusica mo, ArrayList<Music> musics) {
        if (musics.isEmpty()) {
            return new ArrayList<>(); // Não há nada para ordenar ou exibir na tabela
        }
        ArrayList<Music> musicasOrdenadas = new ArrayList<>(musics); //O array será do mesmo tamanho do original (musicas)

        for (int i = 0; i < musicasOrdenadas.size() - 1; i++) {
            for (int j = 0; j < musicasOrdenadas.size() - i - 1; j++) {
                if(mo == CriteriosMusica.NAME) {
                    if (musicasOrdenadas.get(j).getTitle().compareToIgnoreCase(musicasOrdenadas.get(j + 1).getTitle()) > 0) {
                        Music temp = musicasOrdenadas.get(j);
                        musicasOrdenadas.set(j, musicasOrdenadas.get(j + 1));
                        musicasOrdenadas.set(j + 1, temp);
                    }
                }
                else if(mo == CriteriosMusica.GENRE){
                    if (musicasOrdenadas.get(j).getGenre().compareToIgnoreCase(musicasOrdenadas.get(j + 1).getGenre()) > 0) {
                        Music temp = musicasOrdenadas.get(j);
                        musicasOrdenadas.set(j, musicasOrdenadas.get(j + 1));
                        musicasOrdenadas.set(j + 1, temp);
                    }
                }
            }
        }
        return musicasOrdenadas;
    }

    public ArrayList<Music> ordenarMusicasDecrescente(CriteriosMusica mo, ArrayList<Music> musics) {
        if (musics.isEmpty()) {
            return new ArrayList<>(); // Não há nada para ordenar ou exibir na tabela
        }
        ArrayList<Music> musicasOrdenadas = new ArrayList<>(musics); //O array será do mesmo tamanho do original (musicas)

        for (int i = 0; i < musicasOrdenadas.size() - 1; i++) {
            for (int j = 0; j < musicasOrdenadas.size() - i - 1; j++) {
                if(mo == CriteriosMusica.NAME) {
                    if (musicasOrdenadas.get(j).getTitle().compareToIgnoreCase(musicasOrdenadas.get(j + 1).getTitle()) < 0) {
                        Music temp = musicasOrdenadas.get(j);
                        musicasOrdenadas.set(j, musicasOrdenadas.get(j + 1));
                        musicasOrdenadas.set(j + 1, temp);
                    }
                }
                else if(mo == CriteriosMusica.GENRE){
                    if (musicasOrdenadas.get(j).getGenre().compareToIgnoreCase(musicasOrdenadas.get(j + 1).getGenre()) < 0) {
                        Music temp = musicasOrdenadas.get(j);
                        musicasOrdenadas.set(j, musicasOrdenadas.get(j + 1));
                        musicasOrdenadas.set(j + 1, temp);
                    }
                }
            }
        }
        return musicasOrdenadas;
    }




    public boolean rateSong(Music music, double rating) {
        if(getCurrentUserAsClient().hasRatedMusic(music)){ //verifica se já foi avaliada a música
           return false;
        }
        Rating rate = new Rating(rating,getCurrentUserAsClient());

        music.getAvaliacoes().add(rate);

        saveDB();
        return true;
    }
}

