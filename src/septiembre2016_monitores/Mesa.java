package septiembre2016_monitores;

import java.util.concurrent.*;
public class Mesa {
	
	private int N,numeroJugadores,numCaras, totalJugadores, numJugadas;
	private int ganador = totalJugadores;
	private boolean[] jugadas ;
	

	
	public Mesa(int N){
		totalJugadores = N;
		numJugadas = 0;
		jugadas = new boolean[N];
		
	}
	
	public synchronized int nuevaJugada(int id,boolean cara) throws InterruptedException{
		
		numJugadas++;
		jugadas[id] = cara;
		if(cara) 
			numCaras++;
		System.out.println("J"+id+" pone "+cara);
		//esperar al resto
		if(numJugadas < totalJugadores) {
			wait();
			
		}else {
			ganador = hayGanador();
			numCaras = 0;
			numJugadas = 0;
			if(ganador == totalJugadores) {
				System.out.println("empate");				
			}else {
				System.out.println("Ganador J" + ganador);
			}
			notifyAll();
		}
		return ganador;	
	}

	private int hayGanador() {
		int g = totalJugadores;
		int i = 0;
		if(numCaras == 1 || numCaras == totalJugadores -1) {
			boolean buscar = numCaras == 1;
			while (i<totalJugadores && jugadas[i] != buscar) {
				i++;
			}
			g=i;
		}
		
		return g;
		
	}

}
