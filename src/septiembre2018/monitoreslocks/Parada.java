package septiembre2018.monitoreslocks;

import java.util.UnknownFormatFlagsException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Parada {
	
	private Lock l = new ReentrantLock();
	private int personasEnParada;
	private Condition puedeSubir, cllegaBus;
	private boolean haybus, llegaBus;
	
	
	public Parada(){
		personasEnParada = 0;
		puedeSubir = l.newCondition();
		cllegaBus = l.newCondition();
		
	}
	/**
	 * El pasajero id llama a este metodo cuando llega a la parada.
	 * Siempre tiene que esperar el siguiente autobus en uno de los
	 * dos grupos de personas que hay en la parada
	 * El metodo devuelve el grupo en el que esta esperando el pasajero
	 * 
	 */
	public int esperoBus(int id) throws InterruptedException{
		int grupo = 0; //grupo 0 se sube en el autobús en cuanto abra la puerta, el 1 espera al siguiente
		l.lock();
		try {
			System.out.println ("El pasajero "+id+ " llega a la parada");
			while (haybus) 
				puedeSubir.await();
			
			personasEnParada++;
			System.out.println ("Hay "+ personasEnParada+" personas esperando el siguiente Bus");
			cllegaBus.await();

				
		}
			
		finally {
			l.unlock();
		}
		
		return personasEnParada; //comentar esta línea
	}
	/**
	 * Una vez que ha llegado el autobús, el pasajero id que estaba
	 * esperando en el grupo i se sube al autobus
	 *
	 */
	public void subeAutobus(int id,int i){
		l.lock();
		try {
			personasEnParada--;
			System.out.println("El pasajero "+id+ " sube al autobus.");
			
			if(personasEnParada == 0) {
				haybus = false;
				puedeSubir.signalAll();
				System.out.println("El bus se va");
				llegaBus = false;				
			}
		}
		finally {
			l.unlock();
		}
		
	}
	/**
	 * El autobus llama a este metodo cuando llega a la parada
	 * Espera a que se suban todos los viajeros que han llegado antes
	 * que el, y se va
	 * 
	 */
	public void llegoParada() throws InterruptedException{
		l.lock();
		try {
			System.out.println ("Bus llega a la parada");
			llegaBus = true;
			haybus = true;
			cllegaBus.signalAll();
		}
		finally {
			l.unlock();
		}
	}
}
