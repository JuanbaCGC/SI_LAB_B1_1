package Laberinto;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedHashMap;

public class ResolverLaberinto {
	List<Estado> estadosVisitados = new LinkedList<>();
	List<Estado> celdasValor = new LinkedList<>();
	List<Estado> valoresJSON = new LinkedList<>();
	List<Celda> laberinto = new ArrayList<>();
	PriorityQueue<Nodo> frontera = new PriorityQueue<Nodo>();
	int altura, anchura;
	String estrategia;

	public ResolverLaberinto(int altura, int anchura, String estrategia, List<Celda> laberinto) {
		this.altura = altura;
		this.anchura = anchura;
		this.estrategia = estrategia;
		this.laberinto = laberinto;

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

	public double comprobarValorCelda(Celda c) {
		double valor = 0;
		if (celdasValor.isEmpty()) {
			for (int i = 0; i < valoresJSON.size(); i++) {
				if (valoresJSON.get(i).getIdEstado().getFila() == c.x
						&& valoresJSON.get(i).getIdEstado().getColumna() == c.y) {
					valor = valoresJSON.get(i).getValor();
				}

			}
		} else {
			for (int i = 0; i < celdasValor.size(); i++) {
				if (celdasValor.get(i).getIdEstado().getFila() == c.x
						&& celdasValor.get(i).getIdEstado().getColumna() == c.y) {
					valor = celdasValor.get(i).getValor();
				}

			}
		}

		return valor;
	}

	public List<Nodo> expandir_nodo(Problema problema, Nodo nodo, String estrategia, int id) {
		List<Nodo> listaNodos = new LinkedList<>();
		List<Sucesor> listaSucesores = new LinkedList<>();
		listaSucesores = problema.getSucesores(nodo.getEstado());
		Nodo nodoHijo = null;
		Nodo nodoAuxiliar = null;
		for (int i = 0; i < listaSucesores.size(); i++) {
			id++;
			nodoAuxiliar = new Nodo(listaSucesores.get(i).getEstado(), nodo,
					nodo.costo_acumulado + listaSucesores.get(i).getEstado().getValor()
							+ listaSucesores.get(i).getCosto(),
					nodo.profundidad + 1, id, heurística(problema, listaSucesores.get(i).getEstado()),
					listaSucesores.get(i).getAccion());
			nodoHijo = new Nodo(listaSucesores.get(i).getEstado(), nodo,
					nodo.costo_acumulado + listaSucesores.get(i).getEstado().getValor()
							+ listaSucesores.get(i).getCosto(),
					nodo.profundidad + 1, id, heurística(problema, listaSucesores.get(i).getEstado()),
					listaSucesores.get(i).getAccion(), calcula(estrategia, nodoAuxiliar));
			listaNodos.add(nodoHijo);
		}
		return listaNodos;

	}

	public List<Estado> getCeldasValor() {
		return celdasValor;
	}

	public Estado obtenerEstadoInicial() {
		Estado estadoInicial = new Estado(null, 0);
		for (int i = 0; i < celdasValor.size(); i++) {
			if (celdasValor.get(i).idEstado.getFila() == 0 && celdasValor.get(i).idEstado.getColumna() == 0) {
				estadoInicial = celdasValor.get(i);
			}
		}
		return estadoInicial;
	}

	public Estado obtenerEstadoInicialJSON() {
		Estado estadoInicial = new Estado(null, 0);
		for (int i = 0; i < valoresJSON.size(); i++) {
			if (valoresJSON.get(i).idEstado.getFila() == 0 && valoresJSON.get(i).idEstado.getColumna() == 0) {
				estadoInicial = valoresJSON.get(i);
			}
		}
		return estadoInicial;
	}

	public Estado obtenerEstadoObjetivo() {
		Estado estadoObjetivo = new Estado(null, 0);
		for (int i = 0; i < celdasValor.size(); i++) {
			if (celdasValor.get(i).idEstado.getFila() == altura - 1
					&& celdasValor.get(i).idEstado.getColumna() == anchura - 1) {
				estadoObjetivo = celdasValor.get(i);
			}
		}
		return estadoObjetivo;
	}

	public Estado obtenerEstadoObjetivoJSON() {
		Estado estadoObjetivo = new Estado(null, 0);
		for (int i = 0; i < valoresJSON.size(); i++) {
			if (valoresJSON.get(i).idEstado.getFila() == altura - 1
					&& valoresJSON.get(i).idEstado.getColumna() == anchura - 1) {
				estadoObjetivo = valoresJSON.get(i);
			}
		}
		return estadoObjetivo;
	}

	public void inicializarValoresCeldas() {
		Celda c;
		Id id;
		int valor;
		Estado a;
		for (int i = 0; i < laberinto.size(); i++) {
			c = laberinto.get(i);
			id = new Id(c.x, c.y);
			valor = (int) (Math.random() * 4);
			a = new Estado(id, valor);
			celdasValor.add(a);
		}

	}

	public void inicializarValoresJSON(List<Estado> estados) {
		this.valoresJSON = estados;
	}

	public Nodo creaNodo() {
		Nodo nodoInicial = new Nodo(null, null, 0, 0, 0, 0, null, 0);
		return nodoInicial;
	}

	public int heurística(Problema problema, Estado estado) {
		int valor = 0;
		valor = Math.abs(estado.getIdEstado().getFila() - problema.objetivo.getIdEstado().getFila())
				+ Math.abs(estado.getIdEstado().getColumna() - problema.objetivo.getIdEstado().getColumna());
		return valor;

	}

	public double calcula(String estrategia, Nodo nodo) {
		double valor = 0;
		if (estrategia == "BREADTH") {
			valor = nodo.getProfundidad();
		} else if (estrategia == "DEPTH") {
			valor = (1 / (nodo.getProfundidad() * 1.0 + 1));
		} else if (estrategia == "UNIFORM") {
			valor = nodo.getCosto_acumulado();
		} else if (estrategia == "GREEDY") {
			valor = nodo.getHeuristica();
		} else if (estrategia == "A") {
			valor = nodo.getCosto_acumulado() + nodo.getHeuristica();
		}
		return valor;
	}

	public Celda comprobar(Celda celda) {
		Celda c = new Celda(0, 0);
		for (int i = 0; i < laberinto.size(); i++) {
			if (laberinto.get(i).x == celda.x && laberinto.get(i).y == celda.y) {
				c = laberinto.get(i);
			}

		}
		return c;
	}

	public boolean funcionObjetivo(Problema problema, Nodo nodo) {
		boolean solucion = false;
		if (problema.objetivo.getIdEstado().getFila() == nodo.estado.getIdEstado().getFila()
				&& problema.objetivo.getIdEstado().getColumna() == nodo.estado.getIdEstado().getColumna()) {
			solucion = true;
		}

		return solucion;
	}

	public Nodo obtenerNodo(Nodo o) {
		Celda c = new Celda(0, 0);
		Nodo nodo = new Nodo(null, null, 0, 0, 0, 0, null, 0);
		for (int i = 0; i < laberinto.size(); i++) {
			if (laberinto.get(i).x == o.getEstado().getIdEstado().getFila()
					&& laberinto.get(i).y == o.getEstado().getIdEstado().getColumna()) {
				c = laberinto.get(i);
				Id id = new Id(c.x, c.y);
				Estado estado = new Estado(id, o.getValor());
				nodo = new Nodo(estado, o.padre, o.costo_acumulado, o.profundidad, 0, o.heuristica, o.accion, o.valor);
			}

		}
		return nodo;

	}

	public Nodo busqueda(Problema problema, int profundidad, String estrategia) {
		PriorityQueue<Nodo> frontera = new PriorityQueue<Nodo>();
		List<Nodo> listaNodosHijos = new LinkedList<>();
		Estado[] espacioEstados = new Estado[0];
		Visitados visitados = new Visitados(espacioEstados);
		List<Nodo> camino = new LinkedList<>();
		Nodo nodoInicial = creaNodo();
		Nodo nodoAuxiliar = creaNodo();
		Nodo nodo = creaNodo();
		nodoAuxiliar = new Nodo(problema.estadoInicial, null, 0, 0, 0, heurística(problema, problema.estadoInicial),
				null);
		nodoInicial = new Nodo(problema.estadoInicial, null, 0, 0, 0, heurística(problema, problema.estadoInicial),
				null, calcula(estrategia, nodoAuxiliar));
		frontera.add(obtenerNodo(nodoInicial));
		int idNodo = nodoInicial.getId();
		boolean esSolucion = false;
		while (!frontera.isEmpty() && !esSolucion) {
			nodo = frontera.poll();
			if (funcionObjetivo(problema, nodo)) {
				esSolucion = true;

			} else if (!visitados.pertenece(nodo.getEstado()) && nodo.profundidad < profundidad) {
				visitados.insertarEstado(nodo.getEstado());
				listaNodosHijos = expandir_nodo(problema, nodo, estrategia, idNodo);
				for (int i = 0; i < listaNodosHijos.size(); i++) {
					idNodo++;
					frontera.add(listaNodosHijos.get(i));
				}
			}
		}

		if (esSolucion) {
			System.out.println("\nSE HA LLEGADO CON ÉXITO AL NODO OBJETIVO.\n");
			camino = encontrarCaminoNodos(problema, nodo);
			devolverCaminoNodos(camino);

		} else {
			System.out.println("No hay solución.");
		}
		return nodo;

	}

	public List<Nodo> encontrarCaminoNodos(Problema problema, Nodo nodo) {
		List<Nodo> camino = new LinkedList<>();
		Nodo siguienteNodo = null;
		siguienteNodo = new Nodo(nodo.getEstado(), nodo.getPadre(), nodo.getCosto_acumulado(), nodo.getProfundidad(),
				nodo.getId(), nodo.getHeuristica(), nodo.getAccion(), nodo.getValor());
		camino.add(siguienteNodo);
		while (!(siguienteNodo.getPadre().getEstado().getIdEstado().getFila() == 0
				&& siguienteNodo.getPadre().getEstado().getIdEstado().getColumna() == 0)) {
			siguienteNodo = new Nodo(siguienteNodo.getPadre().getEstado(), siguienteNodo.getPadre().getPadre(),
					siguienteNodo.getPadre().getCosto_acumulado(), siguienteNodo.getPadre().getProfundidad(),
					siguienteNodo.getPadre().getId(), siguienteNodo.getPadre().getHeuristica(),
					siguienteNodo.getPadre().getAccion(), siguienteNodo.getPadre().getValor());
			camino.add(siguienteNodo);
		}
		siguienteNodo = new Nodo(siguienteNodo.getPadre().getEstado(), siguienteNodo.getPadre().getPadre(),
				siguienteNodo.getPadre().getCosto_acumulado(), siguienteNodo.getPadre().getProfundidad(),
				siguienteNodo.getPadre().getId(), siguienteNodo.getPadre().getHeuristica(),
				siguienteNodo.getPadre().getAccion(), siguienteNodo.getPadre().getValor());
		camino.add(siguienteNodo);

		Collections.reverse(camino);

		return camino;

	}

	public void devolverCaminoNodos(List<Nodo> camino) {
		StringBuilder sb = new StringBuilder();
		int value = (int) camino.get(0).getValor();
		sb.append("\n CAMINO\n");
		sb.append("[ID]" + "[COSTE, ESTADO, ID_PADRE, ACCIÓN, PROFUNDIDAD, HEURISTICA,VALOR]\n");
		sb.append("[" + camino.get(0).getId() + "]" + "[" + (int) camino.get(0).getCosto_acumulado() + ", "
				+ camino.get(0).getEstado() + ", " + camino.get(0).getId() + ", " + camino.get(0).getAccion() + ", "
				+ camino.get(0).getProfundidad() + ", " + camino.get(0).getHeuristica() + ", "
				+ (hasDecimalPart(camino.get(0).getValor()) ? camino.get(0).getValor() : String.format("%d", value))
				+ "]\n");
		for (int i = 1; i < camino.size(); i++) {
			value = (int) camino.get(i).getValor();
			sb.append("[" + camino.get(i).getId() + "]" + "[" + (int) camino.get(i).getCosto_acumulado() + ", "
					+ camino.get(i).getEstado() + ", " + camino.get(i - 1).getId() + ", " + camino.get(i).getAccion()
					+ ", " + camino.get(i).getProfundidad() + ", " + camino.get(i).getHeuristica() + ", "
					+ (hasDecimalPart(camino.get(i).getValor()) ? camino.get(i).getValor() : String.format("%d", value))
					+ "]\n");
		}
		System.out.println(sb.toString());
		try {
			FileWriter myWriter = new FileWriter("sol_" + altura + "x" + anchura + "_" + this.estrategia + ".txt");
			myWriter.write(sb.toString());
			myWriter.close();
			System.out.println("Se ha generado el fichero .txt.");
		} catch (IOException e) {
			System.out.println("Ha ocurrido un error al generar el fichero .txt.");
			e.printStackTrace();
		}

	}

	boolean hasDecimalPart(double value) {
		boolean result = false;
		int number = (int) value;
		result = ((value - number) > 0.0);
		return result;
	}

}
