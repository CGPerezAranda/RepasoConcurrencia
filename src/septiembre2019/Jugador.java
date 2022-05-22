package septiembre2019;

import java.util.Random;

public class Jugador extends Thread {
	private static Random rnd= new Random();
	private Concurso c;
	private int id;
	public Jugador(int i, Concurso c) {
		this.id = i;
		this.c = c;
	}

	@Override
	public void run() {
		while(true) {
			try {
				do {

					for (int i = 0; i<10; i++) {
						sleep(10+rnd.nextInt(90));
						c.tirarMoneda(id,rnd.nextBoolean());
					};
				} while (!c.concursoTerminado() );
			} catch (InterruptedException e) { e.printStackTrace(); }
		}
	}
}
