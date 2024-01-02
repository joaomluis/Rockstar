package data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Price implements Serializable {
    @Serial
    private static final long serialVersionUID = 1325672347L;
    private double preco;
    private LocalDateTime data;

    public Price(double preco){
        this.preco = preco;
        this.data = LocalDateTime.now();
    }

    public Price() {
    }

    public double getPreco(){
        return preco;
    }
}