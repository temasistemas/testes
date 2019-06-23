package br.com.temasistemas.workshop.testes.utils;

import java.util.Optional;

public class Exceptions {

	private Exceptions() {
		super();
	}

	public static Optional<Exception> extractFrom(final Throwable t) {
		if (t == null) {
			return Optional.empty();
		}

		if (t instanceof Exception) {
			return Optional.of((Exception) t);
		} else {
			return extractFrom(t.getCause());
		}
	}
}
