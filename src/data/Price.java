package data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * Representa o preço de um item em um determinado momento.
 * É serializável para permitir serem gravados em arquivos.
 */
public class Price implements Serializable {
    @Serial
    private static final long serialVersionUID = 1325672347L;
    private double preco;
    private LocalDateTime data;

    public Price(double preco){
        this.preco = preco;
        this.data = LocalDateTime.now();
    }


    public double getPreco(){
        return preco;
    }

    public LocalDateTime getData() {
        return data;
    }
}