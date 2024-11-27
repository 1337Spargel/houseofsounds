package Factory;

public class Rabatt {
    private int Rabatt_ID;
    private String Rabattcode;
    private double Hoehe_Rabatt;
    private Integer Dauer;

    public int getRabatt_ID() {
        return Rabatt_ID;
    }

    public void setRabatt_ID(int Rabatt_ID) {
        this.Rabatt_ID = Rabatt_ID;
    }

    public String getRabattcode() {
        return Rabattcode;
    }

    public void setRabattcode(String Rabattcode) {
        this.Rabattcode = Rabattcode;
    }

    public Double getHoehe_Rabatt() {
        return Hoehe_Rabatt;
    }

    public void setHoehe_Rabatt(double Hoehe_Rabatt) {
        this.Hoehe_Rabatt = Hoehe_Rabatt;
    }

    public Integer getDauer() {
        return Dauer;
    }

    public void setDauer(Integer Dauer) {
        this.Dauer = Dauer;
    }

}

