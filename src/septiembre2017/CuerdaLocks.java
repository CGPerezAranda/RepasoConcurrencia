package Septiembre2017Lock;

import java.util.concurrent.locks.*;

public class Cuerda {
	
	private int numBabuinosNs, numBabuinosSn;//numero de babuinos que quiere cruzar;
	private Lock l = new ReentrantLock();
	private Condition esperaNS = l.newCondition();
	private Condition esperaSN = l.newCondition();
	
	public  void entraDireccionNS(int id) throws InterruptedException{
		l.lock();
		try {
			while(numBabuinosSn != 0) esperaNS.await();
			while(numBabuinosNs == 3) esperaNS.await();
			numBabuinosNs++;
			System.out.println("Estan cruzando los del norte sur");
		}finally {
			l.unlock();
		}
	}
	
	public  void entraDireccionSN(int id) throws InterruptedException{
		l.lock();
		try {
			while(numBabuinosNs != 0) esperaSN.await();
			while(numBabuinosSn == 3) esperaSN.await();
			numBabuinosSn++;
			System.out.println("Estan cruzando los del norte sur");
		}finally {
			l.unlock();
		}
		
	}

	public  void saleDireccionNS(int id) throws InterruptedException{
		l.lock();
		try {
			numBabuinosNs --;
			System.out.println("Salen los del norte sur");
			if(numBabuinosNs == 0) {
				esperaSN.signalAll();
				
			}
		}finally {
			l.unlock();
		}
		
	}
	

	public  void saleDireccionSN(int id) throws InterruptedException{
		l.lock();
		try {
			numBabuinosSn --;
			System.out.println("Salen los del sur norte");
			if(numBabuinosSn == 0) {
				esperaNS.signalAll();
			}
		}finally {
			l.unlock();
		}
	}	
		
}
