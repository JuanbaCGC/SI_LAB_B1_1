package Laberinto;

public class Nodo implements Comparable<Nodo> {
	private Estado estado;
	private Nodo padre;
	private int costo_acumulado;
	private int profundidad;
	private int id;
	private int heuristica;
	private String accion;
	private int valor;
	
	public Nodo(Estado estado, Nodo padre, int costo_acumulado, int profundidad, int id, int heuristica, String accion) {
		this.estado = estado;
		this.padre = padre;
		this.costo_acumulado = costo_acumulado;
		this.profundidad = profundidad;
		this.id = id;
		this.heuristica = heuristica;
		this.accion = accion;
		this.valor = heuristica + costo_acumulado;
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
	public int getCosto_acumulado() {
		return costo_acumulado;
	}
	public void setCosto_acumulado(int costo_acumulado) {
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
	public int getValor() {
		return valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
	
	@Override
	public String toString() {
		return "["+ id + "][" + estado + ", padre : " + padre + "," + costo_acumulado + "," + profundidad + "," + heuristica + "," + accion + "," + valor + "]";
	}

	public int compareTo(Nodo o) {
		int r = 0;
		if(o.getValor() < this.valor)
			r = + 1;
		else if (o.getValor() > this.valor)
			r = -1;
		else {
			r = 0;
			if(o.getEstado().getIdEstado().getFila() < this.estado.getIdEstado().getFila())
				r = +1;
			else if (o.getEstado().getIdEstado().getFila() > this.estado.getIdEstado().getFila())
				r = -1;
			else {
				r = 0;
				if(o.getEstado().getIdEstado().getColumna() < this.estado.getIdEstado().getColumna())
					r = +1;
				else if(o.getEstado().getIdEstado().getColumna() > this.estado.getIdEstado().getColumna())
					r = -1;
			}
		}
		return r;
	}
	
}