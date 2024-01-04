package ui;

import data.Cliente;
import data.Playlist;
import domain.RockstarDB;
import ui.client.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientRootFrame extends JFrame implements ActionListener {

    public static String TITLE = "Cliente";

    RockstarGUI gui;
    private final Cliente client;

    private JButton homeButton;
    private JButton backButton;
    private JButton cartButton;
    private JLabel username;
    private JLabel balance;
    private JPanel homeButtonPanel;
    private JPanel panelContainer;
    private CardLayout cardLayout;
    private MainMenu menuInicial;
    private Store store;
    private MyMusic myMusic;
    private MyPlaylists myPlaylists;
    private CurrentPlaylist currentPlaylist;
    private PurchaseHistory purchaseHistory;
    private PurchaseDetails purchaseDetails;
    private ShoppingCart shoppingCart;
    private JPanel currentPanel;


    public ClientRootFrame(RockstarGUI rockstarGUI) {
        this.gui = rockstarGUI;
        client = (Cliente) gui.getDb().getCurrentUser();
        this.currentPanel = new JPanel();
    }

    public MyPlaylists getMyPlaylists() {
        return myPlaylists;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public MyMusic getMyMusic() {
        return myMusic;
    }

    public PurchaseHistory getPurchaseHistory() {
        return purchaseHistory;
    }

    public PurchaseDetails getPurchaseDetails() {
        return purchaseDetails;
    }

    public CurrentPlaylist getCurrentPlaylist() {
        return currentPlaylist;
    }

    public MainMenu getMenuInicial() {
        return menuInicial;
    }

    public Store getStore() {
        return store;
    }

    public void start() {
        RockstarDB db = gui.getDb();

        /// TODO: double check there is a current user

        ImageIcon logoRockStar = new ImageIcon("logo_2.png");

        //especificações básicas do frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setTitle("RockStar.Inc - Cliente");
        setIconImage(logoRockStar.getImage());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);


        // Criação de card layout para implementar os vários paineis
        panelContainer = new JPanel();
        cardLayout = new CardLayout();
        panelContainer.setLayout(cardLayout);

        //Inicialização dos vários paineis
        menuInicial = new MainMenu(gui);
        store = new Store(gui);
        myMusic = new MyMusic(gui);
        myPlaylists = new MyPlaylists(gui);
        purchaseHistory = new PurchaseHistory(gui);
        shoppingCart = new ShoppingCart(gui);
        currentPlaylist = new CurrentPlaylist(gui);
        purchaseDetails = new PurchaseDetails(gui);


        //Junção dos paines ao card layout
        panelContainer.add(menuInicial, MainMenu.TITLE);
        panelContainer.add(store, Store.TITLE);
        panelContainer.add(myMusic, MyMusic.TITLE);
        panelContainer.add(myPlaylists, MyPlaylists.TITLE);
        panelContainer.add(purchaseHistory, PurchaseHistory.TITLE);
        panelContainer.add(shoppingCart, ShoppingCart.TITLE);
        panelContainer.add(currentPlaylist, CurrentPlaylist.TITLE);
        panelContainer.add(purchaseDetails, PurchaseDetails.TITLE);


        //Painel que fica no topo com os botões
        homeButtonPanel = new JPanel();
        homeButtonPanel.setLayout(null);
        homeButtonPanel.setPreferredSize(new Dimension(0, 40));
        homeButtonPanel.setBackground(new Color(20, 64, 88));

        //Botão retroceder
        backButton = new JButton();
        backButton.setBounds(10, 5, 60, 25);
        backButton.setText("←");
        backButton.setFont(new Font("Arial", Font.BOLD, 26));
        backButton.setFocusable(false);
        backButton.addActionListener(this);

        //Botão home
        homeButton = new JButton();
        homeButton.setBounds(backButton.getX() + 70, backButton.getY(), 60, 25);
        homeButton.setText("⌂");
        homeButton.setFont(new Font("Arial", Font.BOLD, 26));
        homeButton.setFocusable(false);
        homeButton.addActionListener(this);

        //Botão carrinho de compras
        cartButton = new JButton();
        cartButton.setBounds(620, 5, 60, 25);
        cartButton.setText("\uD83D\uDED2");
        cartButton.setFocusable(false);
        cartButton.addActionListener(this);

        //Label saldo
        balance = new JLabel();
        balance.setBounds(570, 5, 60, 25);
        balance.setText(String.format("%1$,.2f€", db.getCurrentUserAsClient().getSaldo()));
        balance.setFont(new Font("Arial", Font.BOLD, 12));
        balance.setForeground(new Color(198, 107, 61));

        //label do username
        username = new JLabel();
        username.setBounds(150, 5, 200, 25);
        username.setText("Bem vindo, " + db.getCurrentUser().getUsername());
        username.setFont(new Font("Arial", Font.BOLD, 12));
        username.setForeground(new Color(198, 107, 61));


        //Acrescentar botões à barra norte
        homeButtonPanel.add(backButton);
        homeButtonPanel.add(homeButton);
        homeButtonPanel.add(cartButton);
        homeButtonPanel.add(balance);
        homeButtonPanel.add(username);


        //Junção de componentes à frame
        add(panelContainer);
        add(homeButtonPanel, BorderLayout.NORTH);


        revalidate();
    }

    public void setCurrentPanel(JPanel currentPanel) {
        this.currentPanel = currentPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == cartButton) {
            gui.showShoppingCart();
        }
        if (e.getSource() == homeButton) {
            gui.showClientMainMenu();
        }
        else if(e.getSource() == backButton){
            if(
                    currentPanel == myPlaylists     ||
                    currentPanel == myMusic         ||
                    currentPanel == purchaseHistory ||
                    currentPanel == store
            ){
                gui.showClientMainMenu();
            }
            else if(currentPanel == menuInicial){
                gui.showClientMainMenu();
            }else {
                showCurrentPanel();
            }
        }
    }

    public void showPanelClient(String panelName) {
        cardLayout.show(panelContainer, panelName);
    }

    public void showCurrentPanel() {
        cardLayout.show(panelContainer, currentPanel.getName());
    }

    public void updateBalanceLabel() {
        Cliente cliente = (Cliente) gui.getDb().getCurrentUser();
        balance.setText(String.format("%1$,.2f€", cliente.getSaldo()));
    }
}
