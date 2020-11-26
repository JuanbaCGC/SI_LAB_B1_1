package Laberinto;

public class Visitados {

	Estado[] espacioEstados;

	public Visitados(Estado[] espacioEstados) {
		this.espacioEstados = espacioEstados;
	}

	public Estado crear_vacio() {
		Estado estado = null;
		return estado;
	}

	public boolean pertenece(Estado e) {
		boolean enc = false;
		for (int i = 0; i < espacioEstados.length; i++) {
			if (espacioEstados[i].getIdEstado().getFila() == e.getIdEstado().getFila()
					&& espacioEstados[i].getIdEstado().getColumna() == e.getIdEstado().getColumna()) {
				enc = true;
			}

		}
		return enc;
	}

	public void insertarEstado(Estado e) {
		Estado[] auxiliar = new Estado[espacioEstados.length];
		for (int i = 0; i < espacioEstados.length; i++) {
			auxiliar[i] = espacioEstados[i];
		}
		espacioEstados = new Estado[espacioEstados.length + 1];
		for (int i = 0; i < auxiliar.length; i++) {
			espacioEstados[i] = auxiliar[i];
		}
		espacioEstados[espacioEstados.length - 1] = e;

	}

}
