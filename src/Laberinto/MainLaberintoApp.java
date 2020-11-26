package Laberinto;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

public class MainLaberintoApp extends ApplicationAdapter {
	SpriteBatch batch;
	int anchuraLaberinto = 1;
	int alturaLaberinto = 1;
	String estrategialab = "";
	WilsonsLaberinto laberinto;
	ResolverLaberinto rl;
	Problema problema;
	Texture tex;

	boolean acabado = false;
	Pixmap pixmap;
	int tamañoCelda = 10;
	int celdaPadding = 1;
	boolean fromJSON = false;

	float timer = 0.0f;
	List<Celda> celdasJSON;
	List<Estado> estadosJSON;
	List<Estado> valoresJSON;
	String estrategia;

	public void setAnchuraAltura(int altura, int anchura) {
		alturaLaberinto = altura;
		anchuraLaberinto = anchura;
	}

	public void setEstrategia(String estrategia) {
		estrategialab = estrategia;
	}

	public void create() {
		batch = new SpriteBatch();
		pixmap = new Pixmap(alturaLaberinto * (tamañoCelda) * 2, anchuraLaberinto * (tamañoCelda) * 2, Format.RGBA8888);
		laberinto = new WilsonsLaberinto(alturaLaberinto, anchuraLaberinto, estrategialab);
		laberinto.inicializarLaberinto();
		if (celdasJSON != null) {
			laberinto.laberinto = celdasJSON;
			tex = new Texture(generatePixmap());
		}
	}

	@Override
	public void render() {
		if (!laberinto.estaCompleto()) {
			laberinto.siguienteCelda();
			tex = new Texture(generatePixmap());
			timer = 0;
		} else {
			List<Celda> maze = laberinto.laberinto;
			ClaseJSONEscribir jsone;
			List<Estado> celdasValor;
			if (!acabado && fromJSON) {
				rl = new ResolverLaberinto(alturaLaberinto, anchuraLaberinto, estrategialab, maze);
				rl.inicializarValoresJSON(estadosJSON);
				valoresJSON = rl.valoresJSON;
				Estado estadoInicial = rl.obtenerEstadoInicialJSON();
				Estado objetivo = rl.obtenerEstadoObjetivoJSON();
				problema = new Problema(estadoInicial, objetivo, alturaLaberinto, anchuraLaberinto, estrategia, maze,
						valoresJSON);
				rl.busqueda(problema, 1000000, estrategialab);
				System.out.println("Estrategia utilizada: " + estrategialab);
				acabado = true;
			}
			if (!acabado && !fromJSON) {
				celdasValor = laberinto.celdasValor;
				jsone = new ClaseJSONEscribir(alturaLaberinto, anchuraLaberinto, laberinto.laberinto, celdasValor);
				FileHandle fh;
				fh = new FileHandle("puzzle_loop_" + alturaLaberinto + "x" + anchuraLaberinto + "_20.png");

				PixmapIO.writePNG(fh, pixmap);
				acabado = true;

				String laberintoJSON = jsone.toJSON().toString(4);
				String problemaJSON = jsone.toJSONProblema().toString(4);

				try {
					File f = new File("problema_" + alturaLaberinto + "x" + anchuraLaberinto + "_maze.json");
					File s = new File("problema_" + alturaLaberinto + "x" + anchuraLaberinto + ".json");
					FileOutputStream fos = new FileOutputStream(f);
					FileOutputStream fss = new FileOutputStream(s);

					if (!f.exists()) {
						f.createNewFile();
					}

					if (!s.exists()) {
						s.createNewFile();
					}

					fos.write(laberintoJSON.getBytes());
					fss.write(problemaJSON.getBytes());

					fos.flush();
					fss.flush();

					fos.close();
					fss.close();
				} catch (Exception e) {

				}
			}
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(tex, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	}

	public void dispose() {
		batch.dispose();
	}

	public void setFromJSON(boolean fromJSON) {
		this.fromJSON = fromJSON;
	}

	private Pixmap generatePixmap() {
		pixmap.setColor(Color.BLACK);
		pixmap.fill();

		pixmap.setColor(Color.WHITE);
		for (Celda celda : laberinto.laberinto) {
			drawCelda(celda);
		}
		pixmap.setColor(Color.BLUE);
		for (Celda celda : laberinto.camino) {
			drawCelda(celda);
		}

		return pixmap;
	}

	private void drawCelda(Celda celda) {
		int totalCeldaSize = celdaPadding + tamañoCelda + celdaPadding;

		int xAux = (celda.x * totalCeldaSize) + celdaPadding;
		int y = (celda.y * totalCeldaSize) + celdaPadding;

		int x = y;
		y = xAux;

		if (!celda.norte) {
			pixmap.fillRectangle(x, y - celdaPadding, tamañoCelda, celdaPadding);
		}
		if (!celda.sur) {
			pixmap.fillRectangle(x, y + tamañoCelda, tamañoCelda, celdaPadding);
		}
		if (!celda.este) {
			pixmap.fillRectangle(x + tamañoCelda, y, celdaPadding, tamañoCelda);
		}
		if (!celda.oeste) {
			pixmap.fillRectangle(x - celdaPadding, y, celdaPadding, tamañoCelda);
		}
	}

	public void setCeldasJSON(List<Celda> laberinto) {
		this.celdasJSON = laberinto;
	}

	public void setEstadosJson(List<Estado> estados) {
		this.estadosJSON = estados;
	}
}