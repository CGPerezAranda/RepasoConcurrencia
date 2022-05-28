package septiembre2016_semaforos;

import java.util.concurrent.*;
public class Mesa {
	
	private int N,numeroJugadores,numCaras, totalJugadores, numJugadas;
	private int ganador = totalJugadores;
	private boolean[] jugadas ;
	
	private Semaphore mutex = new Semaphore(1,true);
	private Semaphore todosHanJugado = new Semaphore(0,true);
	private Semaphore ronda = new Semaphore(1,true);
	
	public Mesa(int N){
		totalJugadores = N;
		numJugadas = 0;
		jugadas = new boolean[N];
		ronda = new Semaphore (1,true);
		todosHanJugado = new Semaphore (0,true);
		mutex = new Semaphore (1,true);
	}
	
	public int nuevaJugada(int id,boolean cara) throws InterruptedException{
		ronda.acquire();
		mutex.acquire();
		numJugadas++;
		jugadas[id] = cara;
		if(cara) 
			numCaras++;
		System.out.println("J"+id+" pone "+cara);
		//esperar al resto
		if(numJugadas < totalJugadores) {
			mutex.release();
			ronda.release();
			todosHanJugado.acquire();
			mutex.acquire();
		}else {
			ganador = hayGanador();
			if(ganador == totalJugadores) {
				System.out.println("empate");
				
			}else {
				System.out.println("Ganador " + ganador);
			}
		}
		//mirar quien ha ganado
		numJugadas--;
		if(numJugadas > 0) {
			todosHanJugado.release();
		}else {
			ronda.release();
			numCaras = 0;
		}
		mutex.release();
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
