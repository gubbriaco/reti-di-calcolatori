package traccia3multi_sem;

import java.net.Socket;

class RecordRegistro {

    Socket enteSocket;
    Offerta miglioreOfferta = null;
    Richiesta richiesta;
    boolean status;

    public RecordRegistro(Socket enteSocket, Richiesta richiesta) {
        this.enteSocket = enteSocket;
        this.richiesta = richiesta;
        this.status = true; // la gara è inizialmente aperta;
    }

    public void setMiglioreOfferta(Offerta miglioreOfferta) {
        this.miglioreOfferta = miglioreOfferta;
    }

    public boolean isActive() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}