package LaberintoGit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import org.json.JSONObject;

public class WilsonsLaberinto{
	List<Celda> noVisitadas = new ArrayList<>();
	List<Celda> laberinto = new ArrayList<>();
    Stack<Celda> camino = new Stack<>();
    
    int anchura, altura;
    
    public WilsonsLaberinto(int anchura, int altura) {
        this.anchura = anchura;
        this.altura = altura;
        Random aleatorio = new Random();
        
        // Crear el punto inicial del laberinto de forma aleatoria
        Celda inicial = new Celda(aleatorio.nextInt(anchura), aleatorio.nextInt(altura));
        laberinto.add(inicial);

        //Inicializar el "tablero"
        for(int x = 0;x<anchura;x++) {
            for(int y = 0;y<altura;y++) {
                noVisitadas.add(new Celda(x, y));
            }
        }

        //Quitar el punto inicial del laberinto de las no conocidas
        noVisitadas.remove(inicial);
    }

    public boolean estaCompleto() {
        return laberinto.size() == anchura*altura;
    }

    public void siguienteCelda() {
        //When empty, choose a new starting point to draw from that's not in the laberinto
        if(camino.isEmpty()) {
        	Random rand = new Random();
            Celda celdaComienzo = noVisitadas.remove(rand.nextInt(noVisitadas.size()));
            camino.add(celdaComienzo);
        } else {
            //Get the top of the stack
            Celda ultimaCelda = camino.peek();
            
            //Pick a random valid direction to travel
            int dir = -1;
            boolean valid = false;
            Celda siguienteCelda = null;
            do {
                dir = (int) (Math.random()*4);
            	
                if(dir == 0 && ultimaCelda.y != 0) { //North
                    siguienteCelda = new Celda(ultimaCelda.x, ultimaCelda.y-1);
                    if(!camino.contains(siguienteCelda)) { //The next celda is not in the camino
                        valid = true;
                        ultimaCelda.norte = true;
                        siguienteCelda.sur = true;
                        camino.push(siguienteCelda);
                    } else {
                        while(!camino.peek().equals(siguienteCelda)) {
                            arreglarCaminos();
                            camino.pop();
                        }
                        ultimaCelda = camino.peek();
                    }
                    
                } else if(dir == 2 && ultimaCelda.y != altura-1) { //South
                    siguienteCelda = new Celda(ultimaCelda.x, ultimaCelda.y+1);
                    if(!camino.contains(siguienteCelda)) {
                        valid = true;
                        ultimaCelda.sur = true;
                        siguienteCelda.norte = true;
                        camino.push(siguienteCelda);
                    } else {
                        while(!camino.peek().equals(siguienteCelda)) {
                            arreglarCaminos();
                            camino.pop();
                        }
                        ultimaCelda = camino.peek();
                    }
                } else if(dir == 3 && ultimaCelda.x != 0) { //West
                    siguienteCelda = new Celda(ultimaCelda.x-1, ultimaCelda.y);
                    if(!camino.contains(siguienteCelda)) {
                        valid = true;
                        ultimaCelda.oeste = true;
                        siguienteCelda.este = true;
                        camino.push(siguienteCelda);
                    } else {
                        while(!camino.peek().equals(siguienteCelda)) {
                            arreglarCaminos();
                            camino.pop();
                        }
                        ultimaCelda = camino.peek();
                    }
                } else if(dir == 1 && ultimaCelda.x != anchura-1) { //East
                    siguienteCelda = new Celda(ultimaCelda.x+1, ultimaCelda.y);
                    if(!camino.contains(siguienteCelda)) {
                        valid = true;
                        ultimaCelda.este = true;
                        siguienteCelda.oeste = true;
                        camino.push(siguienteCelda);
                    } else {
                        while(!camino.peek().equals(siguienteCelda)) {
                            arreglarCaminos();
                            camino.pop();
                        }
                        ultimaCelda = camino.peek();
                    }
                }
            } while(!valid);
            
            if(laberinto.contains(siguienteCelda)) {
                final Celda finalNextCelda = siguienteCelda;
                //Modify that celda in the laberinto so the connections are valid
                laberinto.stream()
                        .filter((c) -> c.equals(finalNextCelda))
                        .forEach((c) -> {
                            c.este = c.este || finalNextCelda.este;
                            c.oeste = c.oeste || finalNextCelda.oeste;
                            c.norte = c.norte || finalNextCelda.norte;
                            c.sur = c.sur || finalNextCelda.sur;
                        });
                camino.pop(); //Remove the last celda from the stack
                laberinto.addAll(camino); //Add all the entires in the camino to the laberinto
                noVisitadas.removeAll(camino);
                camino.clear();
            }            
        }
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
    
    public JSONObject toJSON(){
    	JSONObject laberintoJSON = new JSONObject();
    	laberintoJSON.put("cols", altura);
    	laberintoJSON.put("rows", anchura);

    	JSONObject jsonCeldas = new JSONObject();
    	for(Celda c : laberinto){
    		jsonCeldas.put("("+c.x+", "+c.y+")", c.getJSON());
    	}
    	
    	laberintoJSON.put("cells", jsonCeldas);  	
    	return laberintoJSON;
    }
}
