package septiembre2019_monitores;

public class Concurso {

	private int[] caras = new int[2];
	private int[] tiradas = new int[2];
	private int[] wins = new int[2];
	
	public Concurso() {
	}

	
	public synchronized void tirarMoneda(int id,boolean cara) throws InterruptedException {
		while (tiradas[id] == 10) wait();
		tiradas[id]++;
		if(cara) {
			caras[id]++;
		}
		if (tiradas[id] < 10) 
			notifyAll();
			
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
			}
			notifyAll();
		}
	}	
	
	public synchronized boolean concursoTerminado() throws InterruptedException {
	
		boolean terminado = false;
		for(int i =0; i<2;i++) {
			if(wins[i]==3) {
				System.out.println("\nGanó el jugador "+ i+"\n\n*****************************\n\n");
				wins[i] = 0;
				wins[1-i] =0;
				terminado = true;
			}
		}
		notifyAll();
		return terminado; 
	}
}