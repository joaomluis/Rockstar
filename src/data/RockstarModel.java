package data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RockstarModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 12565672347L;

    private final List<User> users;

    public RockstarModel() {
        this.users = new ArrayList<>();
    }
    public RockstarModel(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public boolean addUser(User user) {
        return users.add(user);
    }

    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                users.set(i, user);
                break;
            }
        }
    }

    //Métodos para a estatistica
    public int getTotalUsers(){
        if(getUsers()!=null){
            return getUsers().size();
        }
        return -1;
    }
    public int getTotalMusicos() {
        int totalMusicos=0;
        if(getUsers()!=null){
            for(User u: getUsers()){
                if(u instanceof Musico){
                    totalMusicos++;
                }
            }
            return totalMusicos;
        }
        return -1;
    }
//    public int albumPorGenero(String genero){
//        int albumPorGenero = 0;
//        for(Album a: baseDadosAlbuns){
//            if(a.getGenre().toLowerCase().equals(genero.toLowerCase())){
//                albumPorGenero++;
//            }
//        }
//        return albumPorGenero;
//    }
//    public double getValortotalMusicas() {
//        double valorTotalMusicas = 0;
//        if(baseDadosMusicas != null){
//            for(Musica m : baseDadosMusicas){
//                valorTotalMusicas += m.getPreco();
//            }
//            return valorTotalMusicas;
//        }
//        return -1;
//    }
}
