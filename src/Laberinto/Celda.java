package Laberinto;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Celda {
	int x;
	int y;
	int valor;

	boolean norte, sur, oeste, este;

	public Celda(int x, int y) {
		this.x = x;
		this.y = y;
		norte = false;
		sur = false;
		oeste = false;
		este = false;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public boolean getNorte() {
		return norte;
	}

	public void setNorte(boolean norte) {
		this.norte = norte;
	}

	public boolean getSur() {
		return sur;
	}

	public void setSur(boolean sur) {
		this.sur = sur;
	}

	public boolean getOeste() {
		return oeste;
	}

	public void setOeste(boolean oeste) {
		this.oeste = oeste;
	}

	public boolean getEste() {
		return este;
	}

	public void setEste(boolean este) {
		this.este = este;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + this.x;
		hash = 53 * hash + this.y;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Celda otra = (Celda) obj;
		if (this.x != otra.x) {
			return false;
		}
		if (this.y != otra.y) {
			return false;
		}
		return true;
	}

	public JSONObject getJSON() {
		JSONObject json = new JSONObject();
		List<Boolean> neighbours = new ArrayList<>();
		neighbours.add(norte);
		neighbours.add(este);
		neighbours.add(sur);
		neighbours.add(oeste);

		json.put("neighbors", neighbours);

		return json;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(" + x);
		sb.append("," + y);
		sb.append(")");
		return sb.toString();
	}

	public String getAccion(Celda padre, Celda actual) {
		String mov = "";
		if (padre.x < actual.x && padre.y == actual.y)
			mov = "S";
		else if (padre.x == actual.x && padre.y < actual.y)
			mov = "E";
		else if (padre.x > actual.x && padre.y == actual.y)
			mov = "N";
		else if (padre.x == actual.x && padre.y > actual.y)
			mov = "O";

		return mov;
	}

}