package data;


import java.io.Serializable;

public class Musico extends User implements Serializable {
    private String pin;

    public Musico(String username, String password, String pin) {
        super(username, password);
        this.pin = pin;
    }

    public Musico() {
        super();
        this.pin = "";
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
