package Laberinto;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClaseJSONEscribir {

	int altura;
	int anchura;
	ResolverLaberinto a;
	List<Celda> laberinto;
	List<Estado> celdasValor;

	public ClaseJSONEscribir(int f, int c, List<Celda> laberinto, List<Estado> celdasValor) {
		this.altura = f;
		this.anchura = c;
		this.laberinto = laberinto;
		this.celdasValor = celdasValor;
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
		for (Estado estado : celdasValor) {
			Id idCelda = estado.idEstado;
			Celda celda = getCelda(idCelda.getFila(), idCelda.getColumna());

			JSONObject jsonCelda = celda.getJSON();
			jsonCelda.put("value", estado.valor);
			jsonCeldas.put("(" + idCelda.getFila() + ", " + idCelda.getColumna() + ")", jsonCelda);
		}

		laberintoJSON.put("cells", jsonCeldas);

		return laberintoJSON;
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

	public JSONObject toJSONProblema() {
		JSONObject problemaJSON = new JSONObject();
		JSONArray cInicial = new JSONArray();
		JSONArray cObjetivo = new JSONArray();

		try {
			Field changeMap = problemaJSON.getClass().getDeclaredField("map");
			changeMap.setAccessible(true);
			changeMap.set(problemaJSON, new LinkedHashMap<>());
			changeMap.setAccessible(false);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			System.out.println("Excepción.");
		}

		cInicial.put(0, 0);
		cInicial.put(1, 0);

		String inicial = "(0, 0)";
		String objetivo = "(" + (altura - 1) + ", " + (anchura - 1) + ")";

		problemaJSON.put("INITIAL", inicial);
		problemaJSON.put("OBJETIVE", objetivo);
		problemaJSON.put("MAZE", "problema_" + altura + "x" + anchura + "_maze.json");

		return problemaJSON;
	}

}
