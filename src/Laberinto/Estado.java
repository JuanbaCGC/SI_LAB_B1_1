package Laberinto;

import org.json.JSONObject;

public class Estado {
	Id idEstado;
	double valor;

	public Estado(Id idEstado, double valor) {
		this.idEstado = idEstado;
		this.valor = valor;
	}

	public Id getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Id idEstado) {
		this.idEstado = idEstado;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "(" + idEstado.toString() + ")";
	}

	public JSONObject getJSON() {
		JSONObject jsonValor = new JSONObject();
		jsonValor.put("value", valor);
		return jsonValor;
	}
}
