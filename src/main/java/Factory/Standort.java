package Factory;

public class Standort {
    private int Standort_ID;
    private String Standortname;
    private String Straße;
    private String Hausnummer;
    private int PLZ_ID;


    // Getter und Setter
    public int getStandort_ID() {
        return Standort_ID;
    }

    public void setStandort_ID(int standort_ID) {
        this.Standort_ID = standort_ID;
    }

    public String getStandortname() {
        return Standortname;
    }

    public void setStandortname(String standortname) {
        this.Standortname = standortname;
    }

    public String getStraße() {
        return Straße;
    }

    public void setStraße(String straße) {
        this.Straße = straße;
    }

    public String getHausnummer() {
        return Hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.Hausnummer = hausnummer;
    }

    public int getPLZ_ID() {
        return PLZ_ID;
    }

    public void setPLZ_ID(int PLZ_ID) {
        this.PLZ_ID = PLZ_ID;
    }

}
