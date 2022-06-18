package septiembre2020.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Curso {

	//Numero maximo de alumnos cursando simultaneamente la parte de iniciacion
	private final int MAX_ALUMNOS_INI = 10;
	
	//Numero de alumnos por grupo en la parte avanzada
	private final int ALUMNOS_AV = 3;
	
	private boolean grupoAvanzado;
	private int nAlIniciacion, nEsperaAvanzado;
	private Lock l;
	private Condition esperaIniciacion, saleIniciacion, esperaAvanzado, saleAvanzado;
	
	public Curso() {
		grupoAvanzado = false;
		nAlIniciacion = 0;
		nEsperaAvanzado = 0;
		l = new ReentrantLock();
		esperaAvanzado = l.newCondition();
		saleAvanzado = l.newCondition();
		esperaIniciacion = l.newCondition();
		saleIniciacion = l.newCondition();
	}
	
	//El alumno tendra que esperar si ya hay 10 alumnos cursando la parte de iniciacion
	public void esperaPlazaIniciacion(int id) throws InterruptedException{
		l.lock();
		try {
			nAlIniciacion++;
			if(nAlIniciacion == MAX_ALUMNOS_INI) {
				esperaIniciacion.await();
				saleIniciacion.signalAll();
			}
				
			System.out.println("PARTE INICIACION: Alumno " + id + " cursa parte iniciacion");
		
		}finally {
			l.unlock();
		}
		//Espera si ya hay 10 alumnos cursando esta parte


	}

	//El alumno informa que ya ha terminado de cursar la parte de iniciacion
	public void finIniciacion(int id) throws InterruptedException{
		l.lock();
		try {
			nAlIniciacion--;
			if(nAlIniciacion==0) {
				esperaIniciacion.signalAll();	
				saleIniciacion.await();		
			}
			//Mensaje a mostrar para indicar que el alumno ha terminado la parte de principiantes
			System.out.println("PARTE INICIACION: Alumno " + id + " termina parte iniciacion");
			

			
		}finally {
			l.unlock();
		}


		//Libera la conexion para que otro alumno pueda usarla
	}
	
	/* El alumno tendra que esperar:
	 *   - si ya hay un grupo realizando la parte avanzada
	 *   - si todavia no estan los tres miembros del grupo conectados
	 */
	public void esperaPlazaAvanzado(int id) throws InterruptedException{
		l.lock();
		try {
			while(grupoAvanzado) //Espera a que no haya otro grupo realizando esta parte
				esperaAvanzado.await();
			nEsperaAvanzado++;
			if(nEsperaAvanzado<ALUMNOS_AV) { //Espera a que haya tres alumnos conectados
				//Mensaje a mostrar si el alumno tiene que esperar al resto de miembros en el grupo
				System.out.println("PARTE AVANZADA: Alumno " + id + " espera a que haya " + ALUMNOS_AV + " alumnos");
				esperaAvanzado.await();
			}else {
				grupoAvanzado=true;
				esperaAvanzado.signalAll();
			}
			//Mensaje a mostrar cuando el alumno pueda empezar a cursar la parte avanzada
			System.out.println("PARTE AVANZADA: Hay " + ALUMNOS_AV + " alumnos. Alumno " + id + " empieza el proyecto");
			
			saleAvanzado.signalAll();
			
		}finally {
			l.unlock();
		}
		

		



		
	}
	
	/* El alumno:
	 *   - informa que ya ha terminado de cursar la parte avanzada 
	 *   - espera hasta que los tres miembros del grupo hayan terminado su parte 
	 */ 
	public void finAvanzado(int id) throws InterruptedException{
		l.lock();
		try {
			nEsperaAvanzado--;
			if(nEsperaAvanzado > 0) {//Espera a que los 3 alumnos terminen su parte avanzada
				//Mensaje a mostrar si el alumno tiene que esperar a que los otros miembros del grupo terminen
				System.out.println("PARTE AVANZADA: Alumno " +  id + " termina su parte del proyecto. Espera al resto");
				
				saleAvanzado.await();
			}else {
				saleAvanzado.signalAll();
				//Mensaje a mostrar cuando los tres alumnos del grupo han terminado su parte
				System.out.println("PARTE AVANZADA: LOS " + ALUMNOS_AV + " ALUMNOS HAN TERMINADO EL CURSO");
				grupoAvanzado = false;
				esperaAvanzado.signalAll();

			}
			
		}finally {
			l.unlock();
		}
		



		
	}
}

