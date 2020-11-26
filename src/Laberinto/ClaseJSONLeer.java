package Laberinto;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClaseJSONLeer {
	
	 int filas;
	 int columnas;
	
	public ClaseJSONLeer(int f, int c) {
		this.filas = f;
		this.columnas = c;
		
	}
	
	public MainLaberintoApp getLaberintoFromJSON() {
		MainLaberintoApp app = new MainLaberintoApp();
		try {
			JSONObject json = leerJSON();

			List<Celda> celdas = getJSONCells(json);
			List<Estado> estados = getJSONEstados(celdas);
			int rows = json.getInt("rows");
			int cols = json.getInt("cols");
			app.setAnchuraAltura(cols, rows);
			app.setCeldasJSON(celdas);
			app.setEstadosJson(estados);

			JSONObject jsonSucesores = leerJSONProblema();

			String initial = jsonSucesores.getString("INITIAL");
			System.out.println("INITIAL: " + initial);
			String objetive = jsonSucesores.getString("OBJETIVE");
			System.out.println("OBJETIVE: " + objetive);
			String maze = jsonSucesores.getString("MAZE");
			System.out.println("MAZE: " + maze);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return app;
	}
	
	public Celda obtenerCelda(List<Celda> lista, int x, int y) {
		Iterator it = lista.iterator();
		Celda encontrada = null;
		while (it.hasNext()) {
			Celda c = (Celda) it.next();
			if (c.x == x && c.y == y) {
				encontrada = c;
				break;
			}
		}
		return encontrada;
	}
	
	public JSONObject leerJSON() throws Exception {
		String data = "";
		try {
			data = new String(Files.readAllBytes(Paths.get("./problema_" + filas + "x" + columnas + "_maze.json")));
		} catch (Exception e) {
			System.out.println("Las dimensiones del laberinto no son correctas.");
			System.exit(-1);
		}

		return new JSONObject(data);
	}
	
	public JSONObject leerJSONProblema() throws Exception {

		String data = new String(Files.readAllBytes(Paths.get("./problema_" + filas + "x" + columnas + ".json")));

		return new JSONObject(data);
	}
	
	public List<Celda> getJSONCells(JSONObject demo) {
		List<Celda> cells = new ArrayList<>();
		try {
			JSONObject jsonCells = demo.getJSONObject("cells");
			Iterator<String> it = jsonCells.keys();
			while (it.hasNext()) {
				String cellCoords = it.next();
				JSONObject cellJSON = jsonCells.getJSONObject(cellCoords);
				cellCoords = cellCoords.replace("(", "");
				cellCoords = cellCoords.replace(")", "");
				cellCoords = cellCoords.replace(" ", "");

				String[] coords = cellCoords.split(",");
				int x = Integer.parseInt(coords[0]);
				int y = Integer.parseInt(coords[1]);

				Celda cell = new Celda(x, y);

				JSONArray neighboursArray = cellJSON.getJSONArray("neighbors");

				Boolean north = neighboursArray.getBoolean(0);
				Boolean east = neighboursArray.getBoolean(1);
				Boolean south = neighboursArray.getBoolean(2);
				Boolean west = neighboursArray.getBoolean(3);

				cell.norte = north;
				cell.este = east;
				cell.sur = south;
				cell.oeste = west;

				cell.setValor(cellJSON.getInt("value"));
				cells.add(cell);
			}

			Iterator cellsIt = cells.iterator();
			boolean consistente = true;
			Celda celdaInconsistente = null;
			while (cellsIt.hasNext()) {
				Celda c = (Celda) cellsIt.next();
				if (c.norte) {
					Celda celdaNorte = obtenerCelda(cells, c.x - 1, c.y);
					if (celdaNorte == null || !celdaNorte.sur) {
						consistente = false;
						celdaInconsistente = c;
						break;
					}
				}
				if (c.sur) {
					Celda celdaSur = obtenerCelda(cells, c.x + 1, c.y);
					if (celdaSur == null || !celdaSur.norte) {
						consistente = false;
						celdaInconsistente = c;
						break;
					}
				}
				if (c.este) {
					Celda celdaEste = obtenerCelda(cells, c.x, c.y + 1);
					if (celdaEste == null || !celdaEste.oeste) {
						consistente = false;
						celdaInconsistente = c;
						break;
					}
				}
				if (c.oeste) {
					Celda celdaOeste = obtenerCelda(cells, c.x, c.y - 1);
					if (celdaOeste == null || !celdaOeste.este) {
						consistente = false;
						celdaInconsistente = c;
						break;
					}
				}

			}
			if (!consistente) {
				System.out.println("Se ha encontrado una inconsistencia en la celda" + celdaInconsistente);
				System.exit(-1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return cells;
	}
	

	public  List<Estado> getJSONEstados(List<Celda> celdas) {
		List<Estado> estados = new LinkedList<>();
		Id id = null;
		Estado e = null;
		for (int i = 0; i < celdas.size(); i++) {
			id = new Id(celdas.get(i).x, celdas.get(i).y);
			e = new Estado(id, celdas.get(i).getValor());
			estados.add(e);
		}
		return estados;
	}


}

