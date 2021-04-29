package edu.uclm.esi.carreful.model.interfaces;

public final class GastosDeEnvio {

	private GastosDeEnvio() {
		throw new IllegalStateException("Utility class");
	}

	protected static final double GASTOSDOMICILIO = 3.25;
	protected static final double GASTOSEXPRESS = 5.5;
	protected static final double GASTOSRECOGIDA = 0.0;
	
	public static double getGastosdomicilio() {
		return GASTOSDOMICILIO;
	}
	public static double getGastosexpress() {
		return GASTOSEXPRESS;
	}
	public static double getGastosrecogida() {
		return GASTOSRECOGIDA;
	}
}
