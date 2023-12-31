package data;

import java.io.Serializable;
import java.time.LocalDate;

public class Purchase implements Serializable {

    private static final long serialVersionUID = 1325672347L;
    private Cliente cliente;
    private LocalDate dataCompra;
    private Music song;
    private double price;

    public Purchase(Cliente cliente, Music song, double price) {
        this.cliente = cliente;
        this.song = song;
        this.dataCompra = LocalDate.now();
        this.price = price;
    }
}
