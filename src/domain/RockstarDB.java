package domain;

import data.*;
import ui.musician.CriteriosMusica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
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

        // dbInitialized = true;
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
            if (user.getUsername().equals(inputUsername)) {
                if (user.getPassword().equals(inputPassword)) {
                    currentUser = user;
                    return RockStarDBStatus.DB_USER_LOGIN_SUCCESS;
                } else {
                    return RockStarDBStatus.DB_USER_LOGIN_FAILED;
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
        dados.getSongs().add(music);
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
        List<Music> musics = dados.getSongs();
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
        if (dados.getSongs() != null) {
            for (Music m : dados.getSongs()) {
                valorTotalMusicas += m.getPreco();
            }
            return valorTotalMusicas;
        }
        return -1;
    }

    public String[] getMusicianAlbums(Musico musico) {
        int aux = musico.getAlbuns().size() + 1;
        String[] dropDown = new String[aux];
        dropDown[0] = "Sem Album";
        int i = 1;
        for (Album a : musico.getAlbuns()) {
            dropDown[i] = a.getTitle();
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

    public boolean addPlaylist(Playlist playlist) {

        Cliente cliente = getCurrentUserAsClient();

        if (checkPlaylistNameExists(playlist)) {
            return false;
        }
        cliente.addPlaylistToClient(playlist);
        return true;
    }

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


    private boolean checkIfSongAlreadyAdded (Playlist playlist, Music song) {
        String songIdentifier = getSongIdentifier(song);

        for (Music playlistMusic : playlist.getMusic()) {
            String playlistMusicIdentifier = getSongIdentifier(playlistMusic);

            if (playlistMusicIdentifier.equals(songIdentifier)) {
                return true; // Song already exists in the playlist
            }
        }
        return false; // Song doesn't exist in the playlist
    }

    // Define a method to count the number of songs of a specific genre
    private int countSongsByGenre(List<Music> songs, String genre) {
        int count = 0;
        for (Music music : songs) {
            if (music.getGenre().equals(genre)) {
                count++;
            }
        }
        return count;
    }

    // Define a method to generate a unique identifier for a song based on title and artist
    private String getSongIdentifier(Music music) {
        return music.getTitle() + "-" + music.getArtist();
    }

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


    ///////////////////////////LOJA\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void addAllRockstarSongsToTable(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        List<Music> musicasPlataforma = dados.getSongs();

        for (Music song : musicasPlataforma) {
            if(song.isVisibilidade()) {
                Object[] row = {song.getTitle(), song.getArtist(), song.getGenre(),String.format("%1$,.2f€", song.getPreco())};
                if(!existeMusicaNaTabela(model, song)) {
                    model.addRow(row);
                }
            }
        }
    }

    private boolean existeMusicaNaTabela(DefaultTableModel model, Music song) {
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
            if(!existeMusicaNaTabela(model, song)) {
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
            if(song.isVisibilidade()) {
                Object[] row = {song.getTitle(), song.getArtist(), song.getGenre(), song.getGenre()};
                if(!existeMusicaNaTabela(model, song)) {
                    model.addRow(row);
                }
            }
        }
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

    public RockStarDBStatus adicionarMusica(String escolhaGenero, String escolhaNome, String escolhaPreco) {
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

        Music newMusic = new Music(escolhaNome,getCurrentUserAsMusician(),escolhaGenero,valor);
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


    ////////////////////////////   \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public ArrayList<Music> procurarMusicas(String nome, CriteriosMusica cm){
        ArrayList<Music> musics = new ArrayList<>();
        ArrayList<Music> songs = new ArrayList<>();
        if(currentUser instanceof Musico) {
            songs = getCurrentUserAsMusician().getMusicas();
        }
        else if(currentUser instanceof Cliente){
            songs = (ArrayList<Music>) getDados().getSongs();
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


}

