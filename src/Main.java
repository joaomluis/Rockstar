import data.RockstarModel;
import domain.RockstarDB;
import ui.RockstarGUI;

public class Main {
    public static void main(String[] args) {
        RockstarModel dados = new RockstarModel();
        RockstarDB db = new RockstarDB(dados);
        db.init();

        RockstarGUI ui = new RockstarGUI(db);
        ui.start();
        System.out.println("bora!");
    }
}