package portook;

import java.util.List;

public class RispostaHandler extends Thread {
	
	private Nave nave;
	private RichiestaHandler rh;
	
	private List<Banchina> banchine;
	
	public RispostaHandler( Nave nave, RichiestaHandler rh) {
		this.nave = nave;
		this.rh = rh;
		
		banchine = rh.getBanchine();
	}
	
	
	private Risposta risposta;
	
	@Override public void run() {
		
		boolean piu40 = false;
		
		if(nave.getLunghezza() > 40)
			piu40 = true;
		
		Banchina banchina;
		boolean stored = false;
		for(int i=0;i<banchine.size();++i) {
			banchina = banchine.get(i);

			if( piu40 ) {
				
				if( banchina.getPiu40() && !banchina.getIsBusy() ) {
					banchina.aggiungiNave(nave);
					risposta = new Risposta(nave.getId(), nave.getLunghezza(), nave.getNr_container_da_scaricare(), i);
					rh.storeRisposta( risposta );
					stored = true;
					break;
				}
				
			}else {

				if( !banchina.getPiu40() && !banchina.getIsBusy() ) {
					banchina.aggiungiNave(nave);
					risposta = new Risposta(nave.getId(), nave.getLunghezza(), nave.getNr_container_da_scaricare(), i);
					rh.storeRisposta( risposta );
					stored = true;
					break;
				}
				
			}
	
		}
		
		if( !stored ) {
			
			rh.aggiungiNaveInCoda(nave);
			
		}
		
		
	}
	
}
