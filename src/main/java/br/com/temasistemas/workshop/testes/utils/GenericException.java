package br.com.temasistemas.workshop.testes.utils;

public class GenericException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GenericException(final Throwable throwable) {
		super(throwable);
	}

	public GenericException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public GenericException(final String message) {
		super(message);
	}

}
