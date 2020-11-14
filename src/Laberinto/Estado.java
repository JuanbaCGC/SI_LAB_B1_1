package Laberinto;

public class Estado {
	private Id idEstado;
	private int valor;

	public Estado(Id idEstado, int valor) {
		this.idEstado = idEstado;
		this.valor = valor;
	}

	public Id getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Id idEstado) {
		this.idEstado = idEstado;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "(" + idEstado.toString() + ")";
	}

}
