package septiembre2017;



import java.util.concurrent.Semaphore;

public class Cuerda {

		
		private int enCuerda, enColaNorte, enColaSur;//numero de babuinos que quiere cruzar;
		
		private Semaphore mutex;
		private Semaphore mutexNorte;
		private Semaphore mutexSur;
		private Semaphore cuerda;
		private Semaphore entraNorte;
		private Semaphore entraSur;
		
		public Cuerda() {
			enCuerda =0;
			enColaNorte = 0;
			enColaSur = 0;
			mutexNorte = new Semaphore(1, true);
			mutexSur = new Semaphore(1, true);
			cuerda = new Semaphore(1, true);
			entraSur = new Semaphore(1, true);
			entraNorte = new Semaphore(1, true);
			
		}
		
	
	public  void entraDireccionNS(int id) throws InterruptedException{
		entraNorte.acquire();
		mutexNorte.acquire();
		enColaNorte++;
		if(enColaNorte == 1) {
			cuerda.acquire(); //solo lo puede hacer el primero de cada dirección
			
		}
		//SC
		enCuerda++;
		System.out.println("El mono norte "+ id + " entra en la  cuerda.");
		//post
		if(enCuerda < 3)
			entraNorte.release();
		mutexNorte.release();
	}
		
	
	public  void entraDireccionSN(int id) throws InterruptedException{
		entraSur.acquire();
		mutexSur.acquire();
		enColaSur++;
		if(enColaSur == 1) {
			cuerda.acquire(); //solo lo puede hacer el primero de cada dirección
			
		}
		//SC
		enCuerda++;
		System.out.println("El mono sur "+ id + " entra en la  cuerda.");
		//post
		if(enCuerda < 3)
			entraSur.release();
		mutexSur.release();
	}

	public  void saleDireccionNS(int id) throws InterruptedException{
		mutexNorte.acquire();
		enCuerda--;
		enColaNorte--;
		System.out.println("Mono norte "+id+" sale de la cuerda");
		System.out.println("Número de monos en la cuerda "+enCuerda);
		//injusta
//		if(enCuerda == 2) 
//			entraNorte.release();
//		if(enCuerda == 0) 
//			entraSur.release();
//
//		cuerda.release();
		//justa
		if(enCuerda == 0) {
			entraSur.release();
			cuerda.release();
		}
		//
		mutexNorte.release();
		
	}
	

	public  void saleDireccionSN(int id) throws InterruptedException{
		mutexSur.acquire();
		enCuerda--;
		enColaSur--;
		System.out.println("Mono sur "+id+" sale de la cuerda");
		System.out.println("Número de monos en la cuerda "+enCuerda);
		//injusta
//		if(enCuerda == 2) 
//			entraSur.release();
//		if(enCuerda == 0) 
//			entraNorte.release();
//
//		cuerda.release();
		//justa
		if(enCuerda == 0) {
			entraNorte.release();
			cuerda.release();
		}
		//
		mutexSur.release();

	}	
		
}
