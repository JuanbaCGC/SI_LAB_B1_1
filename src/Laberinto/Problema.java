package Laberinto;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class Problema {
	
	Estado estadoInicial;
	Estado objetivo;
	int altura;
	int anchura;
	String estrategia;
	List <Celda> laberinto;
	List<Estado> celdasValor;
	
	public Problema(Estado estadoInicial, Estado objetivo, int alt, int anch, String estr,List <Celda> laberinto,List<Estado> celdasValor) {
		this.estadoInicial=estadoInicial;
		this.objetivo= objetivo;
		this.altura = alt;
		this.anchura = anch;
		this.estrategia = estr;
		this.laberinto = laberinto;
		this.celdasValor = celdasValor;
		
	}

	public Estado getEstadoInicial() {
		return estadoInicial;
	}

	public void setEstadoInicial(Estado estadoInicial) {
		this.estadoInicial = estadoInicial;
	}

	public Estado getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(Estado objetivo) {
		this.objetivo = objetivo;
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
	
	public double getValor(Id id) {
		double valor = 0;
		int x = id.getFila();
		int y = id.getColumna();
		for(int i = 0;i<celdasValor.size();i++) {
			if(celdasValor.get(i).getIdEstado().getFila() == x && celdasValor.get(i).getIdEstado().getColumna() == y) {
				valor = celdasValor.get(i).getValor();
			}
			
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
	
	public List<Sucesor> getSucesores(Estado estado) {
		List<Sucesor> sucesoresX = new LinkedList<>();
		Sucesor s = null;
		double valor = 0;
		int x = estado.getIdEstado().getFila();
		int y = estado.getIdEstado().getColumna();
		Celda c = new Celda(x,y);
		Celda celda = null;
		celda = comprobar(c);
		Id id = null;
		Estado e = null;

		Celda norte = null;
		Celda este = null;
		Celda sur = null;
		Celda oeste = null;

		if (celda.norte) {
			norte = getCelda(x - 1, y);
			id = new Id(norte.x, norte.y);
			valor = getValor(id);
			e = new Estado(id,valor);
			s = new Sucesor("N", e, 1);
			sucesoresX.add(s);
		}
		if (celda.este) {
			este = getCelda(x, y + 1);
			id = new Id(este.x, este.y);
			valor = getValor(id);
			e = new Estado(id,valor);
			s = new Sucesor("E", e, 1);
			sucesoresX.add(s);
		}
		if (celda.sur) {
			sur = getCelda(x + 1, y);
			id = new Id(sur.x, sur.y);
			valor = getValor(id);
			e = new Estado(id,valor);
			s = new Sucesor("S", e, 1);
			sucesoresX.add(s);
		}
		if (celda.oeste) {
			oeste = getCelda(x, y - 1);
			id = new Id(oeste.x, oeste.y);
			valor = getValor(id);
			e = new Estado(id,valor);
			s = new Sucesor("O", e, 1);
			sucesoresX.add(s);
		}

		return sucesoresX;
	}
	
}
