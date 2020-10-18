package LaberintoGit;

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

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class ClasePrincipal extends JPanel {

	private static final Scanner TECLADO = new Scanner(System.in);
	static int filas;
	static int columnas;

	
	public static void obtenerDimensionesMatriz() {
		do {
			System.out.println("Por favor, introduce el numero de filas que tenda el laberinto: ");
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
		app.setAnchuraAltura(rows, cols);
		app.setCeldasJSON(celdas);
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
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return cells;
	}
	
	public static JSONObject leerJSON() throws Exception{
		InputStream is = new FileInputStream("./Lab_Filas_Columnas.json");
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		String linea = buf.readLine();
		StringBuilder sb = new StringBuilder();
		while(linea != null){
			sb.append(linea).append("\n"); linea = buf.readLine();
			}

		return new JSONObject(sb.toString());	
	}
	
	public static void menu() {
		boolean salir = false;
		boolean fromJSON = false;
		MainLaberintoApp app = null;
		LwjglApplication lwjglApp = null;
		LwjglApplicationConfiguration config;
		do {
			try {
				System.out.println(
						"Inserte la opcion que desea ejecutar: \n1.Crear fichero Json e imagen del laberinto. \n2.Leer fichero Json para crear el laberinto");
				int opcion = TECLADO.nextInt();
				switch (opcion) {
				case 1:
					fromJSON = false;
					
					obtenerDimensionesMatriz();
					
					break;
				case 2:
					fromJSON = true;
					break;
				default:
					System.out.println("Por favor inserte una de las opciones disponibles.");
					salir = false;
				}
				
				if(!salir){
					app = fromJSON ? getLaberintoFromJSON() : (new MainLaberintoApp());
					if(!fromJSON)
						app.setAnchuraAltura(filas, columnas);
					config = new LwjglApplicationConfiguration();
					config.width = 600;
					config.height = 600;
					config.disableAudio = true;
					lwjglApp = new LwjglApplication(app, config);
				}

			} catch (NumberFormatException e) {
				System.err.println("Error solo se aceptan datos numericos.");
				salir = true;
			}
		}while(salir);

	}

	
	
	public static void main(String[] args) {
		menu();
	}
}
