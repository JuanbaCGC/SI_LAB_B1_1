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
	WilsonsLaberinto laberinto;
	Texture tex;

	boolean acabado = false;
	Pixmap pixmap;
	int tamañoCelda = 10;
	int celdaPadding = 1;
	boolean fromJSON = false;

	float timer = 0.0f;
	List<Celda> celdasJSON;

	public void setAnchuraAltura(int altura, int anchura) {
		anchuraLaberinto = anchura;
		alturaLaberinto = altura;
	}

	public void create() {
		batch = new SpriteBatch();
		pixmap = new Pixmap(alturaLaberinto * (tamañoCelda)*2,
				anchuraLaberinto * (tamañoCelda)*2, Format.RGBA8888);
		laberinto = new WilsonsLaberinto(alturaLaberinto, anchuraLaberinto);
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
			if (!acabado && !fromJSON) {
				FileHandle fh;
				fh = new FileHandle("Lab_Filas_Columnas.png");

				PixmapIO.writePNG(fh, pixmap);
				acabado = true;

				String laberintoJSON = laberinto.toJSON().toString(4);
				String sucesoresJSON = laberinto.toJSONSucesores().toString(4);

				try {
					File f = new File("./Lab_Filas_Columnas.json");
					File s = new File("sucesores_"+alturaLaberinto+"X"+anchuraLaberinto+".json");
					FileOutputStream fos = new FileOutputStream(f);
					FileOutputStream fss = new FileOutputStream(s);

					if (!f.exists()) {
						f.createNewFile();
					}
					
					if (!s.exists()) {
						s.createNewFile();
					}

					fos.write(laberintoJSON.getBytes());
					fss.write(sucesoresJSON.getBytes());


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
		// batch.draw(tex, 0, -557, Gdx.graphics.getWidth() + 560
		// ,Gdx.graphics.getHeight() + 555 );
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
	        //pixmap.fillRectangle(x, y, tamañoCelda, tamañoCelda);
	        if (!celda.norte) {
	        	// 
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
}