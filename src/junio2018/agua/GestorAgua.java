package junio2018.agua;


import java.util.concurrent.*;

public class GestorAgua {
	int nH;
	boolean hayO;
	Semaphore entraH;
	Semaphore entraO;
	Semaphore moleculaLista;
	Semaphore mutex;

	public GestorAgua() {
		hayO = false;
		nH = 0;
		entraH = new Semaphore(1,true);
		entraO = new Semaphore(1,true);
		moleculaLista = new Semaphore(0,true);
		mutex = new Semaphore(1,true);
	}
	
	
	public void hListo(int id) throws InterruptedException{ 
		entraH.acquire();
		mutex.acquire();
		nH++;
		System.out.println("Entra átomo "+id+" de H");
		if(nH == 2 && !hayO) {
			mutex.release();
			moleculaLista.acquire();
		}
	
		if(nH < 2) {
			entraH.release();
			mutex.release();	
			moleculaLista.acquire();
		}else {
			System.out.println("Molécula Lista");
			nH = 0;
			hayO = false;
			
			entraH.release();
			entraO.release();
			moleculaLista.release();
			moleculaLista.release();
			mutex.release();
		}
		
	}
			
	
	public void oListo(int id) throws InterruptedException{ 
		entraO.acquire();
		mutex.acquire();
		hayO = true;
		System.out.println("Entra átomo "+id+" de O");
		if(nH < 2) {
			mutex.release();
			moleculaLista.acquire();
		}else {
			System.out.println("Molécula Lista");
			nH = 0;
			hayO = false;
			
			entraH.release();
			entraO.release();
			moleculaLista.release();
			moleculaLista.release();
			mutex.release();
		}
	}
}