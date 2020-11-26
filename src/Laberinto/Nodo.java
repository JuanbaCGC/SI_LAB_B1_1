package Laberinto;

public class Nodo implements Comparable<Nodo> {
	Estado estado;
	Nodo padre;
	double costo_acumulado;
	int profundidad;
	int id;
	int heuristica;
	String accion;
	double valor;

	public Nodo(Estado estado, Nodo padre, double costo_acumulado, int profundidad, int id, int heuristica,
			String accion, double valor) {
		this.estado = estado;
		this.padre = padre;
		this.costo_acumulado = costo_acumulado;
		this.profundidad = profundidad;
		this.id = id;
		this.heuristica = heuristica;
		this.accion = accion;
		this.valor = valor;
	}

	public Nodo(Estado estado, Nodo padre, double costo_acumulado, int profundidad, int id, int heuristica,
			String accion) {
		this.estado = estado;
		this.padre = padre;
		this.costo_acumulado = costo_acumulado;
		this.profundidad = profundidad;
		this.id = id;
		this.heuristica = heuristica;
		this.accion = accion;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Nodo getPadre() {
		return padre;
	}

	public void setPadre(Nodo padre) {
		this.padre = padre;
	}

	public double getCosto_acumulado() {
		return costo_acumulado;
	}

	public void setCosto_acumulado(double costo_acumulado) {
		this.costo_acumulado = costo_acumulado;
	}

	public int getProfundidad() {
		return profundidad;
	}

	public void setProfundidad(int profundidad) {
		this.profundidad = profundidad;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHeuristica() {
		return heuristica;
	}

	public void setHeuristica(int heuristica) {
		this.heuristica = heuristica;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "[" + id + "][Estado: " + estado + ", padre : " + padre + ", costo: " + costo_acumulado
				+ ", profundidad: " + profundidad + ", heurística: " + heuristica + ", acción: " + accion + ", f: "
				+ valor + "]";
	}

	public int compareTo(Nodo o) {
		int r = 0;

		if (o.getValor() < this.valor)
			r = +1;
		else if (o.getValor() > this.valor)
			r = -1;
		else {
			r = 0;

			if (o.getEstado().getIdEstado().getFila() < this.estado.getIdEstado().getFila())
				r = +1;
			else if (o.getEstado().getIdEstado().getFila() > this.estado.getIdEstado().getFila())
				r = -1;
			else {
				r = 0;

				if (o.getEstado().getIdEstado().getColumna() < this.estado.getIdEstado().getColumna())
					r = +1;
				else if (o.getEstado().getIdEstado().getColumna() > this.estado.getIdEstado().getColumna())
					r = -1;
				else {
					r = 0;

					if (o.getId() < this.id) {
						r = +1;
					} else if (o.getId() > this.id) {
						r = -1;
					}
				}
			}
		}
		return r;
	}
}