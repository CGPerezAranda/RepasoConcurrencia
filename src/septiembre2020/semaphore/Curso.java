package septiembre2020.semaphore;

import java.util.concurrent.Semaphore;

public class Curso {
	
	Semaphore entraIniciacion, saleIniciacion, entraAvanzado, esperaGrupoAvanzado, saleAvanzado, mutexInic, mutexAvanzado;
	private int numAlIniciacion; //num de alumnos realizando el curso de iniciación
	private int nEsperaAvanzado; //num de alumnos en espera para hacer el grupo avanzado
	private boolean grupoAvanzado; //indica si hay un grupo realizando el curso avanzado

	//Numero maximo de alumnos cursando simultaneamente la parte de iniciacion
	private final int MAX_ALUMNOS_INI = 10;
	
	//Numero de alumnos por grupo en la parte avanzada
	private final int ALUMNOS_AV = 3;
	
	public Curso () {
		entraIniciacion = new Semaphore (1); //indica si un alumno puede entrar al curso de iniciación
		saleIniciacion = new Semaphore (0); //indica si un alumno puede salir del curso de iniciación
		entraAvanzado = new Semaphore(1);	//indica si un alumno puede entrar en un grupo de curso avanzado 
		saleAvanzado = new Semaphore(0);	//indica si un alumno puede salir de un curso avanzado
		esperaGrupoAvanzado = new Semaphore(1); //indica si un alumno tiene que esperar a que se forme grupo avanzado
		mutexInic = new Semaphore(1);
		mutexAvanzado = new Semaphore(1);
		numAlIniciacion = 0;
		nEsperaAvanzado = 0;
		grupoAvanzado = false;
	}
	
	//El alumno tendra que esperar si ya hay 10 alumnos cursando la parte de iniciacion
	public void esperaPlazaIniciacion(int id) throws InterruptedException{
		entraIniciacion.acquire();
		mutexInic.acquire();
		//SC
		if(numAlIniciacion < MAX_ALUMNOS_INI) {
			numAlIniciacion++;	
			saleIniciacion.release();
			entraIniciacion.release();
		}
		mutexInic.release();
		
		
		//Espera si ya hay 10 alumnos cursando esta parte

		//Mensaje a mostrar cuando el alumno pueda conectarse y cursar la parte de iniciacion
		System.out.println("PARTE INICIACION: Alumno " + id + " cursa parte iniciacion");
	}

	//El alumno informa que ya ha terminado de cursar la parte de iniciacion
	public void finIniciacion(int id) throws InterruptedException{
		saleIniciacion.acquire();
		mutexInic.acquire();
		
		//SC
		numAlIniciacion--;
		//Mensaje a mostrar para indicar que el alumno ha terminado la parte de principiantes
		System.out.println("PARTE INICIACION: Alumno " + id + " termina parte iniciacion");
		mutexInic.release();
		//Libera la conexion para que otro alumno pueda usarla
	}
	
	/* El alumno tendra que esperar:
	 *   - si ya hay un grupo realizando la parte avanzada
	 *   - si todavia no estan los tres miembros del grupo conectados
	 */
	public void esperaPlazaAvanzado(int id) throws InterruptedException{
		entraAvanzado.acquire();
		mutexAvanzado.acquire();
		
		//No hay un grupo avanzado haciendo el curso por lo que, si hay 3 alumnos esperando pueden hacer el curso y salir después.
		if(!grupoAvanzado) {
	
			nEsperaAvanzado++;
			//Espera a que haya tres alumnos conectados
			//Mensaje a mostrar si el alumno tiene que esperar al resto de miembros en el grupo
			if(nEsperaAvanzado < ALUMNOS_AV) {
				System.out.println("PARTE AVANZADA: Alumno " + id + " espera a que haya " + ALUMNOS_AV + " alumnos");	
				entraAvanzado.release();
			}else {
				//Mensaje a mostrar cuando el alumno pueda empezar a cursar la parte avanzada
				System.out.println("PARTE AVANZADA: Hay " + ALUMNOS_AV + " alumnos. Alumno " + id + " empieza el proyecto");
				grupoAvanzado = true;
				saleAvanzado.release();
			}				
			mutexAvanzado.release(); 
		}else {
			//Espera a que no haya otro grupo realizando esta parte
			mutexAvanzado.release();
			entraAvanzado.release();
		}
	
	}
	
	/* El alumno:
	 *   - informa que ya ha terminado de cursar la parte avanzada 
	 *   - espera hasta que los tres miembros del grupo hayan terminado su parte 
	 */ 
	public void finAvanzado(int id) throws InterruptedException{
		saleAvanzado.acquire();
		mutexAvanzado.acquire();
		//Espera a que los 3 alumnos terminen su parte avanzada
		nEsperaAvanzado--;
		if(nEsperaAvanzado==0) {
			//Mensaje a mostrar cuando los tres alumnos del grupo han terminado su parte
			System.out.println("PARTE AVANZADA: LOS " + ALUMNOS_AV + " ALUMNOS HAN TERMINADO EL CURSO");
			entraAvanzado.release();
		}else {
			//Mensaje a mostrar si el alumno tiene que esperar a que los otros miembros del grupo terminen
			System.out.println("PARTE AVANZADA: Alumno " +  id + " termina su parte del proyecto. Espera al resto");
			saleAvanzado.release();
		}
		mutexAvanzado.release();
		grupoAvanzado = false;

		
	}
}
