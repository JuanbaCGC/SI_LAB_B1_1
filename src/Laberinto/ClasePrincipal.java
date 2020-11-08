package Laberinto;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class ClasePrincipal extends JPanel {

	private static final Scanner TECLADO = new Scanner(System.in);
	static int filas;
	static int columnas;
	
	public static void obtenerDimensionesJson(){
		do {
			System.out.println("Por favor, introduce el numero de filas del laberinto de sucesores que desea leer: ");
			filas = TECLADO.nextInt();
		}while(filas < 2);
		do {
			System.out.println("Por favor, introduce el numero de columnas del laberinto de sucesores que desea leer: ");
			columnas = TECLADO.nextInt();
		}while(columnas < 2);
	}
	
	public static void obtenerDimensionesMatriz() {
		do {
			System.out.println("Por favor, introduce el numero de filas que tendra el laberinto: ");
			filas = TECLADO.nextInt();
		}while(filas < 2);
		do {
			System.out.println("Por favor, introduce el numero de columnas que tendra el laberinto: ");
			columnas = TECLADO.nextInt();
		}while(columnas < 2);
	}
	
	public static MainLaberintoApp getLaberintoFromJSON() {
		MainLaberintoApp app = new MainLaberintoApp();
		try {
		JSONObject json = leerJSON();
		
		List<Celda> celdas = getJSONCells(json);
		int rows = json.getInt("rows");
		int cols = json.getInt("cols");
		app.setAnchuraAltura(cols, rows);
		app.setCeldasJSON(celdas);
		
		JSONObject jsonSucesores = leerJSONProblema();
		
		String initial = jsonSucesores.getString("INITIAL");
		System.out.println("INITIAL: "+initial);
		String objetive = jsonSucesores.getString("OBJETIVE");
		System.out.println("OBJETIVE: "+objetive);
		String maze = jsonSucesores.getString("MAZE");
		System.out.println("MAZE: "+maze);
		
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return app;
		
	}
	
	public static List<Celda> getJSONCells(JSONObject demo) {
		List<Celda> cells = new ArrayList<>();
		
		try {
			JSONObject jsonCells = demo.getJSONObject("cells");
			
			Iterator<String> it = jsonCells.keys();
			while(it.hasNext()){
				String cellCoords = it.next();
				JSONObject cellJSON = jsonCells.getJSONObject(cellCoords);
				cellCoords = cellCoords.replace("(", "");
				cellCoords = cellCoords.replace(")", "");
				cellCoords = cellCoords.replace(" ", "");
				
				String[] coords = cellCoords.split(",");
				int x = Integer.parseInt(coords[0]);
				int y = Integer.parseInt(coords[1]);
				
				Celda cell = new Celda(x,y);
				JSONArray neighboursArray = cellJSON.getJSONArray("neighbors");
				
				Boolean north = neighboursArray.getBoolean(0);
				Boolean east = neighboursArray.getBoolean(1);
				Boolean south = neighboursArray.getBoolean(2);
				Boolean west = neighboursArray.getBoolean(3);
				
				cell.norte = north;
				cell.este = east;
				cell.sur = south;
				cell.oeste = west;
				
				cells.add(cell);
			}
			
			Iterator cellsIt = cells.iterator();
			boolean consistente = true;
			Celda celdaInconsistente = null;
			while(cellsIt.hasNext()){
				Celda c = (Celda) cellsIt.next();
				if(c.norte){
					Celda celdaNorte = obtenerCelda(cells, c.x-1, c.y);
					if (celdaNorte == null || !celdaNorte.sur ){
						consistente = false;
						celdaInconsistente = c;
						break;
					}
				}
				if(c.sur){
					Celda celdaSur = obtenerCelda(cells, c.x+1, c.y);
					if (celdaSur == null || !celdaSur.norte ){
						consistente = false;
						celdaInconsistente = c;
						break;
					}
				}
				if(c.este){
					Celda celdaEste = obtenerCelda(cells, c.x, c.y+1);
					if (celdaEste == null || !celdaEste.oeste ){
						consistente = false;
						celdaInconsistente = c;
						break;
					}
				}
				if(c.oeste){
					Celda celdaOeste = obtenerCelda(cells, c.x, c.y-1);
					if (celdaOeste == null || !celdaOeste.este ){
						consistente = false;
						celdaInconsistente = c;
						break;
					}
				}
				
			}
			if (!consistente){
				System.out.println("Se ha encontrado una inconsistencia en la celda" + celdaInconsistente);
				System.exit(-1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return cells;
	}

	
	public static Celda obtenerCelda(List<Celda> lista, int x, int y){
		Iterator it = lista.iterator();
		Celda encontrada = null;
		while(it.hasNext()){
			Celda c = (Celda) it.next();
			if (c.x == x && c.y ==y){
				encontrada = c;
				break;
			}
		}
		return encontrada;
	}
	
	public static JSONObject leerJSON() throws Exception{
		String data = new String ( Files.readAllBytes( Paths.get("./Lab_Filas_Columnas.json") ) );
		return new JSONObject(data);	
	}
	
	public static JSONObject leerJSONProblema() throws Exception{
		String data = "";
		try{
			data = new String ( Files.readAllBytes( Paths.get("./sucesores_"+filas+"X"+columnas+".json") ) );
		}catch(Exception e){
			System.out.println("Las dimensiones del laberinto no son correctas.");
			System.exit(-1);
		}
		return new JSONObject(data);
	}
	
	public static void menu() {
		boolean salir = false;
		boolean valido = false;
		boolean fromJSON = false;
		MainLaberintoApp app = null;
		LwjglApplication lwjglApp = null;
		LwjglApplicationConfiguration config;
		do {
			try {
				System.out.println(
						"Inserte la opcion que desea ejecutar: \n1.Crear fichero Json e imagen del laberinto. \n2.Leer fichero Json para crear el laberinto.");
				int opcion = TECLADO.nextInt();
				switch (opcion) {
				case 1:
					fromJSON = false;
					valido = true;
					obtenerDimensionesMatriz();
					
					break;
				case 2:
					fromJSON = true;
					valido = true;
					obtenerDimensionesJson();
					break;
				default:
					System.out.println("Por favor inserte una de las opciones disponibles.");
					salir = false;
				}
				
				if(!salir && valido){
					app = fromJSON ? getLaberintoFromJSON() : (new MainLaberintoApp());
					if(!fromJSON)
						app.setAnchuraAltura(filas, columnas);
					app.setFromJSON(fromJSON);
					config = new LwjglApplicationConfiguration();
					config.width = 800;
					config.height = 600;
					config.disableAudio = true;
					lwjglApp = new LwjglApplication(app, config);
				}

			} catch (NumberFormatException e) {
				System.err.println("Error solo se aceptan datos numericos.");
				salir = true;
			}
		}while(!valido);

	}	
	
	public static void main(String[] args) {
		menu();
	}
}