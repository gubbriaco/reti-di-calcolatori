package traccia3multi_sem;

import java.io.Serializable;

public class Offerta implements Serializable {
    private int idPartecipante;
    private int idGara;
    private int importoRichiesto;

    public Offerta(int idPartecipante, int idGara, int importoRichiesto) {
        this.idPartecipante = idPartecipante;
        this.importoRichiesto = importoRichiesto;
        this.idGara = idGara;
    }

    public int getIdPartecipante() {
        return idPartecipante;
    }
    public int getIdGara() {
        return idGara;
    }
    public int getImportoRichiesto() { return importoRichiesto; }

    @Override
    public String toString() {
        return "Offerta [idPartecipante=" + idPartecipante + ", idGara=" + idGara +
                ", importoRichiesto=" + importoRichiesto + ']';
    }
}

