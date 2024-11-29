package Factory;

public class Produkte {
    private int Produkte_ID;
    private double Preis;
    private int Rabatt_ID;
    /*
    private int Dauer;
    private double Hoehe_Rabatt;
    private String Rabattcode;

     */

    // Getter und Setter
    public int getProdukte_ID() { return Produkte_ID; }
    public void setProdukte_ID(int produkte_ID) { this.Produkte_ID = produkte_ID; }

    public double getPreis() { return Preis; }
    public void setPreis(double preis) { this.Preis = preis; }

    public int getRabatt_ID() { return Rabatt_ID; }
    public void setRabatt_ID(int Rabatt_ID) { this.Rabatt_ID = Rabatt_ID; }
/*
    public double getHoehe_Rabatt() { return Hoehe_Rabatt; }
    public void setHoehe_Rabatt(double hoehe_Rabatt) { this.Hoehe_Rabatt = hoehe_Rabatt; }

    public String getRabattcode() { return Rabattcode; }
    public void setRabattcode(String rabattcode) { this.Rabattcode = rabattcode; }

 */
@Override
public String toString() {
    return  "Produkt ID: " + Produkte_ID +
            ", Preis: " + Preis +
            ", Rabatt_ID: " + Rabatt_ID;
    }
}