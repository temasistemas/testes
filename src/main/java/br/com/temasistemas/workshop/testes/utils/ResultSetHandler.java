package br.com.temasistemas.workshop.testes.utils;

import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;

public class ResultSetHandler implements AutoCloseable {

	private final ResultSet delegate;

	public ResultSetHandler(final ResultSet resultSet) {
		super();
		this.delegate = resultSet;
	}

	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return this.delegate.unwrap(iface);
	}

	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return this.delegate.isWrapperFor(iface);
	}

	public boolean next() {
		try {
			return this.delegate.next();
		} catch (final SQLException e) {
			throw new GenericException("Erro ao verificar próximo registro.");
		}
	}

	@Override
	public void close() throws SQLException {
		this.delegate.close();
	}

	public boolean wasNull() throws SQLException {
		try {
			return this.delegate.wasNull();
		} catch (final SQLException e) {
			throw new GenericException("Erro ao verificar wasNull", e);
		}
	}

	public String getString(final String columnLabel) {
		try {
			return this.delegate.getString(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public boolean getBoolean(final String columnLabel) {
		try {
			return this.delegate.getBoolean(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public boolean getBooleanString(final String columnLabel) {
		try {
			final String valor = this.delegate.getString(columnLabel);
			return valor != null && "S".equalsIgnoreCase(valor);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public int getInt(final String columnLabel) {
		try {
			return this.delegate.getInt(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Integer getIntegerNull(final String columnLabel) {
		try {
			final int valor = this.delegate.getInt(columnLabel);
			if (this.wasNull()) {
				return null;
			}
			return valor;
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Double getDoubleNull(final String columnLabel) {
		try {
			final double valor = this.delegate.getDouble(columnLabel);
			if (this.wasNull()) {
				return null;
			}
			return valor;
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Long getLongNull(final String columnLabel) {
		try {
			final long valor = this.delegate.getLong(columnLabel);
			if (this.wasNull()) {
				return null;
			}
			return valor;
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public long getLong(final String columnLabel) {
		try {
			return this.delegate.getLong(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public float getFloat(final String columnLabel) {
		try {
			return this.delegate.getFloat(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public double getDouble(final String columnLabel) {
		try {
			return this.delegate.getDouble(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public byte[] getBytes(final String columnLabel) {
		try {
			return this.delegate.getBytes(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Date getDate(final String columnLabel) {
		try {
			return this.delegate.getDate(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Time getTime(final String columnLabel) {
		try {
			return this.delegate.getTime(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Timestamp getTimestamp(final String columnLabel) {
		try {
			return this.delegate.getTimestamp(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Object getObject(final String columnLabel) {
		try {
			return this.delegate.getObject(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public boolean columnExists(final String columnLabel) {
		if (columnLabel == null) {
			return false;
		}
		try {
			this.delegate.findColumn(columnLabel);
			return true;
		} catch (final SQLException e) {
			return false;
		}
	}

	public Reader getCharacterStream(final String columnLabel) {
		try {
			return this.delegate.getCharacterStream(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public BigDecimal getBigDecimal(final String columnLabel) {
		try {
			return this.delegate.getBigDecimal(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Object getObject(final String columnLabel, final Map<String, Class<?>> map) {
		try {
			return this.delegate.getObject(columnLabel, map);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Ref getRef(final String columnLabel) {
		try {
			return this.delegate.getRef(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Blob getBlob(final String columnLabel) {
		try {
			return this.delegate.getBlob(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Clob getClob(final String columnLabel) {
		try {
			return this.delegate.getClob(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Array getArray(final String columnLabel) {
		try {
			return this.delegate.getArray(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Date getDate(final String columnLabel, final Calendar cal) {
		try {
			return this.delegate.getDate(columnLabel, cal);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Time getTime(final String columnLabel, final Calendar cal) {
		try {
			return this.delegate.getTime(columnLabel, cal);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Timestamp getTimestamp(final String columnLabel, final Calendar cal) {
		try {
			return this.delegate.getTimestamp(columnLabel, cal);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public boolean isClosed() throws SQLException {
		return this.delegate.isClosed();
	}

	public NClob getNClob(final String columnLabel) {
		try {
			return this.delegate.getNClob(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public SQLXML getSQLXML(final String columnLabel) {
		try {
			return this.delegate.getSQLXML(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public String getNString(final String columnLabel) {
		try {
			return this.delegate.getNString(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public Reader getNCharacterStream(final String columnLabel) {
		try {
			return this.delegate.getNCharacterStream(columnLabel);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public <T> T getObject(final String columnLabel, final Class<T> type) {
		try {
			return this.delegate.getObject(columnLabel, type);
		} catch (final SQLException e) {
			throw this.tratarExcecaoColuna(columnLabel, e);
		}
	}

	public LocalDate getLocalDate(final String columnLabel) {
		final Date data = this.getDate(columnLabel);
		if (data == null) {
			return null;
		}
		return data.toLocalDate();
	}

	private GenericException tratarExcecaoColuna(final String columnLabel, final SQLException e) {
		return new GenericException("Erro ao recuperar dados da coluna [" + columnLabel + "]", e);
	}

	public LocalDateTime getLocalDateTime(final String columnLabel) {
		final Timestamp timestamp = this.getTimestamp(columnLabel);
		if (timestamp == null) {
			return null;
		}
		return timestamp.toLocalDateTime();
	}

}
