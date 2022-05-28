package septiembre2017;



import java.util.concurrent.Semaphore;

public class Cuerda_w {
	
	private int numBabuinosNs, numBabuinosSn;//numero de babuinos que quiere cruzar;
	
	private Semaphore mutex = new Semaphore(1,true);
	private Semaphore NS = new Semaphore (1,true);
	private Semaphore SN = new Semaphore (1,true);
	
	private Semaphore esperaNS = new Semaphore (0,true);
	private Semaphore esperaSN = new Semaphore (0,true);
	
	public  void entraDireccionNS(int id) throws InterruptedException{
		NS.acquire();
		mutex.acquire();
		
		if(numBabuinosSn != 0 ) {
			System.out.println("Estan cruzando los del sur norte");
			mutex.release();
			esperaNS.acquire();
			mutex.acquire();
		}
		
		if(numBabuinosNs < 3) {
			numBabuinosNs++;
			System.out.println("Cruza el babuinoNS " + id + ". Estan cruzando los del norte sur");
		}else if(numBabuinosNs == 3){
			NS.release();
			mutex.release();
			esperaNS.acquire();
			
			mutex.acquire();
			NS.acquire();
		}
		
		mutex.release();
		NS.release();
	}
	
	public  void entraDireccionSN(int id) throws InterruptedException{
		SN.acquire();
		mutex.acquire();
		
		if(numBabuinosNs != 0 ) {
			System.out.println("Estan cruzando los del norte sur");
			mutex.release();
			esperaNS.acquire();
			mutex.acquire();
		}
		
		if(numBabuinosSn < 3) {
			numBabuinosSn++;
			System.out.println("Estan cruzando los del sur norte");
		}else if(numBabuinosSn == 3){
			SN.release();
			mutex.release();
			esperaSN.acquire();
			
			mutex.acquire();
			SN.acquire();
		}
		
		mutex.release();
		SN.release();
	}

	public  void saleDireccionNS(int id) throws InterruptedException{
		NS.acquire();
		mutex.acquire();
		
		numBabuinosNs--;
		System.out.println("Salen los babuinos del norte sur");
		
		if(numBabuinosNs == 0) {
			esperaNS.release();
			esperaSN.release();
			}else if(numBabuinosNs > 0) esperaNS.release();
			else if(numBabuinosSn == 0)esperaSN.release();
		
		mutex.release();
		NS.release();
	}
	

	public  void saleDireccionSN(int id) throws InterruptedException{
		SN.acquire();
		mutex.acquire();
		
		numBabuinosSn--;
		System.out.println("Salen los babuinos del sur norte");
		
		if(numBabuinosSn == 0) {
			esperaNS.release();
			esperaSN.release();
			}else if(numBabuinosNs > 0) esperaNS.release();
			else if(numBabuinosSn == 0)esperaSN.release();
		
		mutex.release();
		SN.release();
	}	
		
}
