package Laberinto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class WilsonsLaberinto {
	Problema problema;
	Random aleatorio = new Random();
	ResolverLaberinto a;
	List<Estado> estadosVisitados = new LinkedList<>();
	List<Estado> celdasValor = new LinkedList<>();
	List<Celda> noVisitadas = new ArrayList<>();
	List<Celda> laberinto = new ArrayList<>();
	PriorityQueue<Nodo> frontera = new PriorityQueue<Nodo>();
	Stack<Celda> camino = new Stack<>();
	Celda cFinal = null;
	int anchura, altura;
	String estrategia;
	Estado e;
	Id id;

	public WilsonsLaberinto(int altura, int anchura, String estrategia) {
		this.anchura = anchura;
		this.altura = altura;
		this.estrategia = estrategia;

	}

	public void inicializarLaberinto() {
		Celda inicial = new Celda(aleatorio.nextInt(altura), aleatorio.nextInt(anchura));
		laberinto.add(inicial);
		System.out.println("\n LABERINTO\nCelda comienzo: fila --> " + inicial.x + " columna --> " + inicial.y);
		generarCeldasNoVisitadas(inicial);
	}

	public void generarCeldasNoVisitadas(Celda inicial) {

		for (int x = 0; x < altura; x++) {
			for (int y = 0; y < anchura; y++) {
				noVisitadas.add(new Celda(x, y));
			}
		}
		noVisitadas.remove(inicial);
	}

	public boolean estaCompleto() {
		return laberinto.size() == anchura * altura;
	}

	public void siguienteCelda() {
		if (camino.isEmpty()) {
			Random rand = new Random();
			Celda celdaComienzo = noVisitadas.remove(rand.nextInt(noVisitadas.size()));
			camino.add(celdaComienzo);
		} else {
			Celda ultimaCelda = camino.peek();

			int dir = -1;
			boolean valid = false;
			Celda siguienteCelda = null;
			do {
				dir = (int) (Math.random() * 4);

				if (dir == 0 && ultimaCelda.x != 0) {
					siguienteCelda = new Celda(ultimaCelda.x - 1, ultimaCelda.y);
					if (!camino.contains(siguienteCelda)) {
						valid = true;
						ultimaCelda.norte = true;
						siguienteCelda.sur = true;
						camino.push(siguienteCelda);

					} else {
						while (!camino.peek().equals(siguienteCelda)) {
							arreglarCaminos();
							camino.pop();
						}
						ultimaCelda = camino.peek();
					}

				} else if (dir == 2 && ultimaCelda.x != altura - 1) {
					siguienteCelda = new Celda(ultimaCelda.x + 1, ultimaCelda.y);
					if (!camino.contains(siguienteCelda)) {
						valid = true;
						ultimaCelda.sur = true;
						siguienteCelda.norte = true;
						camino.push(siguienteCelda);
					} else {
						while (!camino.peek().equals(siguienteCelda)) {
							arreglarCaminos();
							camino.pop();
						}
						ultimaCelda = camino.peek();
					}
				} else if (dir == 3 && ultimaCelda.y != 0) {
					siguienteCelda = new Celda(ultimaCelda.x, ultimaCelda.y - 1);
					if (!camino.contains(siguienteCelda)) {
						valid = true;
						ultimaCelda.oeste = true;
						siguienteCelda.este = true;
						camino.push(siguienteCelda);
					} else {
						while (!camino.peek().equals(siguienteCelda)) {
							arreglarCaminos();
							camino.pop();
						}
						ultimaCelda = camino.peek();
					}
				} else if (dir == 1 && ultimaCelda.y != anchura - 1) {
					siguienteCelda = new Celda(ultimaCelda.x, ultimaCelda.y + 1);
					if (!camino.contains(siguienteCelda)) {
						valid = true;
						ultimaCelda.este = true;
						siguienteCelda.oeste = true;
						camino.push(siguienteCelda);
					} else {
						while (!camino.peek().equals(siguienteCelda)) {
							arreglarCaminos();
							camino.pop();
						}
						ultimaCelda = camino.peek();
					}
				}

			} while (!valid);

			if (laberinto.contains(siguienteCelda)) {
				final Celda finalNextCelda = siguienteCelda;
				laberinto.stream().filter((c) -> c.equals(finalNextCelda)).forEach((c) -> {
					c.este = c.este || finalNextCelda.este;
					c.oeste = c.oeste || finalNextCelda.oeste;
					c.norte = c.norte || finalNextCelda.norte;
					c.sur = c.sur || finalNextCelda.sur;
				});
				camino.pop();
				laberinto.addAll(camino);
				noVisitadas.removeAll(camino);
				camino.clear();
			}

			if (estaCompleto()) {
				Celda celdaFinal = siguienteCelda;
				System.out.println("Celda final: fila --> " + celdaFinal.x + " columna --> " + celdaFinal.y + "\n");
				System.out.println("\nSE HA CONSTRUIDO EL LABERINTO CON ÉXITO.");	
			}

		}
	}

	public boolean esObjetivo(int f, int c) {
		return (cFinal.x == f && cFinal.y == c);
	}

	private void arreglarCaminos() {
		Celda ultimaCelda = camino.peek();
		if (camino.size() > 1) {
			Celda nextLastCelda = camino.elementAt(camino.size() - 2);
			if (ultimaCelda.norte) {
				nextLastCelda.sur = false;
			}
			if (ultimaCelda.sur) {
				nextLastCelda.norte = false;
			}
			if (ultimaCelda.este) {
				nextLastCelda.oeste = false;
			}
			if (ultimaCelda.oeste) {
				nextLastCelda.este = false;
			}
		}
	}

}
