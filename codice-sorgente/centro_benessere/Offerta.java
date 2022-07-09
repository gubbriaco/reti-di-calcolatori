package centro_benessere;

import java.io.Serializable;

public class Offerta implements Serializable {
    private String hostNameCentroB;
    private int prezzo;

    public Offerta(String hostNameCentroB,int prezzo){
        this.hostNameCentroB=hostNameCentroB;
        this.prezzo=prezzo;
    }

    public int getPrezzo() {
        return prezzo;
    }
    public String getHostNameCentroB(){
        return hostNameCentroB;
    }
    public String toString(){
        return hostNameCentroB+"-"+prezzo;
    }
}

