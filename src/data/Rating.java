package data;

import java.io.Serializable;
import java.time.LocalDate;
/**
 * Representa uma avaliação realizada por um cliente.
 * Armazena a nota da avaliação, a data em que foi feita e o cliente que a fez.
 */
public class Rating implements Serializable {
    private static final long serialVersionUID = 1325672347L;

    private double avaliacao;
    private LocalDate data;
    private Cliente cliente;
    public Rating(double avaliacao, Cliente cliente) {
        this.avaliacao = avaliacao;
        this.cliente = cliente;
        this.data = LocalDate.now();
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public LocalDate getData() {
        return data;
    }

    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "avaliacao=" + avaliacao +
                ", data=" + data +
                ", cliente=" + cliente +
                '}';
    }
}