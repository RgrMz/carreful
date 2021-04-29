package edu.uclm.esi.carreful.exceptionhandling;

import org.springframework.http.HttpStatus;

public class CarrefulException extends Exception {
	private static final long serialVersionUID = 1202851932968744045L;
	private final HttpStatus status;
	private final String message;

	public CarrefulException (HttpStatus status, String mensaje) {
		this.status = status;
		this.message = mensaje;
	}

	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
