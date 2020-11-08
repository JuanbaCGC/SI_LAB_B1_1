package Laberinto;

public class Sucesor {
	public enum Movimiento{
		N,
		E,
		S,
		O
	}
	private Movimiento movimiento;
	private Celda estado;
	private int costo;
	
	public Sucesor(Movimiento movimiento, Celda estado, int costo){
		this.movimiento = movimiento;
		this.estado = estado;
		this.costo = costo;
	}
	
	public Movimiento getMovimiento() {
		return movimiento;
	}
	public void setMovimiento(Movimiento movimiento) {
		this.movimiento = movimiento;
	}
	public Celda getEstado() {
		return estado;
	}
	public void setEstado(Celda estado) {
		this.estado = estado;
	}
	public int getCosto() {
		return costo;
	}
	public void setCosto(int costo) {
		this.costo = costo;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("['");
		sb.append(movimiento);
		sb.append("',");
		sb.append(estado);
		sb.append(",");
		sb.append(costo);
		sb.append("]");
		return sb.toString();
	}
}