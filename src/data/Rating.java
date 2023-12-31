package data;

import java.io.Serializable;
import java.time.LocalDate;

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

    public Rating() {
    }

    //** Getters ***/
    /**
     * Geter para avaliação
     *
     * @return avaliacao
     */
    public double getAvaliacao() {
        return avaliacao;
    }
    /**
     * Geter para data da avaliação
     *
     * @return data
     */
    public LocalDate getData() {
        return data;
    }
    /**
     * Geter para cliente que a avaliou
     *
     * @return cliente
     */
    public Cliente getCliente() {
        return cliente;
    }
}