package data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Price implements Serializable {
    @Serial
    private static final long serialVersionUID = 1325672347L;
    private double preco;
    private LocalDate data;

    public Price(double preco){
        this.preco = preco;
        this.data = LocalDate.now();
    }

    public Price() {
    }

    public double getPreco(){
        return preco;
    }
}