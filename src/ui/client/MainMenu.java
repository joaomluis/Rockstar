package ui.client;

import data.Cliente;
import data.Music;
import data.Playlist;
import data.Purchase;
import ui.RockstarGUI;
import ui.client.popups.GeneratePlaylist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel implements ActionListener {

    public static String TITLE = "Client MENU";

    RockstarGUI gui;
    private final Cliente client;

    private JButton myMusic;
    private JButton myPlaylists;
    private JButton createPlaylist;
    private JButton purchaseHistory;
    private JButton logOut;
    private JButton store;
    private JLabel title;

    public MainMenu(RockstarGUI gui) {
        this.gui = gui;
        client = (Cliente) gui.getDb().getCurrentUser();

        setLayout(null);
        setBackground(new Color(20,64,88));

        //Label principal do painel
        title = new JLabel();
        title.setText("Welcome to RockStar");
        title.setForeground(new Color(198,107,61));
        title.setFont(new Font("Arial", Font.BOLD, 33));
        title.setBounds(180, 35, 400, 40);

        //botão para aceder ao menu das músicas do cliente
        myMusic = new JButton();
        myMusic.setBounds(213, title.getY() + 90, 280, 35);
        myMusic.setForeground(Color.black);
        myMusic.setFont(new Font("Arial", Font.BOLD, 15));
        myMusic.setText("Minhas músicas");
        myMusic.setFocusable(false);
        myMusic.addActionListener(this);

        //botão para aceder ao menu das playlists do cliente
        myPlaylists = new JButton();
        myPlaylists.setBounds(myMusic.getX(), myMusic.getY() + 50, 280, 35);
        myPlaylists.setForeground(Color.black);
        myPlaylists.setFont(new Font("Arial", Font.BOLD, 15));
        myPlaylists.setText("Minhas playlists");
        myPlaylists.setFocusable(false);
        myPlaylists.addActionListener(this);

        // botão para mostrar pop up que vai gerar playlist
        createPlaylist = new JButton();
        createPlaylist.setBounds(myPlaylists.getX(), myPlaylists.getY() + 50, 280, 35);
        createPlaylist.setForeground(Color.black);
        createPlaylist.setFont(new Font("Arial", Font.BOLD, 15));
        createPlaylist.setText("Gerar playlist");
        createPlaylist.setFocusable(false);
        createPlaylist.addActionListener(this);

        //botão para aceder ao histórico de compras
        purchaseHistory = new JButton();
        purchaseHistory.setBounds(createPlaylist.getX(), createPlaylist.getY() + 50, 280, 35);
        purchaseHistory.setForeground(Color.black);
        purchaseHistory.setFont(new Font("Arial", Font.BOLD, 15));
        purchaseHistory.setText("Histórico de compras");
        purchaseHistory.setFocusable(false);
        purchaseHistory.addActionListener(this);

        //Botão para aceder à loja
        store = new JButton();
        store.setFocusable(false);
        store.setBounds(purchaseHistory.getX(), purchaseHistory.getY() + 50, 280, 35);
        store.setText("Loja");
        store.setFont(new Font("Arial", Font.BOLD, 15));
        store.setForeground(Color.black);
        store.addActionListener(this);

        //botão para fazer log out
        logOut = new JButton();
        logOut.setBounds(store.getX(), store.getY() + 50, 280, 35);
        logOut.setForeground(Color.black);
        logOut.setFont(new Font("Arial", Font.BOLD, 15));
        logOut.setText("Log out");
        logOut.setFocusable(false);
        logOut.addActionListener(this);

        add(title);
        add(store);
        add(myMusic);
        add(myPlaylists);
        add(createPlaylist);
        add(purchaseHistory);
        add(logOut);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (e.getSource() == store) {
            gui.showStore();
        }
        if (e.getSource() == myMusic) {
            gui.showMyMusic();
            gui.updateMyMusicTable(gui.getMyMusicTableModel(), gui.getMyMusicTable());
        }
        if (e.getSource() == myPlaylists) {
            gui.showMyPlaylists();
            for (Playlist playlist : gui.getDb().getCurrentUserAsClient().getPlaylists()) {
                System.out.println(playlist);
            }
        }
        if(e.getSource() == createPlaylist) {
            new GeneratePlaylist(gui, parent);
        }
        if (e.getSource() == purchaseHistory) {
            gui.updatePurchaseTable(gui.getPurchaseHistoryTableModel(), gui.getPurchaseHistoryTable()); // NAO CRIOU COMPRA????????????
            gui.showPurchaseHistory();
        }
        if (e.getSource() == logOut) {
            gui.getDb().logOut();
            gui.showAutenticationFrame();
        }
    }
}