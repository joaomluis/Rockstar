package data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Purchase implements Serializable {

    @Serial
    private static final long serialVersionUID = 1325672347L;

    private String purchaseId;
    private Cliente cliente;
    private LocalDateTime dataCompra;
    private List<Music> songList;
    private double price;


    public Purchase(Cliente cliente, double price) {
        this.cliente = cliente;
        this.dataCompra = LocalDateTime.now();
        this.price = price;
        this.songList = new ArrayList<>();
        this.purchaseId = generatePurchaseID();
    }



    public String generatePurchaseID() {
        String uuid = UUID.randomUUID().toString();
        return "RSPID-" + uuid.substring(0, Math.min(uuid.length(), 6));
    }


    public List<Music> getSongList() {
        return songList;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public double getPrice() {
        return price;
    }

}
