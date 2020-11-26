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
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.NumberFormatException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class ClasePrincipal extends JPanel {

	private static Scanner TECLADO = new Scanner(System.in);
	static int filas;
	static int columnas;
	static String estrategia;

	public static void obtenerDimensionesJson() {
		do {
			try {
				System.out.println("Por favor, introduce el numero de filas del laberinto JSON que desea leer: ");
				filas = TECLADO.nextInt();
			} catch (Exception e) {
				System.err.println("Error solo se aceptan datos numericos.");
				TECLADO = new Scanner(System.in);
			}
		} while (filas < 2);
		do {
			try {
				System.out.println("Por favor, introduce el numero de columnas del laberinto JSON que desea leer: ");
				columnas = TECLADO.nextInt();
			} catch (Exception e) {
				System.err.println("Error solo se aceptan datos numericos.");
				TECLADO = new Scanner(System.in);
			}
		} while (columnas < 2);
	}

	public static void obtenerDimensionesMatriz() {
		do {
			try {
				System.out.println("Por favor, introduce el numero de filas que tendra el laberinto: ");
				filas = TECLADO.nextInt();
			} catch (Exception e) {
				System.err.println("Error solo se aceptan datos numericos.");
				TECLADO = new Scanner(System.in);
			}
		} while (filas < 2);
		do {
			try {
				System.out.println("Por favor, introduce el numero de columnas que tendra el laberinto: ");
				columnas = TECLADO.nextInt();
			} catch (Exception e) {
				System.err.println("Error solo se aceptan datos numericos.");
				TECLADO = new Scanner(System.in);
			}
		} while (columnas < 2);

	}

	public static void obtenerEstrategia() {
		boolean salir = false;
		int e = 0;
		do {
			try {
				System.out.println(
						"Por favor, elija la estrategia que desea seguir en el algoritmo de búsqueda:\n1.Búsqueda en anchura.\n2.Búsqueda en profundidad acotada.\n3.Búsqueda de costo uniforme.\n4.Búsqueda voraz.\n5.Búsqueda de A* ");
				e = TECLADO.nextInt();
				switch (e) {
				case 1:
					System.out.println("Ha elegido la estrategia de búsqueda en anchura.");
					estrategia = "BREADTH";
					salir = true;
					break;
				case 2:
					System.out.println("Ha elegido la estrategia de búsqueda en profundidad acotada.");
					estrategia = "DEPTH";
					salir = true;
					break;
				case 3:
					System.out.println("Ha elegido la estrategia de búsqueda de coste uniforme.");
					estrategia = "UNIFORM";
					salir = true;
					break;
				case 4:
					System.out.println("Ha elegido la estrategia de búsqueda voraz.");
					estrategia = "GREEDY";
					salir = true;
					break;
				case 5:
					System.out.println("Ha elegido la estrategia de búsqueda A*.");
					estrategia = "A";
					salir = true;
					break;
				default:
					System.out.println("Opción no válida.\n");
				}
			} catch (Exception i) {
				System.err.println("Error solo se aceptan datos numericos.\n");
				TECLADO = new Scanner(System.in);
			}
		} while (!salir);

	}

	public static void menu() {
		boolean salir = false;
		boolean valido = false;
		boolean fromJSON = false;
		MainLaberintoApp app = null;
		LwjglApplication lwjglApp = null;
		LwjglApplicationConfiguration config;
		ClaseJSONLeer json;
		int opcion;
		do {
			try {
				System.out.println(
						"Inserte la opcion que desea ejecutar: \n1.Crear fichero Json e imagen del laberinto. \n2.Leer fichero Json para crear el laberinto.");
				opcion = Integer.parseInt(TECLADO.next());
				switch (opcion) {
				case 1:
					fromJSON = false;
					valido = true;
					obtenerDimensionesMatriz();
					obtenerEstrategia();
					break;

				case 2:
					fromJSON = true;
					valido = true;
					obtenerDimensionesJson();
					obtenerEstrategia();
					break;

				default:
					System.out.println("Por favor inserte una de las opciones disponibles.");
					salir = false;
				}

			} catch (NumberFormatException e) {
				System.err.println("Error solo se aceptan datos numericos, vuelva a introducir la opción.");
				TECLADO = new Scanner(System.in);
			}

			if (!salir && valido) {
				json = new ClaseJSONLeer(filas, columnas);
				app = fromJSON ? json.getLaberintoFromJSON() : (new MainLaberintoApp());
				if (!fromJSON)
					app.setAnchuraAltura(filas, columnas);
				app.setAnchuraAltura(filas, columnas);
				app.setEstrategia(estrategia);
				app.setFromJSON(fromJSON);
				config = new LwjglApplicationConfiguration();
				config.width = 800;
				config.height = 600;
				config.disableAudio = true;
				lwjglApp = new LwjglApplication(app, config);
			}
		} while (!valido && !salir);
	}

	public static void main(String[] args) {
		menu();
	}
}