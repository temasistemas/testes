package br.com.temasistemas.workshop.testes.mocks;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

public class DataSourceMock implements DataSource {

	private final JdbcDataSource delegate;
	private ConnectionMock connectionMock;

	public DataSourceMock(final JdbcDataSource ds) {
		this.delegate = ds;
	}

	@Override
	public int hashCode() {
		return this.delegate.hashCode();
	}

	@Override
	public int getLoginTimeout() {
		return this.delegate.getLoginTimeout();
	}

	@Override
	public boolean equals(final Object obj) {
		return this.delegate.equals(obj);
	}

	@Override
	public void setLoginTimeout(final int timeout) {
		this.delegate.setLoginTimeout(timeout);
	}

	@Override
	public PrintWriter getLogWriter() {
		return this.delegate.getLogWriter();
	}

	@Override
	public void setLogWriter(final PrintWriter out) {
		this.delegate.setLogWriter(out);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return this.connectionMock();
	}

	@Override
	public Connection getConnection(final String user, final String password) throws SQLException {
		return this.connectionMock();
	}

	private Connection connectionMock() throws SQLException {
		if (this.connectionMock == null) {
			this.connectionMock = new ConnectionMock(this.delegate.getConnection());
		}
		return this.connectionMock;
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return this.delegate.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return this.delegate.isWrapperFor(iface);
	}

	@Override
	public Logger getParentLogger() {
		return this.delegate.getParentLogger();
	}

	public void fecharConexao() {
		if (this.connectionMock != null) {
			try {
				this.connectionMock.fecharMock();
			} catch (final SQLException e) {
				// Sem erros
			}
		}
	}

}
