package br.com.temasistemas.workshop.testes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javax.persistence.EntityManager;

import org.hibernate.internal.SessionImpl;

import br.com.temasistemas.workshop.testes.service.DataSourceApplication;
import br.com.temasistemas.workshop.testes.utils.GenericException;
import br.com.temasistemas.workshop.testes.utils.ParameterSet;
import br.com.temasistemas.workshop.testes.utils.Query;
import br.com.temasistemas.workshop.testes.utils.ResultSetHandler;

public abstract class GenericDao {

	protected <T> List<T> getList(final Query query, final Function<ResultSetHandler, T> converter,
			final UnaryOperator<ParameterSet> parametros) {
		return this.get(query, rs -> {
			final List<T> dados = new ArrayList<>();
			while (rs.next()) {
				final T retorno = converter.apply(rs);
				if (retorno != null) {
					dados.add(retorno);
				}
			}
			return dados;
		}, parametros);
	}

	protected <T> T get(final Query query, final Function<ResultSetHandler, T> converter,
			final UnaryOperator<ParameterSet> parametros) {
		try (Connection conn = DataSourceApplication.defaultDataSource().getConnection()) {
			try (PreparedStatement preparedStatement = conn.prepareStatement(query.getSqlParse())) {
				parametros.apply(new ParameterSet(preparedStatement, query.getIndexParameter()));
				try (ResultSetHandler resultSet = new ResultSetHandler(preparedStatement.executeQuery())) {
					return converter.apply(resultSet);
				}
			}
		} catch (final SQLException e) {
			throw new GenericException("Problemas para executar a consulta", e);
		}
	}

	public <T> void salvar(final T entidade) {
		this.iniciarTransacao();
		try {
			this.getEm().persist(entidade);
			this.getEm().flush();
			this.voltarTransacao();
		} catch (final Exception e) {
			this.rollback();
			this.voltarTransacao();
			throw new GenericException(e);
		}
	}

	private void rollback() {
		try {
			((SessionImpl) this.getEm().getDelegate()).getJdbcCoordinator().getLogicalConnection()
					.getPhysicalConnection().rollback();
		} catch (final SQLException e) {
			throw new GenericException(e);
		}
	}

	private void voltarTransacao() {
		try {
			((SessionImpl) this.getEm().getDelegate()).getJdbcCoordinator().getLogicalConnection()
					.getPhysicalConnection().setAutoCommit(true);
		} catch (final SQLException e) {
			throw new GenericException(e);
		}
	}

	private void iniciarTransacao() {
		try {
			((SessionImpl) this.getEm().getDelegate()).getJdbcCoordinator().getLogicalConnection()
					.getPhysicalConnection().setAutoCommit(false);
		} catch (final SQLException e) {
			throw new GenericException(e);
		}
	}

	protected abstract EntityManager getEm();

	public <T> void delete(final T entidade) {
		this.iniciarTransacao();
		try {
			this.getEm().remove(entidade);
			this.voltarTransacao();
		} catch (final Exception e) {
			this.rollback();
			this.voltarTransacao();
			throw new GenericException(e);
		}
	}

}
