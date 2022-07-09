package traccia3multi_sem;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class RegistroGare {

    private static int COUNTER = 0;
    HashMap<Integer, RecordRegistro> gare = new HashMap<>();
    private Semaphore lock = new Semaphore(1);

    public Richiesta getRichiesta(int idGara) {
        return this.gare.get(idGara).richiesta;
    }
    public Offerta getOffertaVincente(int idGara) {
        return this.gare.get(idGara).miglioreOfferta;
    }
    public Socket getSocketEnte(int idGara) {
        return this.gare.get(idGara).enteSocket;
    }

    public int addGara(Socket enteSocket, Richiesta richiesta) throws InterruptedException {
        lock.acquire();
        int idGara = COUNTER++;
        this.gare.put(idGara, new RecordRegistro(enteSocket, richiesta));
        lock.release();
        return idGara;
    }

    public void chiudiGara(int idGara) throws InterruptedException {
        lock.acquire();
        if (this.gare.containsKey(idGara)) {
            this.gare.get(idGara).setStatus(false);
        }
        lock.release();
    }

    public void aggiungiOfferta(Offerta offerta) throws InterruptedException {
        lock.acquire();
        int idGara = offerta.getIdGara();
        if (this.gare.containsKey(idGara)) {
            RecordRegistro gara = this.gare.get(idGara);
            if (gara.isActive()) {
                Offerta oVincente = gara.miglioreOfferta;
                if ((oVincente == null && offerta.getImportoRichiesto() <= gara.richiesta.getImportoMassimo()) ||
                        oVincente.getImportoRichiesto() > offerta.getImportoRichiesto() ||
                        (oVincente.getImportoRichiesto() == offerta.getImportoRichiesto()
                                && offerta.getIdPartecipante() < oVincente.getIdPartecipante())
                ) {
                    gara.setMiglioreOfferta(offerta);
                    System.out.println("Aggiunta offerta per gara " + idGara);
                }
            } else {
                System.out.println("* Rifiutata offerta fuori tempo per gara " + idGara);
            }
        }
        lock.release();
    }

}
