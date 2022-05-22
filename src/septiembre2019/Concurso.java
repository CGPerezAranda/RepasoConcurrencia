package septiembre2019;

import java.util.concurrent.Semaphore;

public class Concurso {
	Semaphore mutex = new Semaphore(1);
	Semaphore[] haTerminado = new Semaphore[2];
	private int[] caras = new int[2];
	private int[] tiradas = new int[2];
	private int[] wins = new int[2];
	
	public Concurso() {
		for (int i=0; i<2;i++) {
			haTerminado[i] = new Semaphore(1);
		}
	}

	
	public void tirarMoneda(int id,boolean cara) throws InterruptedException {
		haTerminado[id].acquire();
		mutex.acquire();
		tiradas[id]++;
		if(cara) {
			caras[id]++;
		}
		if (tiradas[id] < 10) 
			haTerminado[id].release();
			
		if(tiradas[id] == 10 && tiradas[1-id] == 10) {
			if (caras[id] > caras[1-id]) {
				wins[id]++;
				System.out.println ("Ha ganado el jugador "+ id + " con "+ caras[id]+ " caras.");
			}else if(caras[id] < caras [1-id]){
				wins[1-id]++;
				System.out.println ("Ha ganado el jugador "+ (1-id) + " con "+ caras[1-id]+ " caras.");
			}else {
				System.out.println ("Los jugadores han empatado a "+caras[id]+ " caras.");
			}
			for (int i=0; i<2; i++) {
				caras[i] = 0;
				tiradas[i] = 0;
				haTerminado[i].release();
			}
	
		}
		mutex.release();
	}	
	
	public boolean concursoTerminado() throws InterruptedException {
		boolean terminado = false;
		mutex.acquire();
		for(int i =0; i<2;i++) {
			if(wins[i]==3) {
				System.out.println("\nGanó el jugador "+ i+"\n\n*****************************\n\n");
				wins[i] = 0;
				wins[1-i] =0;
				terminado = true;
			}
		}
		mutex.release();
		return terminado; //borrar
	}
}