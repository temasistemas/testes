package br.com.temasistemas.workshop.testes.persistence;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Index;
import org.jboss.jandex.IndexReader;

import br.com.temasistemas.workshop.testes.service.DataSourceApplication;
import br.com.temasistemas.workshop.testes.utils.GenericException;

@ApplicationScoped
public class PersistenceApplication {

	private final Set<String> classesEntity = new HashSet<>();

	private EntityManagerFactory entityManagerFactory;

	private EntityManagerFactory getEntityManagerFactory() {
		if (this.entityManagerFactory == null) {
			synchronized (PersistenceApplication.class) {
				if (this.entityManagerFactory == null) {
					this.createEntityManagerFactory();
				}
			}
		}
		return this.entityManagerFactory;
	}

	private void createEntityManagerFactory() {
		final EntityManagerFactory emf = new HibernatePersistenceProvider()
				.createContainerEntityManagerFactory(this.createPersistenceUnitInfo(), this.criarPropriedades());
		this.entityManagerFactory = emf;
	}

	private Map<String, Object> criarPropriedades() {
		final Map<String, Object> propriedades = new HashMap<>();
		propriedades.put("javax.persistence.validation.mode", "none");
		propriedades.put("hibernate.bytecode.use_reflection_optimizer", "true");
		propriedades.put("hibernate.bytecode.provider", "javassist");
		propriedades.put("hibernate.order_updates", "true");
		propriedades.put("hibernate.max_fetch_depth", "4");
		propriedades.put("hibernate.jdbc.batch_size", "100");
		propriedades.put("hibernate.connection.handling_mode", "DELAYED_ACQUISITION_AND_RELEASE_AFTER_STATEMENT");
		propriedades.put("hibernate.jdbc.use_get_generated_keys", "true");
		propriedades.put("hibernate.jdbc.batch_versioned_data", "true");
		propriedades.put("hibernate.id.new_generator_mappings", "true");
		propriedades.put("hibernate.show_sql", "false");
		propriedades.put("hibernate.format_sql", "false");
		propriedades.put("hibernate.transaction.flush_before_completion", "true");
		propriedades.put("hibernate.transaction.jta.platform_resolver",
				"br.com.temasistemas.workshop.testes.transaction.CustomJTAPlataformResolver");
		return propriedades;
	}

	private PersistenceUnitInfo createPersistenceUnitInfo() {
		return new PersistenceUnitInfo() {
			@Override
			public String getPersistenceUnitName() {
				return "agenda-pu";
			}

			@Override
			public String getPersistenceProviderClassName() {
				return "org.hibernate.jpa.HibernatePersistenceProvider";
			}

			@Override
			public PersistenceUnitTransactionType getTransactionType() {
				return PersistenceUnitTransactionType.JTA;
			}

			@Override
			public DataSource getJtaDataSource() {
				return PersistenceApplication.this.getDataSource();
			}

			@Override
			public DataSource getNonJtaDataSource() {
				return null;
			}

			@Override
			public List<String> getMappingFileNames() {
				return new ArrayList<>();
			}

			@Override
			public List<URL> getJarFileUrls() {
				return Collections.emptyList();
			}

			@Override
			public URL getPersistenceUnitRootUrl() {
				return null;
			}

			@Override
			public List<String> getManagedClassNames() {
				return this.obterClassesEntity();
			}

			private List<String> obterClassesEntity() {
				final List<String> nomeClasses = new ArrayList<>();
				try {
					final DotName entity = DotName.createSimple(Entidade.class.getName());
					final Enumeration<URL> enumerations = this.getClass().getClassLoader()
							.getResources("META-INF/jandex.idx");
					while (enumerations.hasMoreElements()) {
						final URL url = enumerations.nextElement();
						try (final InputStream in = url.openStream()) {
							final IndexReader indexerReader = new IndexReader(in);
							final Index index = indexerReader.read();
							nomeClasses.addAll(index.getAllKnownImplementors(entity).stream()
									.map(classe -> classe.name().toString()).collect(Collectors.toList()));
						}
					}
					nomeClasses.addAll(PersistenceApplication.this.classesEntity);
					return nomeClasses;
				} catch (final Exception e) {
					throw new GenericException(e);
				}
			}

			@Override
			public boolean excludeUnlistedClasses() {
				return false;
			}

			@Override
			public Properties getProperties() {
				return new Properties();
			}

			@Override
			public ClassLoader getClassLoader() {
				return this.getClass().getClassLoader();
			}

			@Override
			public void addTransformer(final ClassTransformer transformer) {
				// Nao utilizado
			}

			@Override
			public ClassLoader getNewTempClassLoader() {
				return null;
			}

			@Override
			public SharedCacheMode getSharedCacheMode() {
				return SharedCacheMode.ENABLE_SELECTIVE;
			}

			@Override
			public ValidationMode getValidationMode() {
				return ValidationMode.NONE;
			}

			@Override
			public String getPersistenceXMLSchemaVersion() {
				return null;
			}

		};
	}

	private DataSource getDataSource() {
		return DataSourceApplication.defaultDataSource();
	}

	public void adicionarClasses(final Class<? extends Entidade> classe) {
		if (classe != null) {
			this.classesEntity.add(classe.getName());
		}
	}

	@Produces
	public EntityManager createEntityManager() {
		return this.getEntityManagerFactory().createEntityManager();
	}

	public void finaliza(@Disposes final EntityManager entityManager) {
		try {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().commit();
			}
		} finally {
			entityManager.close();
		}
	}

}
