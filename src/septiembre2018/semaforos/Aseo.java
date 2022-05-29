package septiembre2018.semaforos;

import java.util.concurrent.Semaphore;

public class Aseo {
	
	Semaphore entraHombre,entraMujer,mutexHombre,mutexMujer,aseo;
	int colaHombres, colaMujeres, enAseo;

	
	public Aseo() {
		entraHombre = new Semaphore(1,true);
		entraMujer = new Semaphore(1,true);
		mutexHombre = new Semaphore(1,true);
		mutexMujer = new Semaphore(1,true);
		aseo = new Semaphore(1,true);
		colaHombres = 0;
		colaMujeres = 0;
		enAseo = 0;
	}
	
	/**
	 * El hombre id quiere entrar en el aseo. 
	 * Espera si no es posible, es decir, si hay alguna mujer en ese
	 * momento en el aseo
	 */
	public void llegaHombre(int id) throws InterruptedException{
		entraHombre.acquire();
		mutexHombre.acquire();
		colaHombres++;
		if(colaHombres == 1) {
			aseo.acquire();			
		}
		//SC
		enAseo++;
		System.out.println("ENTRA hombre " + id + ". Hay "+ enAseo+ " hombres en el aseo");
		//post
		entraHombre.release();
		mutexHombre.release();
	}
	/**
	 * La mujer id quiere entrar en el aseo. 
	 * Espera si no es posible, es decir, si hay algun hombre en ese
	 * momento en el aseo
	 */
	public void llegaMujer(int id) throws InterruptedException{
		entraMujer.acquire();
		mutexMujer.acquire();
		colaMujeres++;
		if(colaMujeres == 1) {
			aseo.acquire();			
		}

		//SC
		enAseo++;
		System.out.println("ENTRA mujer " + id + ". Hay "+ enAseo+ " mujeres en el aseo");
		//post
		entraMujer.release();
		mutexMujer.release();
		
	}
	/**
	 * El hombre id, que estaba en el aseo, sale
	 */
	public void saleHombre(int id)throws InterruptedException{
		mutexHombre.acquire();
		colaHombres--;

		
		//SC
		enAseo--;
		System.out.println("SALE hombre " + id + ". Hay "+ enAseo+ " hombres en el aseo");
		//post
		
		if(colaHombres == 0) {
			aseo.release();
			entraHombre.release();
		}
		mutexHombre.release();
		
	}
	
	public void saleMujer(int id)throws InterruptedException{
		mutexMujer.acquire();
		colaMujeres--;
		//SC
		enAseo--;
		System.out.println("SALE mujer " + id + ". Hay "+ enAseo+ " mujeres en el aseo");
		//post
		if(colaMujeres == 0) {
			aseo.release();
			entraHombre.release();
		}
		mutexMujer.release();
		
		
	}
}
