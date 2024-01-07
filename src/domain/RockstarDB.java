package domain;

import data.*;
import ui.musician.CriteriosMusica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 * Responsável pela gestão e manipulação da base de dados da Rockstar.
 */
public class RockstarDB {

    static String dbPath = "baseDadosRockstar.ser";

    private RockstarModel dados;
    private User currentUser;

    public RockstarDB(RockstarModel dados) {
        this.dados = dados;
    }

    public RockstarModel getDados() {
        return dados;
    }

    /**
     * Método que faz arrancar a classe RockstarDB mas primeiro verifica se o ficheiro
     * que guarda os dados existe através do checkIfDBExists(), se existir lê o ficheiro
     * através do loadDB(), se não existir cria o ficheiro como readDB().
     */
    public void init() {
        if (checkIfDBExists()) {
            loadDB();
        } else {
            saveDB();
        }
    }

    /**
     * Verifica se o ficheiro no endereço indicado existe ou não.
     * @return true se existir, false se não.
     */
    private boolean checkIfDBExists() {
        try {
            File f = new File(dbPath);
            return f.exists();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Método com dois própositos, consegue criar um ficheiro para guardar a serialização,
     * quase já exista, quando chamado, o saveDB() escreve no ficheiro as alterações efetuadas
     * tanto do como da plataforma em si.
     */
    private void saveDB() {
        try (FileOutputStream fos = new FileOutputStream(dbPath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(dados);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Método que serve para ler o ficheiro onde estão guardados todos os dados 
     * da plataforma, contem uma verificação que permite confirmar se o tipo de 
     * dados é do tipo RockstarModel, o que faz com que esta aplicação só funcione 
     * com ficheiros que contém esse tipo de dados.
     */
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
            e.printStackTrace();
        }
    }

    
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Método que serve para retornar o user atualamente autenticado na plataforma
     * mas com um cast para tipo de user Cliente para se poder ter acesso a todos 
     * os atributos e métodos dessa classe.
     * @return Um objeto do tipo Cliente.
     */

    public Cliente getCurrentUserAsClient() {
        return (Cliente) getCurrentUser();
    }

    /**
     * Método que serve para retornar o user atualamente autenticado na plataforma
     * mas com um cast para tipo de user Musico para se poder ter acesso a todos
     * os atributos e métodos dessa classe.
     * @return Um objeto do tipo Musico.
     */
    public Musico getCurrentUserAsMusician() {
        return (Musico) currentUser;
    }

    /**
     * Método que serve para guarda uma alteração no ficheiro de dados, especificamente
     * para quando uma alteração é feita num User, chamando o updateUser() que substitui
     * o objeto user guardado na base de dados pelo mesmo objeto mas com as alterações que
     * foram feitas.
     */
    public void saveCurrentUser() {
        dados.updateUser(currentUser);
        saveDB();
    }

    /**
     * Define o atributo currentUser como null ou seja um objeto de um User deixa de estar
     * associado à plataforma enquanto está a correr. Fazendo assim log out da Rockstar.
     */
    public void logOut() {
        currentUser = null;
    }

    /**
     * Método para registar cliente novo. Primeiro acede à lista de user da plataforma,
     * após isso verifica se o username que está a ser tentado registar já existe nessa lista.
     * Se exisitir dá return de um status a avisar isso mesmo, se não existir cria um novo objeto
     * Cliente com os dados passodos nos pârametros e adiciona à lista da plataforma.
     * @param inputUsername String com o username
     * @param inputPassword String com a passaword
     * @return DB_USER_ALREADY_EXISTS status que significa que já existe um user com esse username,
     * DB_USER_ADDED significa que a operação de registo foi bem sucedida, por fim DB_USER_FAILED_TO_SAVE
     * se existiu algum erro inesperado dá este return como "fail safe".
     */
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

    /**
     * Método para registar músico novo. Primeiro acede à lista de user da plataforma,
     * após isso verifica se o username que está a ser tentado registar já existe nessa lista.
     * Se exisitir dá return de um status a avisar isso mesmo, se não existir cria um novo objeto
     * Musico com os dados passodos nos pârametros e adiciona à lista da plataforma.
     * @param inputUsername String com o username
     * @param inputPassword String com a passaword
     * @param inputPin String com o pin
     * @return DB_USER_ALREADY_EXISTS status que significa que já existe um user com esse username,
     * DB_USER_ADDED significa que a operação de registo foi bem sucedida, por fim DB_USER_FAILED_TO_SAVE
     * se existiu algum erro inesperado dá este return como "fail safe".
     */
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

    /**
     * Método que vai percorrer a lista de Users da plataforma e verificar se os
     * dados do input para username e password já existem num Objeto Cliente, se exisitirem
     * os dados desse objeto são atribuídos ao CurrentUser fazendo com que esse Cliente
     * seja o que esteja autenticado na plataforma de momento.
     * @param inputUsername String com input de username
     * @param inputPassword String com input da password
     * @return DB_USER_LOGIN_SUCCESS se o log in for bem sucedido, DB_USER_LOGIN_FAILED se
     * falhar.
     */
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

    /**
     * Método que vai percorrer a lista de Users da plataforma e verificar se os
     * dados do input para username, password e pin já existem num objeto Musico, se exisitirem
     * os dados desse objeto são atribuídos ao CurrentUser fazendo com que esse Cliente
     * seja o que esteja autenticado na plataforma de momento.
     * @param inputUsername String com input de username
     * @param inputPassword String com input da password
     * @param inputPin String com o input do pin
     * @return DB_USER_LOGIN_SUCCESS se o log in for bem sucedido, DB_USER_LOGIN_FAILED se
     * falhar.
     */
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

    /**
     * Método que adiciona uma música às músicas do músico atual e à lista de músicas disponíveis
     * no sistema.
     * Antes de adicionar, verifica se o nome da música é válido usando o método validSongName.
     * Se o nome não for válido, a música não é adicionada.
     *
     * @param music A instância da música a ser adicionada.
     * @return  true se a música for adicionada com sucesso às músicas do músico e à lista de músicas disponíveis;
     *          false se o nome da música não for válido ou a operação falhar.
     */
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
    /**
     * Método que adiciona o álbum à lista de álbuns do músico atual e à lista de todos os álbuns no sistema.
     * Verifica se o usuário atual é um músico antes de adicionar o álbum e se
     * o título do álbum é válido usando o método validAlbumName.
     * Se o título não for válido ou o usuário atual não for um músico, o álbum não é adicionado.
     *
     * @param album O álbum a ser adicionado.
     * @return  true se o álbum for adicionado com sucesso à lista de álbuns do músico e à lista de álbuns no sistema;
     *          false se o título do álbum não for válido, já existir um álbum com o mesmo título na lista de álbuns do músico
     *          ou o usuário atual não for um músico.
     */
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
    /**
     * Método que cria um novo álbum no banco de dados.
     * Este método tenta criar um novo álbum com base nos parâmetros fornecidos.
     * Verifica se o nome do álbum é válido utilizando o método 'validAlbumName'.
     * Se o nome do álbum for válido, cria um novo álbum associado ao músico autenticado e adiciona-o ao banco de dados.
     * @param escolhaNome O nome desejado para o novo álbum.
     * @param escolhaGenero O género do novo álbum.
     * @return RockStarDBStatus.DB_ALBUM_NAME_FAILED se o nome do álbum não for válido;
     *         RockStarDBStatus.DB_ALBUM_NAME_HAS_CHANGED se o álbum foi criado e adicionado ao banco de dados.
     */
    public RockStarDBStatus criarAlbum(String escolhaNome, String escolhaGenero) {
        Musico musico = getCurrentUserAsMusician();
        if(!validAlbumName(escolhaNome)){
            return RockStarDBStatus.DB_ALBUM_NAME_FAILED;
        }
        Album novoAlbum = new Album(escolhaNome,getCurrentUserAsMusician(),escolhaGenero);
        addAlbum(novoAlbum);

        return RockStarDBStatus.DB_ALBUM_NAME_HAS_CHANGED;
    }


    //devolve vetor com os generos das musicas
    ////////////////////////////ESTATISTICAS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    /**
     * Método que retorna o número total de utilizadores no sistema.
     * @return  O número total de utilizadores se a lista de utilizadores não for nula;
     *          -1 caso contrário.
     */
    public int getTotalUsers() {
        if (dados.getUsers() != null) {
            return dados.getUsers().size();
        }
        return -1;
    }
    /**
     * Método que retorna o número total de músicos registados no sistema.
     * @return  O número total de músicos se a lista de utilizadores não for nula;
     *          -1 caso contrário.
     */
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
    /**
     * Método que retorna o número total de músicas disponíveis no sistema.
     * @return  O número total de músicas disponíveis se a lista de músicas não for nula;
     *          -1 caso contrário.
     */
    public int getTotalSongs() {
        List<Music> musics = dados.getAllSongsAvailable();
        if (musics != null) {
            return musics.size();
        } else {
            return -1;
        }
    }
    /**
     * Método que retorna o número total de álbuns disponíveis no sistema.
     * @return  O número total de álbuns disponíveis se a lista de álbuns não for nula;
     *          -1 caso contrário.
     */
    public int getTotaAlbums() {
        List<Album> albums = dados.getAlbums();
        if (albums != null) {
            return albums.size();
        } else {
            return -1;
        }
    }
    /**
     * Método que retorna o valor total das músicas disponíveis no sistema.
     * @return  O valor total das músicas se a lista de músicas não for nula;
     *          -1 caso contrário.
     */
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
    /**
     * Método que conta a quantidade de álbuns de um determinado género.
     * @param genero O género no qual se deseja fazer a contagem.
     * @return O número de álbuns do género especificado;
     *         retorna 0 se não existirem álbuns desse género
     *         ou se os dados ou a lista de álbuns forem nulos.
     */
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
    /**
     * Método que calcula o valor total das músicas vendidas através das compras registadas.
     * Percorre a lista de todas as compras registadas para calcular o valor total das músicas
     * vendidas, somando os preços de todas as compras na variável sum.
     * @return O valor total das músicas vendidas através das compras registadas;
     *         retorna 0 se não houver compras registadas ou se a lista de compras for nula.
     */
    public double getTotalValueSongsSold(){
        List<Purchase> allPurchases = dados.getAllPurchases();
        double sum = 0;
        for (Purchase allPurchase : allPurchases) {
            sum += allPurchase.getPrice();
        }
        return sum;
    }
    /**
     * Método que obtém os álbuns de um músico específico e armazena-os num array de álbuns.
     * Inclui um espaço nulo no início do array para representar a ausência de álbuns, caso não haja nenhum álbum associado ao músico.
     *
     * @param musico O músico do qual se desejam obter os álbuns.
     * @return Um array de álbuns do músico, incluindo um espaço nulo no início se não houverem álbuns
     *         associados a este músico.
     */
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
    /**
     * Método obtem um array contendo as playlists associadas a um cliente em particular.
     * @param cliente O cliente do qual se desejam obter as playlists.
     * @return Um array de playlists do cliente;
     *         retorna um array vazio se não houver playlists associada a este cliente.
     */
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
    /**
     * Método que retorna um vetor contendo os géneros musicais disponíveis no sistema.
     * @return Um vetor de strings representando os géneros musicais disponíveis.
     */
    public String[] getMusicGenrs() {
        Genre[] generos = Genre.values();
        String[] generosMusicais = new String[generos.length];

        for (int i = 0; i < generos.length; i++) {
            generosMusicais[i] = generos[i].toString();
        }
        return generosMusicais;
    }
    /**
     * Método que verifica se o nome de uma música é único para o músico atualmente autenticado.
     * @param nome O nome da música a ser validado.
     * @return true se o nome da música for único para o músico atual e não for nulo nem vazio;
     *         false se o nome da música já existir na lista de músicas do músico ou se for nulo/vazio.
     */
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
    /**
     * Método que verifica se o nome de um álbum é único para o músico atualmente autenticado.
     * @param nome O nome do álbum a ser validado.
     * @return true se o nome do álbum for único para o músico atual e não for nulo nem vazio;
     *         false se o nome do álbum já existir na lista de álbuns do músico ou se for nulo/vazio.
     */
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

    ///////////////////////////PLAYLISTS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Método que verifica se a Playlist passada no parâmetro tem um nome igual a alguma que
     * já exista na conta do cliente através de checkPlaylistNameExists. Se não exisitir a
     * playlist é adicionada à coleção do cliente.
     * @param playlist Playlist que vai ser adicionada à lista de playlists do cliente.
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
     * @param name String com o nome da playlist a ser gerada
     * @param size int com o tamanho da playlist a ser gerada
     * @param genre String com o género da playlist a ser gerada
     * @return São vários tipos do Enum RockStarDBStatus que conforme as condições são respeitadas,
     * ou não, o return varia. Por exemplo, se já existir uma playlist com o nome dado na coleção do user,
     * o return será DB_PLAYLIST_NAME_ALREADY_EXISTS.
     * DB_PLAYLIST_GENERATED_SUCCESSFULLY se a playlist for gerada e cumprir todos os requisitos
     * DB_PLAYLIST_GENERATED_BUT_WITHOUT_WANTED_SIZE foi gerada uma playlist, mas sem o tamanho pretendido
     * pelo user pois não foram encontradas mais músicas do género que o user pretendia.
     * DB_NO_SONGS_ADDED_TO_PLAYLIST não foram encontradas músicas que respeitavam os requisitos
     * logo não foi gerada a playlist
     * DB_SOME_FIELD_IS_EMPTY algum campo ficou vazio logo não foi gerada uma playlist
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
                            saveDB();
                            return RockStarDBStatus.DB_PLAYLIST_GENERATED_SUCCESSFULLY;
                        }
                    }
                }
                if (songsAdded > 0) {
                    currentClient.addPlaylistToClient(newPlaylist);
                    saveDB();
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
     * @param playlist Playlist a verificar se já tem a música em questão
     * @param song Música que se está a verificar se já pertence à playlist
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
                return true;
            }
        }
        return false;
    }

    /**
     * Adiciona uma música a uma playlist, mas antes verifica se esse música já
     * existe dentro da playlist que está a receber como parâmetro, se já existe
     * não adiciona.
     * @param music Musica a adicionar à playlist
     * @param playlist Playlist que é verificada e pode receber a música que o método
     *                 recebe
     * @return DB_MUSIC_ALREADY_EXISTS_IN_THE_PLAYLIST se a playlist já exisitir na playlist
     * DB_MUSIC_ADDED se a musica for adicionada com sucesso
     * DB_MUSIC_CANT_BE_ADDED_TO_PLAYLISTS se ocorrer algum erro e a musica não for adicionada
     */
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

    /**
     * Método que recebe uma playlist e verifica o estado da visibilidade dela
     * e depois altera-o.
     *
     * @param newVis Boolean com o estado de visibilidade a ser atribuido à Playlist
     * @param playlist Playlist que se pretende alterar o estado de visibilidade
     * @return DB_PLAYLIST_VISIB_CHANGED se houver mudança no estado de visibilidade
     * DB_PLAYLIST_VISIB_UNCHANGED se o estado ficar na mesma
     */
    public RockStarDBStatus changePlaylistVisibility(boolean newVis, Playlist playlist) {
        boolean visibilityBeforeChange = playlist.getVisibilidade();
        playlist.setVisibilidade(newVis);

        if (playlist.getVisibilidade() != visibilityBeforeChange) {
            saveDB();
            return RockStarDBStatus.DB_PLAYLIST_VISIB_CHANGED;
        }
        return RockStarDBStatus.DB_PLAYLIST_VISIB_UNCHANGED;
    }



    //////////////////Adicionar histórico preços à JDialog\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Percorre a ArrayList com os preços da musica e adiciona-os à JTable
     * na interface depois dos atributos serem convertidos num objeto.
     * @param song Musica que se pretende retirar o histórico de preços
     * @param table Tabela a que se vai juntar os vários preços
     */
    public void addPriceHistoryToTable(Music song, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        List<Price> priceList = song.getHistoricoPreco();

        for (Price price: priceList) {
            Object[] row = {formatLocalDateTime(price.getData()), String.format("%1$,.2f€", price.getPreco())};
                model.addRow(row);
        }
    }

    //////////////////////Histórico Compras\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Percorre a ArrayList com o histórico de compras do cliente e adiciona-os à JTable
     * na interface depois dos atributos serem convertidos num objeto. Também verifica se
     * essa compra já se encontra na tabela antes de adicionar.
     * @param table Tabela a que se vai juntar as várias compras
     */
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
     * @param dateTime Variavel LocalDateTime a ser formatada
     * @return String com uma variável LocalDateTime formatada
     */
    private String formatLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    /**
     * Verifica na tabela já possui a compra que é passada no parâmetro, faz essa
     * verificação através do ID da compra.
     * @param model Modelo da tabela onde vai ser feita a verificação
     * @param purchase Compra que vai ser verificada
     * @return true se a compra já está na tabela, false se não
     */
    private boolean purchaseExistsOnTable(DefaultTableModel model, Purchase purchase) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 0).equals(purchase.getPurchaseId())) {
                return true;
            }
        }
        return false;
    }


    ///////////////////////////LOJA\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Método para adicionar todas as músicas da plataforma para a tabela da UI,
     * formata os atributos da música para uma visualização mais fácil e verifica
     * se a música já se encontra na tabela. De notar que a ArrayList passada no
     * parâmetro só vai conter músicas com visibilidade true
     * @param table Tabela onde as musicas vão ser adicionadas
     * @param musics ArrayList das músicas a adicionar
     */
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

    /**
     * Método para verificar se determinada música já se encontra numa tabela
     * que é referenciada nos parâmetros. É chamado noutros métodos que fazem
     * a adição à tabela propriamente dita.
     * @param model Modelo da tabela para se verificar
     * @param song Musica que se pretende verificar se já está na tabela
     * @return true se a musica já estiver na tabela, false se não
     */
    private boolean songExistsOnTable(DefaultTableModel model, Music song) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 0).equals(song.getTitle())) {
                return true;
            }
        }
        return false;
    }

    //////////////////////////////////CARRINHO\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Adiciona a música passada no parâmetro à ArrayList do carrinho de compras,
     * faz verificações ao chamar outros métodos para ver se a música já se encontra
     * no carrinho para não se repetir e se o cliente já comprou a música antes,
     * impedindo que compre algo que já comprou anteriormente.
     * @param song Música que é verificada e adicionada ao carrinho se caso disso
     * @return DB_SONG_ALREADY_IN_CART a música já está no carrinho
     * DB_SONG_ALREADY_BOUGHT o cliente já comprou a música em questão
     * DB_SONG_ADDED_TO_CART a música foi adicionada com sucesso ao carrinho
     */
    public RockStarDBStatus addSongToCart(Music song) {
        if (isSongOnCart(song)) {
            return RockStarDBStatus.DB_SONG_ALREADY_IN_CART;
        } else if (isSongAlreadyOwned(song)) {
            return RockStarDBStatus.DB_SONG_ALREADY_BOUGHT;
        } else {
            getCurrentUserAsClient().getSongsInCart().add(song);
            saveCurrentUser();
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

    /**
     * Adiciona todas as músicas na ArrayList do carrinho de compras para
     * a JTable que as vai representar na interface. Chama um método para impedir
     * que se repitam na tabela
     * @param table Tabela onde se pretende adicionar as músicas
     */
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
     *Método que vai passar todas as músicas no carrinho de compras para um
     * objeto compra um depois as musicas são adicionadas à ArrayList de músicas
     * que cada compra tem e adicionadas às músicas do cliente se a compra for bem
     * sucedida. Faz uma série de verificações desde veriricar se o carrinho está
     * vazio, se o user tem saldo suficiente para concluir a compra, deduz o custo
     * total da compra do saldo do cliente e adiciona a compra ao histórico de
     * compras do cliente.
     * @return DB_SONGS_PURCHASED_SUCCESSFULLY se a compra foi executada com sucesso
     * DB_INSUFFICIENT_BALANCE se o cliente não tem saldo suficiente
     * DB_CART_EMPTY o carrinho está vazio
     */
    public RockStarDBStatus buyAllSongsFromCart() {

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
                saveDB();

                return RockStarDBStatus.DB_SONGS_PURCHASED_SUCCESSFULLY;
            } else {
                return RockStarDBStatus.DB_INSUFFICIENT_BALANCE;
            }
        }
        return RockStarDBStatus.DB_CART_EMPTY;
    }


    /**
     * Adiciona a música à lista de músicas que o cliente possui.
     * @param song Musica a adicionar à lista de músicas
     */
    private void addSongToMyMusic(Music song) {
        getCurrentUserAsClient().getSongsOwned().add(song);
    }

    ////////////////////////////////////MINHA MUSICA\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    /**
     * Método para adicionar todas as músicas que estão na Lista de musicas compradas
     * do cliente à tabela na UI que as vai apresentar.
     * @param table Tabela que vai receber objetos que contêm os dados das musicas a apresentar
     */
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

    ////////////////////////////////////ALTERAÇÕES MÚSICAS\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    /**
     * Método que altera o preço de uma música no banco de dados.
     * Este método tenta alterar o preço de uma música para o valor fornecido como string.
     * O try/catch serve para evitar que o programa tente converter uma string que não tenha correspondência a um número.
     * Converte a string para um valor double e utiliza o método 'alterarPreco' da música para atualizar o preço.
     * Em caso de sucesso na alteração, salva as mudanças no banco de dados.
     * @param novoPreco A nova string de preço a ser atribuída à música.
     * @param music A música à qual se deseja alterar o preço.
     * @return RockStarDBStatus.DB_MUSIC_PRICE_HAS_CHANGED se o preço da música foi alterado com sucesso;
     *         RockStarDBStatus.DB_INCORRET_FORMAT_NUMBER se a string de preço não pôde ser convertida para um número.
     */
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
    /**
     * Método que adiciona uma nova música ao banco de dados.
     * Este método tenta adicionar uma nova música ao banco de dados com base nos parâmetros fornecidos.
     * Verifica se o nome da música não está vazio, se o nome retornar true usando o método 'validSongName'
     * Verifica se é possivel fazer corresponder essa string a um número, alterando se o for.
     *
     * @param escolhaGenero O género da música a ser adicionada.
     * @param escolhaNome O nome da música a ser adicionada.
     * @param escolhaPreco O preço da música a ser adicionada.
     * @param album O álbum ao qual a música será associada.
     * @return RockStarDBStatus.DB_MUSIC_NAME_EMPTY se o nome da música estiver vazio;
     *         RockStarDBStatus.DB_INCORRET_FORMAT_NUMBER se o formato do preço não for válido;
     *         RockStarDBStatus.DB_MUSIC_NAME_FAILED se o nome da música não for válido;
     *         RockStarDBStatus.DB_MUSIC_ADDED se a música for adicionada com sucesso ao banco de dados.
     */
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
    /**
     * Método que altera o nome de uma música no banco de dados.
     * Este método tenta alterar o nome de uma música para o nome fornecido, verificando se o novo nome é válido utilizando o método 'validSongName'.
     * Se o novo nome for válido, o método 'alterarTitulo' da música é invocado para realizar a alteração.
     * Em seguida, as mudanças são salvas no banco de dados.
     *
     * @param escolhaNome O novo nome desejado para a música.
     * @param music A música à qual se deseja alterar o nome.
     * @return RockStarDBStatus.DB_MUSIC_NAME_FAILED se o novo nome não for válido;
     *         RockStarDBStatus.DB_MUSIC_NAME_HAS_CHANGED se o nome da música foi alterado com sucesso.
     */
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
    /**
     * Método que altera a disponibilidade de uma música no banco de dados.
     * Este método tenta alterar a disponibilidade de uma música, definindo sua visibilidade com base no parâmetro fornecido.
     * Verifica se o músico autenticado é o mesmo que o músico associado à música que está sendo alterada.
     * Se for, atualiza a visibilidade da música
     * e salva as mudanças no banco de dados.
     *
     * @param music A música cuja visibilidade será alterada.
     * @param visibilidade O valor booleano que define a visibilidade da música.
     * @return RockStarDBStatus.DB_MUSIC_VISIBILITY_CHANGED se a visibilidade da música foi alterada com sucesso;
     *         RockStarDBStatus.DB_MUSIC_VISIBILITY_FAIL se o músico autenticado não é o mesmo que o músico da música.
     */
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

    /**
     * Método avalia uma música com uma classificação (rating) fornecida pelo cliente autenticado.
     * Verifica se o cliente já avaliou a música anteriormente.
     * Se não, cria um objeto 'Rating' com a classificação e o cliente associado, e adiciona essa avaliação à lista de avaliações da música.
     * Em seguida, salva as alterações no banco de dados.
     *
     * @param music A música a ser avaliada.
     * @param rating A classificação (rating) a ser atribuída à música.
     * @return true se a música foi avaliada com sucesso e a avaliação foi adicionada;
     *         false se o cliente já tiver avaliado a música anteriormente.
     */
    public boolean rateSong(Music music, double rating) {
        if(getCurrentUserAsClient().hasRatedMusic(music)){ //verifica se já foi avaliada a música
            return false;
        }
        Rating rate = new Rating(rating,getCurrentUserAsClient());

        music.getAvaliacoes().add(rate);

        saveDB();
        return true;
    }

    //////////////////////////// PESQUISA DE MÚSICAS   \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    /**
     * Método que procura músicas com base em critérios específicos.
     * Este método procura músicas com base no nome ou género fornecido, utilizando os critérios definidos no Enum.
     * Usando o instance of distingue-se o tipo de user.
     * Se o utilizador autenticado for um músico, procura nas suas músicas.
     * Se for um cliente, procura em todas as músicas visíveis na plataforma RockStar.
     * Percorre a lista de músicas de acordo com o tipo de user e adiciona-as à lista que vai ser retornada.
     * Este método apenas serve para as músicas do músico e as da loja do cliente.
     * @param nome O nome ou parte do nome da música a ser procurada.
     * @param cm O critério de pesquisa a ser aplicado (CriteriosMusica.Nome ou CriteriosMusica.Genero).
     * @return Uma lista de músicas que correspondem aos critérios de pesquisa definidos.
     */
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
            if(cm == CriteriosMusica.Nome){
                if(m.getTitle().toLowerCase().contains(nome.toLowerCase())){
                    musics.add(m);
                }
            }else if(cm == CriteriosMusica.Genero){
                if(m.getGenre().toLowerCase().contains(nome.toLowerCase())){
                    musics.add(m);
                }
            }
        }
        return musics;
    }
    /**
     * Método para pesquisar músicas que o Cliente já comprou pelo nome ou pelo género.
     * É verificado se existe alguma música que contenha o que for colocado na string que
     * recebe o input, se existir essa música é adicionada a uma ArrayList que depois é
     * devolvida com todas as "matches" que foram encontradas com o input.
     * @param pesquisa String com o input que vai ser pesquisado na lista de musicas
     * @param cm Um Enum que contém os vários tipos filtros que podem ser aplicados, neste
     * caso nome e genero
     * @return ArrayList com todas as musicas que tiverem o input colocado na pesquisa.
     */
    public ArrayList<Music> procurarMinhasMusicasCliente(String pesquisa, CriteriosMusica cm) {
        List<Music> clientSongs = getCurrentUserAsClient().getSongsOwned();
        ArrayList<Music> searchResult = new ArrayList<>();

        for (Music music : clientSongs) {
            if(cm == CriteriosMusica.Nome){
                if(music.getTitle().toLowerCase().contains(pesquisa.toLowerCase())){
                    searchResult.add(music);
                }
            }else if(cm == CriteriosMusica.Genero){
                if(music.getGenre().toLowerCase().contains(pesquisa.toLowerCase())){
                    searchResult.add(music);
                }
            }
        }
        return searchResult;
    }

    //////////////////////////// ORDENAÇÕES CRESCENTE / DECRESCENTE   \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    /**
     * Método que ordena uma lista de músicas em ordem crescente com base em critérios específicos.
     * Este método ordena uma lista de músicas em ordem alfabética crescente, com base nos critérios definido (por nome ou género).
     * Se a lista de músicas estiver vazia, retorna uma lista vazia.
     * Caso contrário, realiza a ordenação das músicas e retorna a lista ordenada.
     *
     * @param mo O critério de ordenação a ser aplicado (CriteriosMusica.Nome ou CriteriosMusica.Genero).
     * @param musics A lista de músicas a ser ordenada.
     * @return Uma lista de músicas ordenada em ordem crescente com base nos critérios fornecidos;
     *         retorna uma lista vazia se não houver músicas para ordenar.
     */
    public ArrayList<Music> ordenarMusicasCrescente(CriteriosMusica mo, ArrayList<Music> musics) {
        if (musics.isEmpty()) {
            return new ArrayList<>(); // Não há nada para ordenar ou exibir na tabela
        }
        ArrayList<Music> musicasOrdenadas = new ArrayList<>(musics); //O array será do mesmo tamanho do original (musicas)

        for (int i = 0; i < musicasOrdenadas.size() - 1; i++) {
            for (int j = 0; j < musicasOrdenadas.size() - i - 1; j++) {
                if(mo == CriteriosMusica.Nome) {
                    if (musicasOrdenadas.get(j).getTitle().compareToIgnoreCase(musicasOrdenadas.get(j + 1).getTitle()) > 0) {
                        Music temp = musicasOrdenadas.get(j);
                        musicasOrdenadas.set(j, musicasOrdenadas.get(j + 1));
                        musicasOrdenadas.set(j + 1, temp);
                    }
                }
                else if(mo == CriteriosMusica.Genero){
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
    /**
     * Método que ordena uma lista de músicas em ordem decrescente com base em critérios específicos.
     * Este método ordena uma lista de músicas em ordem alfabética decrescente, com base nos critérios definido (por nome ou género).
     * Se a lista de músicas estiver vazia, retorna uma lista vazia.
     * Caso contrário, realiza a ordenação das músicas e retorna a lista ordenada.
     *
     * @param mo O critério de ordenação a ser aplicado (CriteriosMusica.Nome ou CriteriosMusica.Genero).
     * @param musics A lista de músicas a ser ordenada.
     * @return Uma lista de músicas ordenada em ordem crescente com base nos critérios fornecidos;
     *         retorna uma lista vazia se não houver músicas para ordenar.
     */
    public ArrayList<Music> ordenarMusicasDecrescente(CriteriosMusica mo, ArrayList<Music> musics) {
        if (musics.isEmpty()) {
            return new ArrayList<>(); // Não há nada para ordenar ou exibir na tabela
        }
        ArrayList<Music> musicasOrdenadas = new ArrayList<>(musics); //O array será do mesmo tamanho do original (musicas)

        for (int i = 0; i < musicasOrdenadas.size() - 1; i++) {
            for (int j = 0; j < musicasOrdenadas.size() - i - 1; j++) {
                if(mo == CriteriosMusica.Nome) {
                    if (musicasOrdenadas.get(j).getTitle().compareToIgnoreCase(musicasOrdenadas.get(j + 1).getTitle()) < 0) {
                        Music temp = musicasOrdenadas.get(j);
                        musicasOrdenadas.set(j, musicasOrdenadas.get(j + 1));
                        musicasOrdenadas.set(j + 1, temp);
                    }
                }
                else if(mo == CriteriosMusica.Genero){
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

}

