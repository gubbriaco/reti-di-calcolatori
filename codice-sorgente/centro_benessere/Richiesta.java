package centro_benessere;

import java.io.Serializable;
import java.util.Date;

public class Richiesta implements Serializable {
    private Date data;
    private int numPersone;

    public Richiesta(Date data, int numPersone){
        this.data=data;
        this.numPersone=numPersone;
    }
    public Date getData(){
        return data;
    }
    public int getNumPersone(){
        return numPersone;
    }
    public String toString(){
        return data.toString()+"-"+numPersone;
    }
}
