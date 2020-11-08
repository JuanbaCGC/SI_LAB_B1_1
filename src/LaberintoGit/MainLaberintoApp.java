package LaberintoGit;

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

public class MainLaberintoApp extends ApplicationAdapter{
	SpriteBatch batch;
    int anchuraLaberinto = 4;
    int alturaLaberinto = 4;
    WilsonsLaberinto laberinto;
    Texture tex;

    boolean acabado = false;
    Pixmap pixmap;
    int tamañoCelda = 2;
    int celdaPadding = 1;
    
    List<Celda> celdasJSON;

    public void setAnchuraAltura(int anchura, int altura){
    	anchuraLaberinto = anchura;
    	alturaLaberinto = altura;
    }

    public void create() {
        batch = new SpriteBatch();
        pixmap = new Pixmap(anchuraLaberinto*tamañoCelda*(celdaPadding*2), alturaLaberinto*tamañoCelda*(celdaPadding*2), Format.RGBA8888);
        laberinto = new WilsonsLaberinto(anchuraLaberinto, alturaLaberinto);
        if(celdasJSON != null){
        	laberinto.laberinto = celdasJSON;
        	tex = new Texture(generatePixmap());
        }
    }

    @Override
    public void render() {
        if (!laberinto.estaCompleto()) {
            laberinto.siguienteCelda();
            tex = new Texture(generatePixmap());
        } else {
        	if(!acabado){
	        	 FileHandle fh;
	             fh =  new FileHandle("Lab_Filas_Columnas.png");
	             PixmapIO.writePNG(fh, pixmap);
	             
	             acabado = true;
	             
	             String laberintoJSON = laberinto.toJSON().toString();
	             
	             try{             
		             File f = new File("./Lab_Filas_Columnas.json");
		             FileOutputStream fos = new FileOutputStream(f);
		             
		             if(!f.exists()){
		            	 f.createNewFile();
		             }
		             
		             fos.write(laberintoJSON.getBytes());
		             
		             fos.flush();
		             
		             fos.close();
	             }catch(Exception e){
	            	 
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

    private Pixmap generatePixmap() {
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        pixmap.setColor(Color.WHITE);
        for (Celda celda : laberinto.laberinto) {
            drawCelda(celda);
        }
        pixmap.setColor(Color.BLUE);
        for(Celda celda : laberinto.camino) {
            drawCelda(celda);
        }

        return pixmap;
    }

    private void drawCelda(Celda celda) {
        int totalCeldaSize = celdaPadding + tamañoCelda + celdaPadding;

        int x = (celda.x * totalCeldaSize) + celdaPadding;
        int y = (celda.y * totalCeldaSize) + celdaPadding;
        pixmap.fillRectangle(x, y, tamañoCelda, tamañoCelda);
        if (celda.norte) {
            pixmap.fillRectangle(x, y - celdaPadding, tamañoCelda, celdaPadding);
        }
        if (celda.sur) {
            pixmap.fillRectangle(x, y + tamañoCelda, tamañoCelda, celdaPadding);
        }
        if (celda.este) {
            pixmap.fillRectangle(x + tamañoCelda, y, celdaPadding, tamañoCelda);
        }
        if (celda.oeste) {
            pixmap.fillRectangle(x - celdaPadding, y, celdaPadding, tamañoCelda);
        }
    }
    
    public void setCeldasJSON(List<Celda> laberinto){
    	this.celdasJSON = laberinto;
    }
}
