package domain;

import data.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.List;

public class RockstarDB {

    static String dbPath = "baseDadosRockstar.ser";
    private int commit;

    private RockstarModel dados;
    private User currentUser;

    public RockstarDB(RockstarModel dados) {
        this.dados = dados;
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
        dados.getMusics().add(music);
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

    public int getTotalUsers(){
        if(dados.getUsers()!=null){
            return dados.getUsers().size();
        }
        return -1;
    }
    public int getTotalMusician() {
        int totalMusicos=0;
        if(dados.getUsers()!=null){
            for(User u: dados.getUsers()){
                if(u instanceof Musico){
                    totalMusicos++;
                }
            }
            return totalMusicos;
        }
        return -1;
    }
    public int getTotalSongs() {
        List<Music> musics = dados.getMusics();
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
        if(dados.getMusics() != null){
            for(Music m : dados.getMusics()){
                valorTotalMusicas += m.getPreco();
            }
            return valorTotalMusicas;
        }
        return -1;
    }
    public String[] getMusicianAlbums(Musico musico){
        int aux = musico.getAlbuns().size()+1;
        String[] dropDown = new String[aux];
        dropDown[0] = "Sem Album";
        int i=1;
        for(Album a : musico.getAlbuns()){
            dropDown[i] = a.getTitle();
            i++;
        }
        return dropDown;
    }



    //devolve vetor com a quantidade de albuns de X genero
    public int albumByGenre(String genero){
        int albumByGenre = 0;
        //verifica se dados é null ou os albums são null antes de percorrer os albums
        if (dados != null && dados.getAlbums() != null) {
            for(Album a: dados.getAlbums()){
                if(a.getGenre().equalsIgnoreCase(genero)){
                    albumByGenre++;
                }
            }
        }
        return albumByGenre;
    }
    //lê os generos de musicas que há disponiveis
    public String[] getMusicGenrs() {
        Gender[] generos = Gender.values();
        String[] generosMusicais = new String[generos.length];

        for (int i = 0; i < generos.length; i++) {
            generosMusicais[i] = generos[i].toString();
        }
        return generosMusicais;
    }



    //verifica se existe um nome igual na lista de músicas
    public boolean validSongName(String nome){
        if(currentUser instanceof Musico && getCurrentUserAsMusician().getMusicas() != null && nome != null && !nome.isEmpty()) {
            for (Music m : ((Musico) currentUser).getMusicas()) {
                if (m.getTitle().equalsIgnoreCase(nome)) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean validAlbumName(String nome){
        if(currentUser instanceof Musico && getCurrentUserAsMusician().getMusicas() != null && nome != null && !nome.isEmpty()) {
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

    public boolean addPlaylist (Playlist playlist) {
        if (currentUser instanceof Cliente){
            Cliente cliente = getCurrentUserAsClient();

            if (cliente.getPlaylists().stream().anyMatch(p -> p.getNome().equalsIgnoreCase(playlist.getNome()))) {
                return false;
            }
            cliente.addPlaylistToClient(playlist);
            return true;
        }
        return false;
    }

    public boolean adicionarElementosTabela(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Cliente cliente = getCurrentUserAsClient();
        List<Playlist> playlistCliente = cliente.getPlaylists();

        try {
            for (Playlist playlist : playlistCliente) {
                Object[] row = {playlist.getNome(), playlist.isVisibilidade()};
                if (!existePlaylistNaTabela(model, playlist)) {
                    model.addRow(row);
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean existePlaylistNaTabela(DefaultTableModel model, Playlist playlist) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 0).equals(playlist.getNome())) {
                return true; // Playlist já existe na tabela
            }
        }
        return false; // Playlist não encontrada na tabela
    }

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

}

