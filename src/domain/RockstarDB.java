package domain;

import data.Cliente;
import data.Musico;
import data.RockstarModel;
import data.User;

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
}

