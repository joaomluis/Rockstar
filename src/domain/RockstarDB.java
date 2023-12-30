package domain;

import data.*;

import java.io.*;
import java.util.ArrayList;
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
        if (currentUser instanceof Musico) {
            ArrayList<Music> musicianMusic = ((Musico) currentUser).getMusicas();
            if (musicianMusic != null) {
                validSongName(music.getTitle());
            }
        }
        dados.getMusics().add(music);
        return true;
    }
    public boolean addAlbum(Album album){
        if(currentUser instanceof Musico) {
            ArrayList<Album> albunsDomusico = ((Musico) currentUser).getAlbuns();
            if (albunsDomusico != null) {
                for (Album a : albunsDomusico) {
                    if (a.getTitle().equalsIgnoreCase(album.getTitle())) {
                        return false;
                    }
                }
            }
            dados.getAlbums().add(album);
        }
        return true;
    }



    //devolve vetor com os generos das musicas
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
        int albumPorGenero = 0;
        for(Album a: dados.getAlbums()){
            if(a.getGenre().equalsIgnoreCase(genero)){
                albumPorGenero++;
            }
        }
        return albumPorGenero;
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
        if(currentUser instanceof Musico) {
            for (Music m : ((Musico) currentUser).getMusicas()) {
                if (m.getTitle().equalsIgnoreCase(nome)) {
                    return false;
                }
            }
        }
        return true;
    }

    //falta método para calcular o valor total de músicas vendidas.
}

