package Laberinto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import Laberinto.Sucesor.Movimiento;

public class WilsonsLaberinto {
	List<Celda> noVisitadas = new ArrayList<>();
	List<Celda> laberinto = new ArrayList<>();
	PriorityQueue<Nodo> frontera = new PriorityQueue<Nodo>();
	Stack<Celda> camino = new Stack<>();
	Celda cFinal = null;
	int anchura, altura;

	public WilsonsLaberinto(int altura, int anchura) {
		this.anchura = anchura;
		this.altura = altura;
		Random aleatorio = new Random();

		Celda inicial = new Celda(aleatorio.nextInt(altura), aleatorio.nextInt(anchura));
		// Primer nodo
		laberinto.add(inicial);
		System.out.println("Celda comienzo: fila -->" + inicial.x + " columna -->" + inicial.y);

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
			// Estado estadoComienzo = new Estado (new Id
			// (celdaComienzo.x,celdaComienzo.y));
			// System.out.println(estadoComienzo);
			camino.add(celdaComienzo);
		} else {
			Celda ultimaCelda = camino.peek();

			int dir = -1;
			boolean valid = false;
			Celda siguienteCelda = null;
			do {
				dir = (int) (Math.random() * 4);

				if (dir == 0 && ultimaCelda.x != 0) { // North
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

				} else if (dir == 2 && ultimaCelda.x != altura - 1) { // South
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
				} else if (dir == 3 && ultimaCelda.y != 0) { // West
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
				} else if (dir == 1 && ultimaCelda.y != anchura - 1) { // East
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
				System.out.println("Celda final: " +celdaFinal);
				for (int i=0; i < laberinto.size(); i++) {
					List<Sucesor> mSucesores = sucesores(laberinto.get(i));
					Celda celdaSucesores = laberinto.get(i);
					System.out.println("Celda: " +laberinto.get(i));
					System.out.println("Sucesores: " + sucesores(laberinto.get(i)));
				}
				practica2();
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

	public JSONObject toJSON() {
		JSONObject laberintoJSON = new JSONObject();
		JSONArray c = new JSONArray();
		JSONArray c0 = new JSONArray();
		JSONArray c1 = new JSONArray();
		JSONArray c2 = new JSONArray();
		JSONArray c3 = new JSONArray();

		JSONArray idMov = new JSONArray();
		try {
			Field changeMap = laberintoJSON.getClass().getDeclaredField("map");
			changeMap.setAccessible(true);
			changeMap.set(laberintoJSON, new LinkedHashMap<>());
			changeMap.setAccessible(false);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			System.out.println("Excepción.");
		}
		laberintoJSON.put("cols", altura);
		laberintoJSON.put("rows", anchura);

		c0.put(0, -1);
		c0.put(1, 0);

		c1.put(0, 0);
		c1.put(1, 1);

		c2.put(0, 1);
		c2.put(1, 0);

		c3.put(0, 0);
		c3.put(1, -1);

		c.put(c0);
		c.put(c1);
		c.put(c2);
		c.put(c3);

		idMov.put(0, "N");
		idMov.put(1, "E");
		idMov.put(2, "S");
		idMov.put(3, "O");

		laberintoJSON.put("mov", c);
		laberintoJSON.put("max_n", 4);
		laberintoJSON.put("id_mov", idMov);

		JSONObject jsonCeldas = new JSONObject();
		for (Celda cell : laberinto) {
			jsonCeldas.put("(" + cell.x + ", " + cell.y + ")", cell.getJSON());
		}

		laberintoJSON.put("cells", jsonCeldas);

		return laberintoJSON;
	}

	// Crear estructura JSON sucesores.
	public JSONObject toJSONSucesores() {
		JSONObject sucesoresJSON = new JSONObject();
		String inicial = "(0, 0)";
		String objetivo = "("+(altura-1)+", "+(anchura-1)+")";

		try {
			Field changeMap = sucesoresJSON.getClass().getDeclaredField("map");
			changeMap.setAccessible(true);
			changeMap.set(sucesoresJSON, new LinkedHashMap<>());
			changeMap.setAccessible(false);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			System.out.println("Excepción.");
		}

		sucesoresJSON.put("INITIAL", inicial);
		sucesoresJSON.put("OBJETIVE", objetivo);
		sucesoresJSON.put("MAZE", "sucesores_" + altura + "X" + anchura + "_maze.json");

		return sucesoresJSON;
	}

	public Celda getCelda(int x, int y) {
		Celda celda = null;
		for (int i = 0; i < laberinto.size(); i++) {
			Celda c = laberinto.get(i);
			if (c.x == x && c.y == y) {
				celda = c;
				break;
			}
		}
		return celda;
	}

	public List<Sucesor> sucesores(Celda celda) {
		List<Sucesor> sucesoresX = new LinkedList<>();
		int x = celda.x;
		int y = celda.y;

		Celda norte = null;
		Celda este = null;
		Celda sur = null;
		Celda oeste = null;

		if (celda.norte) {
			norte = getCelda(x - 1, y);
			sucesoresX.add(new Sucesor(Movimiento.N, norte, 1));
		}
		if (celda.este) {
			este = getCelda(x, y + 1);
			sucesoresX.add(new Sucesor(Movimiento.E, este, 1));
		}
		if (celda.sur) {
			sur = getCelda(x + 1, y);
			sucesoresX.add(new Sucesor(Movimiento.S, sur, 1));
		}
		if (celda.oeste) {
			oeste = getCelda(x, y - 1);
			sucesoresX.add(new Sucesor(Movimiento.O, oeste, 1));
		}

		return sucesoresX;
	}

	/*
	 * public Boolean objetivo(int f, int c) { return (f == cFinal.x && c ==
	 * cFinal.y); }
	 */

	public void practica2() {
		int posicion = (int) Math.random() * laberinto.size();
		Celda inicial = laberinto.get(posicion);
		frontera = resolverProblema(frontera, inicial);
	}

	public PriorityQueue<Nodo> resolverProblema(PriorityQueue<Nodo> frontera, Celda inicial) {
		boolean encontrado = false;

		System.out.println("La celda inicial para el problema es --------> " + inicial.toString());
		Id idInicial = new Id(inicial.x, inicial.y);
		Estado estadoInicial = new Estado(idInicial, 0);
		Nodo nodoRaiz = new Nodo(estadoInicial, null, 0, 0, 0, 5, null);
		int id = nodoRaiz.getId();
		System.out.println("Nodo raiz: " + nodoRaiz.toString());

		// Vemos los vecinos de esta celda
		if (inicial.getNorte() == true) {
			for (int j = 0; j < laberinto.size() && !encontrado; j++) {
				// Celda siguiente = new Celda(inicial.x - 1, inicial.y);
				Id siguienteId = new Id(inicial.x - 1, inicial.y);
				Estado siguienteEstado = new Estado(siguienteId, 0);
				Celda comparar = laberinto.get(j);
				if (siguienteId.getFila() == comparar.x && siguienteId.getColumna() == comparar.y) {
					encontrado = true;
					id = id + 1;
					Nodo siguienteNodo = new Nodo(siguienteEstado, nodoRaiz, nodoRaiz.getCosto_acumulado() + 1,
							nodoRaiz.getProfundidad() + 1, id, 0, "N");
					// System.out.println("Nodo padre : " + siguienteNodo.getPadre());
					System.out.println("Vecino norte encontrado : " + siguienteNodo.toString());
					frontera.add(siguienteNodo);
				}
			}
			encontrado = false;
		} else
			System.out.println("No tiene vecino en el norte");

		if (inicial.getEste() == true) {
			for (int j = 0; j < laberinto.size() && !encontrado; j++) {
				// Celda siguiente = new Celda(inicial.x, inicial.y + 1);
				Id siguienteId = new Id(inicial.x, inicial.y + 1);
				Estado siguienteEstado = new Estado(siguienteId, 0);
				Celda comparar = laberinto.get(j);
				if (siguienteId.getFila() == comparar.x && siguienteId.getColumna() == comparar.y) {
					encontrado = true;
					id = id + 1;
					Nodo siguienteNodo = new Nodo(siguienteEstado, nodoRaiz, nodoRaiz.getCosto_acumulado() + 1,
							nodoRaiz.getProfundidad() + 1, id, 0, "E");
					frontera.add(siguienteNodo);
					System.out.println("Vecino este encontrado : " + siguienteNodo.toString());
				}
			}
			encontrado = false;
		} else
			System.out.println("No tiene vecino en el este");
		if (inicial.getSur() == true) {
			for (int j = 0; j < laberinto.size() && !encontrado; j++) {
				// Celda siguiente = new Celda(inicial.x + 1, inicial.y);
				Id siguienteId = new Id(inicial.x + 1, inicial.y);
				Estado siguienteEstado = new Estado(siguienteId, 0);
				Celda comparar = laberinto.get(j);
				if (siguienteId.getFila() == comparar.x && siguienteId.getColumna() == comparar.y) {
					encontrado = true;
					id = id + 1;
					Nodo siguienteNodo = new Nodo(siguienteEstado, nodoRaiz, nodoRaiz.getCosto_acumulado() + 1,
							nodoRaiz.getProfundidad() + 1, id, 0, "S");
					frontera.add(siguienteNodo);
					System.out.println("Vecino sur encontrado : " + siguienteNodo.toString());
				}
			}
			encontrado = false;
		} else
			System.out.println("No tiene vecino en el sur");

		if (inicial.getOeste() == true) {
			for (int j = 0; j < laberinto.size() && !encontrado; j++) {
				// Celda siguiente = new Celda(inicial.x, inicial.y - 1);
				Id siguienteId = new Id(inicial.x, inicial.y - 1);
				Estado siguienteEstado = new Estado(siguienteId, 0);
				Celda comparar = laberinto.get(j);
				if (siguienteId.getFila() == comparar.x && siguienteId.getColumna() == comparar.y) {
					encontrado = true;
					id = id + 1;
					Nodo siguienteNodo = new Nodo(siguienteEstado, nodoRaiz, nodoRaiz.getCosto_acumulado() + 1,
							nodoRaiz.getProfundidad() + 1, id, 0, "O");
					System.out.println("Vecino oeste encontrado : " + siguienteNodo.toString());
					frontera.add(siguienteNodo);
				}
			}
			encontrado = false;
		} else
			System.out.println("No tiene vecino en el oeste");

		if (frontera.size() != 0) {
			Nodo siguienteNodo = frontera.peek();
			System.out.println("El primer nodo de la priorityqueue es antes de insertar los nodos de prueba: "
					+ siguienteNodo.toString());
		}
		// Para comprobar que la frontera se ha creado de manera correcta, introducimos
		// dos nodos con id 0, de manera que sus filas y columnas decidirán cual se
		// coloca con mayor prioridad (primero el que menor fila tenga y si coinciden,
		// la menor columna)
		Id id0 = new Id(0, 0);
		Id id1 = new Id(1, 1);
		Estado estado0 = new Estado(id0, 0);
		Estado estado1 = new Estado(id1, 0);
		Nodo nodoComprobar0 = new Nodo(estado0, null, 0, 0, 0, 0, "S");
		Nodo nodoComprobar1 = new Nodo(estado1, null, 0, 0, 0, 0, "E");
		frontera.add(nodoComprobar1);
		frontera.add(nodoComprobar0);
		System.out.println("Tras insertar los nodos de prueba, la frontera queda: ");
		int contador = 0;
		do {
			System.out.println("Elemento número " + contador + " --> " + frontera.poll());
			contador++;
		} while (!frontera.isEmpty());

		return frontera;
	}
}