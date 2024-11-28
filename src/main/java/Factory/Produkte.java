package Factory;

public class Produkte {

    private int Produkte_ID;
    private double Preis;
    private int Rabatt_ID;

    // Getter und Setter
    public int getProdukte_ID() {
        return Produkte_ID;
    }

    public void setProdukte_ID(int produkte_ID) {
        this.Produkte_ID = produkte_ID;
    }

    public double getPreis() {
        return Preis;
    }

    public void setPreis(double preis) {
        this.Preis = preis;
    }

    public int getRabatt_ID() {
        return Rabatt_ID;
    }

    public void setRabatt_ID(int Rabatt_ID) {
        this.Rabatt_ID = Rabatt_ID;
    }
}