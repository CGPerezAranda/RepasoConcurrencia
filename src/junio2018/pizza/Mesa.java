package junio2018.pizza;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Mesa {

	private int nRaciones=8;
	private Lock l = new ReentrantLock();

	private Condition estudiante=l.newCondition();
	private Condition pizzeroReparte=l.newCondition();
	private Condition pizzeroCocina = l.newCondition();
	private Condition estudiantePaga = l.newCondition();


	boolean llamadaPizzero = false;
	boolean pizzaHecha = false;
	boolean pizzaEnDomicilio = false;
	boolean pagada = false;

	public Mesa() {

	}


	/**
	 * 
	 * @param id
	 * El estudiante id quiere una ración de pizza. 
	 * Si hay una ración la coge y sigue estudiante.
	 * Si no hay y es el primero que se da cuenta de que la mesa está vacía
	 * llama al pizzero y
	 * espera hasta que traiga una nueva pizza. Cuando el pizzero trae la pizza
	 * espera hasta que el estudiante que le ha llamado le pague.
	 * Si no hay pizza y no es el primer que se da cuenta de que la mesa está vacía
	 * espera hasta que haya un trozo para él.
	 * @throws InterruptedException 
	 * 
	 */
	public void nuevaRacion(int id) throws InterruptedException{
		l.lock();
		try {
			if(nRaciones > 0) {
				nRaciones--;
				System.out.println("El estudiante "+id+" ha cogido una racion");
			}
			if(nRaciones == 0 && !llamadaPizzero) {
				System.out.println("Sin pizza, llamando al pizzero");
				llamadaPizzero = true;
				pizzeroCocina.signal();		
				//cuando pizza llega estudiante la paga
				estudiantePaga.await();
				pagada = true;
				//pizzeroReparte.signal();
				estudiante.await();
				//el estudiante que ha llamado y pagado la pizza coge un trozo
				nRaciones--;
				System.out.println("El estudiante "+id+" ha cogido una racion");
			}else {
				estudiante.await();
			}

		}finally {
		l.unlock();
		}
	}


	/**
	 * El pizzero entrega la pizza y espera hasta que le paguen para irse
	 * @throws InterruptedException 
	 */
	public void nuevoCliente() throws InterruptedException{
		l.lock();
		try {
			while(!llamadaPizzero)
				pizzeroCocina.await();

			System.out.println ("Llamada recibida, preparando pizza");
			pizzaEnDomicilio = true;
			
			//Mejor poner la llamada al pizzero al final del método para que no entren nuevas hebras estudiante que se encuentran sin pizza
			//y llaman también al pizzero
			//*******************
			//llamadaPizzero = false;
			//*******************
			pizzeroReparte.signal();
			System.out.println("El pizzero lleva una pizza");
			estudiantePaga.signal();

		}finally {
			l.unlock();
		}
	}



	/**
	 * El pizzero espera hasta que algún cliente le llama para hacer una pizza y
	 * llevársela a domicilio
	 * @throws InterruptedException 
	 */
	public void nuevaPizza() throws InterruptedException{

		l.lock();
		try {
			while (!pagada || !pizzaEnDomicilio) 
				pizzeroReparte.await();
			
			System.out.println("pizza pagada, entregando pizza");
			nRaciones = 8;
			pizzaEnDomicilio = false;
			pagada = false;
			estudiante.signalAll();	
			llamadaPizzero = false;


		}finally {
			l.unlock();
		}

	}


}
