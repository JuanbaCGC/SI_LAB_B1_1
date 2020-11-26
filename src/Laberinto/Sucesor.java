package Laberinto;

public class Sucesor {

	private String accion;
	private Estado estado;
	private int costo;

	public Sucesor(String accion, Estado estado, int costo) {
		this.accion = accion;
		this.estado = estado;
		this.costo = costo;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String movimiento) {
		this.accion = movimiento;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public int getCosto() {
		return costo;
	}

	public void setCosto(int costo) {
		this.costo = costo;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("['");
		sb.append(accion);
		sb.append("',");
		sb.append(estado);
		sb.append(",");
		sb.append(costo);
		sb.append("]");
		return sb.toString();
	}
}