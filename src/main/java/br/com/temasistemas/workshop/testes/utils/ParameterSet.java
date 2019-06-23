package br.com.temasistemas.workshop.testes.utils;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public class ParameterSet {

	private final PreparedStatement st;

	private final Map<String, Integer> parametros;

	public ParameterSet(final PreparedStatement st, final Map<String, Integer> parametros) {
		super();
		this.st = st;
		this.parametros = parametros;
	}

	public ParameterSet setString(final String name, final String value) {
		return this.setNull(indice -> this.st.setString(indice, value), name, value, Types.VARCHAR);
	}

	public ParameterSet setLocalDate(final String name, final LocalDate value) {
		return this.setNull(indice -> this.st.setDate(indice, Date.valueOf(value)), name, value, Types.DATE);
	}

	public ParameterSet setLocalDateTime(final String name, final LocalDateTime value) {
		return this.setNull(indice -> this.st.setTimestamp(indice, Timestamp.valueOf(value)), name, value,
				Types.TIMESTAMP);
	}

	public ParameterSet setIntPrimitive(final String name, final int value) {
		return this.set(indice -> this.st.setInt(indice, value), name);
	}

	public ParameterSet setBooleanToString(final String name, final Boolean value) {
		return this.set(indice -> this.st.setString(indice, value != null && value.booleanValue() ? "S" : "N"), name);
	}

	public ParameterSet setLongPrimitive(final String name, final long value) {
		return this.set(indice -> this.st.setLong(indice, value), name);
	}

	public ParameterSet setDoublePrimitive(final String name, final double value) {
		return this.set(indice -> this.st.setDouble(indice, value), name);
	}

	protected ParameterSet set(final ConsumerSQLException<Integer> parameterSetFunctional, final String name) {
		try {
			parameterSetFunctional.acceptThrowsSQLException(
					Optional.ofNullable(this.parametros.get(name)).orElseThrow(() -> new GenericException(name)));
		} catch (final SQLException e) {
			this.tratarExecao(name);
		}
		return this;
	}

	protected <V> ParameterSet setNull(final ConsumerSQLException<Integer> parameterSetFunctional, final String name,
			final V valor, final int sqlType) {
		try {
			final int indice = Optional.ofNullable(this.parametros.get(name))
					.orElseThrow(() -> new GenericException(name));
			if (valor == null) {
				this.st.setNull(indice, sqlType);
			} else {
				parameterSetFunctional.acceptThrowsSQLException(indice);
			}
		} catch (final SQLException e) {
			this.tratarExecao(name);
		}
		return this;
	}

	private void tratarExecao(final String name) {
		throw new GenericException("Erro ao atribuir o parâmetro [" + name + "]");
	}

	public void addBatch() throws SQLException {
		this.st.addBatch();
	}

	public boolean parametroExistente(final String parametro) {
		return this.parametros.containsKey(parametro);
	}

	public void set(final String name, final Object value, final Class<?> tipo) {
		if (tipo.isAssignableFrom(LocalDate.class)) {
			this.setLocalDate(name, (LocalDate) value);
		} else if (tipo.isAssignableFrom(LocalDateTime.class)) {
			this.setLocalDateTime(name, (LocalDateTime) value);
		} else if (tipo.isAssignableFrom(Boolean.class)) {
			this.setBooleanToString(name, (Boolean) value);
		} else if (tipo.isAssignableFrom(String.class)) {
			this.setString(name, (String) value);
		} else if (value == null) {
			this.setNullPrimitiveValues(name, tipo);
		} else if (tipo.isAssignableFrom(Integer.class)) {
			this.setIntPrimitive(name, (Integer) value);
		} else if (tipo.isAssignableFrom(Long.class)) {
			this.setLongPrimitive(name, (Long) value);
		} else if (tipo.isAssignableFrom(Double.class)) {
			this.setDoublePrimitive(name, (Double) value);
		} else {
			throw new IllegalArgumentException("O tipo informado não pode ser atribuido [" + tipo.getName() + "]");
		}
	}

	private void setNullPrimitiveValues(final String name, final Class<?> tipo) {
		if (tipo.isAssignableFrom(Integer.class)) {
			this.setNull(indice -> this.st.setInt(indice, 0), name, null, Types.INTEGER);
		} else if (tipo.isAssignableFrom(Long.class)) {
			this.setNull(indice -> this.st.setLong(indice, 0), name, null, Types.INTEGER);
		} else if (tipo.isAssignableFrom(Double.class)) {
			this.setNull(indice -> this.st.setDouble(indice, 0), name, null, Types.DOUBLE);
		}
	}

}
