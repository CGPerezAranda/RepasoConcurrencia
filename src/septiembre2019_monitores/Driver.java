package septiembre2019_monitores;
public class Driver {

	public static void main(String[] args) {
		Concurso c = new Concurso();
		Jugador b0 = new Jugador(0, c);
		Jugador b1 = new Jugador(1, c);
		b0.start();
		b1.start();
	}

}
