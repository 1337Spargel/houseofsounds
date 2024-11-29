package Factory;

public class PLZ {
    private int PLZ_ID;
    private int PLZ;
    private String ORT;

    public int getPLZ_ID() {
        return PLZ_ID;
    }

    public void setPLZ_ID(int PLZ_ID) {
        this.PLZ_ID = PLZ_ID;
    }

    public int getPLZ() {
        return PLZ;
    }

    public void setPLZ(int PLZ) {
        this.PLZ = PLZ;
    }

    public String getORT() {
        return ORT;
    }

    public void setORT(String ORT) {
        this.ORT = ORT;
    }

    @Override
        public String toString() {
            return  "PLZ ID: " + PLZ_ID +
                    ", PLZ: " + PLZ +
                    ", Ort: " + ORT;
    }
}
