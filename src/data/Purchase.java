package data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Purchase implements Serializable {

    @Serial
    private static final long serialVersionUID = 1325672347L;
    private Cliente cliente;
    private LocalDateTime dataCompra;
    private List<Music> songList;
    private double price;

    public Purchase(Cliente cliente, double price) {
        this.cliente = cliente;
        this.dataCompra = LocalDateTime.now();
        this.price = price;
        this.songList = new ArrayList<>();
    }

    public List<Music> getSongList() {
        return songList;
    }

}
