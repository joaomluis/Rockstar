package data;

import java.io.Serializable;
import java.util.ArrayList;

public class Cliente extends User implements Serializable {
    private static final long serialVersionUID = 1325672347L;
    private double saldo;

    public Cliente(String username, String password) {
        super(username, password);
        saldo = 0.00;
    }

    public Cliente() {
        super();
        this.saldo = 0;
    }

    public double getSaldo() {
        return saldo;
    }


    public boolean adicionaSaldo(double valor){
        if(valor >= 0){
            saldo += valor;
            return true;
        }
        return false;
    }

}
