package br.com.temasistemas.workshop.testes.utils;

import java.sql.SQLException;

@FunctionalInterface
public interface ConsumerSQLException<T> {

	void acceptThrowsSQLException(T t) throws SQLException;

}
