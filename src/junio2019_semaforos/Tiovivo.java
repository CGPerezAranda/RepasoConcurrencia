package junio2019_semaforos;

import java.util.concurrent.Semaphore;

public class Tiovivo extends Thread {
	private Semaphore puedenSubir;
	private Semaphore puedenBajar;
	private Semaphore vuelta;
	private int capacidad;
	private int nPasajeros;
	
	public Tiovivo(int capacidad) {
		this.capacidad = capacidad;
		nPasajeros = 0;
		puedenSubir = new Semaphore(1);
		puedenBajar = new Semaphore(0);
		vuelta = new Semaphore(0);	
	}

	public void subir(int id) throws InterruptedException 
	{	
		puedenSubir.acquire();
		nPasajeros++;
		System.out.println("Sube el pasajero "+id+"\n"+(capacidad-nPasajeros)+" plazas libres");
		if(nPasajeros==capacidad){
			System.out.println("Comienza viaje");
			vuelta.release();
		}
	//TODO
	}
	
	public void bajar(int id) 
	{
		//TODO
	}
	
	public void esperaLleno () 
	{
		//TODO			
	}
	
	public void finViaje () 
	{
		//TODO
	}
}
