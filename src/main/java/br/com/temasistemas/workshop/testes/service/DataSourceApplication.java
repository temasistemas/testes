package br.com.temasistemas.workshop.testes.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.h2.engine.Constants;
import org.h2.tools.RunScript;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import br.com.temasistemas.workshop.testes.components.alert.Alerts;
import br.com.temasistemas.workshop.testes.utils.BusinessException;
import br.com.temasistemas.workshop.testes.utils.UserPreferences;

public class DataSourceApplication {

	public static final String DEFAULT_URL_H2 = "jdbc:h2:file:./testesbd";

	private static HikariDataSource defaultDataSource;

	private static void executeSchemaSQLAsResource(final String schemaNameFile) throws SQLException {
		Enumeration<URL> urls = null;
		try {
			urls = DataSourceApplication.class.getClassLoader().getResources(schemaNameFile);
		} catch (final IOException e) {
			throw new BusinessException("Não foi possível ler o recurso de nome [" + schemaNameFile + "]");
		}
		try (Connection conn = defaultDataSource().getConnection()) {
			while (urls.hasMoreElements()) {
				executeSchema(conn, urls.nextElement());
			}
		}
	}

	private static void executeSchema(final Connection conn, final URL schema) {
		try (InputStream in = new BufferedInputStream(schema.openStream(), Constants.IO_BUFFER_SIZE)) {
			final Reader reader = new InputStreamReader(in);
			RunScript.execute(conn, reader);
		} catch (final Exception e) {
			throw new BusinessException("Nao foi possivel executar o script [" + schema + "].", e);
		}
	}

	private DataSourceApplication() {
		super();
	}

	public static DataSource defaultDataSource() {
		if (defaultDataSource == null) {
			try {
				criarDataSourceDefault();
			} catch (final Exception e) {
				Alerts.error(e);
			}
		}
		return defaultDataSource;
	}

	private static synchronized void criarDataSourceDefault() throws SQLException {
		if (defaultDataSource == null) {
			defaultDataSource = criarDataSourceSePossivel(
					UserPreferences.get(UserPreferences.JDBC_DATASOURCE, DEFAULT_URL_H2),
					UserPreferences.get(UserPreferences.USUARIO_DATASOURCE, "sa"),
					UserPreferences.get(UserPreferences.SENHA_DATASOURCE, ""));
			iniciarEstruturaECarga();
		}
	}

	private static void iniciarEstruturaECarga() throws SQLException {
		executeSchemaSQLAsResource("testes_schema.sql");
	}

	public static HikariDataSource criarDataSourceSePossivel(final String jdbc, final String usuario,
			final String senha) throws SQLException {
		if (StringUtils.isBlank(jdbc)) {
			throw new BusinessException("JDBC é requerido.");
		}
		if (StringUtils.isBlank(usuario)) {
			throw new BusinessException("Usuário é requerido.");
		}
		final HikariConfig config = new HikariConfig();
		config.setMaximumPoolSize(200);
		config.setJdbcUrl(jdbc);
		config.setUsername(usuario);
		config.setPassword(senha);
		final HikariDataSource dataSourceCriado = new HikariDataSource(config);
		try (Connection conn = dataSourceCriado.getConnection()) {
			conn.getMetaData().getDatabaseProductVersion();
		}
		return dataSourceCriado;
	}

	public static void close() {
		if (defaultDataSource != null && !defaultDataSource.isClosed()) {
			try {
				defaultDataSource.close();
			} catch (final Exception e) {
				// Close silent
			}
			defaultDataSource = null;
		}
	}

}
