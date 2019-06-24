package br.com.temasistemas.workshop.testes.mocks;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.persistence.EntityManager;

import org.dbunit.IDatabaseTester;
import org.dbunit.IOperationListener;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.h2.engine.Constants;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.RunScript;

import br.com.temasistemas.workshop.testes.persistence.Entidade;
import br.com.temasistemas.workshop.testes.persistence.PersistenceApplication;
import br.com.temasistemas.workshop.testes.service.DataSourceApplication;
import br.com.temasistemas.workshop.testes.transaction.TransactionManagerJavaFxFactory;
import br.com.temasistemas.workshop.testes.utils.ConsumerSQLException;
import br.com.temasistemas.workshop.testes.utils.GenericException;
import br.com.temasistemas.workshop.testes.utils.ResultSetHandler;

public class MocksForGenericDao {

	private static final String JDBC_DRIVER = org.h2.Driver.class.getName();

	private static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

	private static final String USER = "sa";

	private static final String PASSWORD = "";

	private static final IOperationListener OPERATION_LISTNER = new IOperationListener() {

		@Override
		public void operationTearDownFinished(final IDatabaseConnection connection) {
			// Nao fazer nada
		}

		@Override
		public void operationSetUpFinished(final IDatabaseConnection connection) {
			// Nao fazer nada
		}

		@Override
		public void connectionRetrieved(final IDatabaseConnection connection) {
			connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
		}
	};

	private static MocksForGenericDao instance;

	private final List<URL> schemasSQL;

	private final List<URL> dataSetsSQL;

	private DataSourceMock dataSource;

	private final MocksForServiceInjectorFactory mocksForServiceInjectorFactory;

	private PersistenceApplication persistenceApplication;

	private EntityManager entityManager;

	private MocksForGenericDao() {
		super();
		this.schemasSQL = new ArrayList<>();
		this.dataSetsSQL = new ArrayList<>();
		this.addSchemaSQLAsResource("backendlibs_schema.sql").addDataSetAsResource("backendlibs_dataset.xml")
				.executeAll();
		this.mocksForServiceInjectorFactory = MocksForServiceInjectorFactory.makeMock();
		this.criarPersister();
	}

	private void criarPersister() {
		mockStatic(DataSourceApplication.class);
		mockStatic(TransactionManagerJavaFxFactory.class);
		when(DataSourceApplication.defaultDataSource()).thenReturn(this.dataSource);
		when(TransactionManagerJavaFxFactory.instance()).thenReturn(new TransactionManagerJavaFxMock());
		this.persistenceApplication = new PersistenceApplication();
		this.persistenceApplication.forceShowSQl();
		this.mocksForServiceInjectorFactory.injectMock(PersistenceApplication.class, this.persistenceApplication);
	}

	public static MocksForGenericDao create() {
		instance = new MocksForGenericDao();
		return instance;
	}

	public static MocksForGenericDao instance() {
		if (instance == null) {
			create();
		}
		return instance;
	}

	public MocksForGenericDao addSchemaSQLAsResource(final String schemaNameFile) {
		Enumeration<URL> urls = null;
		try {
			urls = this.getClass().getClassLoader().getResources(schemaNameFile);
		} catch (final IOException e) {
			throw new GenericException("Não foi possível ler o recurso de nome [" + schemaNameFile + "]");
		}
		while (urls.hasMoreElements()) {
			this.schemasSQL.add(urls.nextElement());
		}
		return this;
	}

	public MocksForGenericDao addDataSetAsResource(final String dataSetNameFile) {
		Enumeration<URL> urls = null;
		try {
			urls = this.getClass().getClassLoader().getResources(dataSetNameFile);
		} catch (final IOException e) {
			throw new GenericException("Não foi possível ler o recurso de nome [" + dataSetNameFile + "]");
		}
		while (urls.hasMoreElements()) {
			this.dataSetsSQL.add(urls.nextElement());
		}
		return this;
	}

	public MocksForGenericDao executeSchemas() {
		try (Connection conn = this.getConnection()) {
			final Iterator<URL> itSchemasSQL = this.schemasSQL.iterator();
			while (itSchemasSQL.hasNext()) {
				final URL schema = itSchemasSQL.next();
				itSchemasSQL.remove();
				this.executeSchema(conn, schema);
			}
		} catch (final SQLException eSql) {
			throw new GenericException("Não foi possível abrir conexão", eSql);
		}
		return this;
	}

	private void executeSchema(final Connection conn, final URL schema) {
		try (InputStream in = new BufferedInputStream(schema.openStream(), Constants.IO_BUFFER_SIZE)) {
			final Reader reader = new InputStreamReader(in, StandardCharsets.ISO_8859_1);
			RunScript.execute(conn, reader);
		} catch (final Exception e) {
			throw new GenericException("Nao foi possivel executar o script [" + schema + "].", e);
		}
	}

	public void executeDataSets() {
		IDatabaseTester databaseTester;
		try {
			databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, JDBC_URL, USER, PASSWORD);
		} catch (final ClassNotFoundException e) {
			throw new GenericException("Nao foi possivel criar a conexão com o banco [" + JDBC_URL + "].", e);
		}
		final FlatXmlDataSetBuilder dataSetBuilder = new FlatXmlDataSetBuilder();
		final Iterator<URL> itDataSet = this.dataSetsSQL.iterator();
		while (itDataSet.hasNext()) {
			final URL dataSet = itDataSet.next();
			itDataSet.remove();
			databaseTester.setOperationListener(OPERATION_LISTNER);
			databaseTester.setSetUpOperation(DatabaseOperation.REFRESH);
			try {
				databaseTester.setDataSet(dataSetBuilder.build(dataSet));
				databaseTester.onSetup();
			} catch (final Exception e) {
				throw new GenericException("Problemas para executar o dataSet [" + dataSet.getFile() + "].", e);
			}
		}
	}

	public <T> T get(final String sql, final Function<ResultSetHandler, T> converter,
			final ConsumerSQLException<PreparedStatement> parametros, final Optional<Integer> maxResult) {
		try (Connection conn = this.getConnection()) {
			return this.get(sql, converter, parametros, maxResult, conn);
		} catch (final SQLException e) {
			throw new GenericException("Problemas para abrir conexão", e);
		}
	}

	public Connection getConnection() {
		try {
			return this.dataSource().getConnection();
		} catch (final SQLException e) {
			throw new GenericException("Não foi possível abrir conexão", e);
		}
	}

	private <T> T get(final String sql, final Function<ResultSetHandler, T> converter,
			final ConsumerSQLException<PreparedStatement> parametros, final Optional<Integer> maxResult,
			final Connection conn) {
		try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
			if (maxResult.isPresent()) {
				preparedStatement.setMaxRows(maxResult.get());
			}
			parametros.acceptThrowsSQLException(preparedStatement);
			try (ResultSetHandler resultSet = new ResultSetHandler(preparedStatement.executeQuery())) {
				return converter.apply(resultSet);
			}
		} catch (final SQLException e) {
			throw new GenericException("Problemas para executar a consulta", e);
		}
	}

	private DataSourceMock dataSource() {
		if (this.dataSource == null) {
			final JdbcDataSource dataSourceCriado = new JdbcDataSource();
			dataSourceCriado.setURL(JDBC_URL);
			dataSourceCriado.setUser(USER);
			dataSourceCriado.setPassword(PASSWORD);
			this.dataSource = new DataSourceMock(dataSourceCriado);
		}
		return this.dataSource;
	}

	public MocksForGenericDao executeAll() {
		this.executeSchemas();
		this.executeDataSets();
		return this;
	}

	public MocksForServiceInjectorFactory getMocksForServiceInjectorFactory() {
		return this.mocksForServiceInjectorFactory;
	}

	public void criarContextoPersistencia() {
		this.entityManager = this.persistenceApplication.createEntityManager();
	}

	public void finalizarContextoPersistencia() {
		if (this.entityManager != null) {
			this.persistenceApplication.finaliza(this.entityManager);
			this.entityManager = null;
		}
	}

	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	public MocksForGenericDao adicionarClasseEntidade(final Class<? extends Entidade> classe) {
		this.persistenceApplication.adicionarClasses(classe);
		return this;
	}

}
