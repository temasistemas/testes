package br.com.temasistemas.workshop.testes.utils;

import java.text.MessageFormat;

public class Objects {

	public static final <R> R requireNonNull(R obj, String objectName) {
		return requireNonNull(obj, "'{0}' não pode ser nulo", objectName);
	}

	public static final <R> R requireNonNull(R obj, String msgTemplate, Object... params) {
		return java.util.Objects.requireNonNull(obj, () -> MessageFormat.format(msgTemplate, params));
	}

}
