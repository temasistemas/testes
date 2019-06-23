package br.com.temasistemas.workshop.testes;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.util.AnnotationLiteral;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.message.MessageFormatMessageFactory;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

	private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(MainApp.class,
			new MessageFormatMessageFactory());

	private static Properties appProperties;

	public static void main(final String[] args) {
		launch(args);
	}

	@SuppressWarnings("serial")
	@Override
	public void start(final Stage stage) throws Exception {
		final SeContainerInitializer initializer = SeContainerInitializer.newInstance();
		final SeContainer container = initializer.initialize();
		container.getBeanManager().fireEvent(stage, new AnnotationLiteral<StartupScene>() {
		});
	}

	public static synchronized Properties getApplicationProperties() {
		if (appProperties == null) {
			final Optional<URL> applicationFile = Optional
					.ofNullable(MainApp.class.getResource("/application.properties"));
			if (applicationFile.isPresent()) {
				final Properties appPropertieFile = new Properties();
				try (InputStream openStream = applicationFile.get().openStream()) {
					appPropertieFile.load(openStream);
					appProperties = new Properties(appPropertieFile);
				} catch (final IOException e) {
					LOG.error("Erro inesperado na tentativa de leitura do arquivo de configuração da aplicação.", e);
					appProperties = new Properties();
				}
			} else {
				LOG.warn(
						"Não foi possível encontrar algum arquivo de configuração com nome 'application.properties' na pasta padrão.");
				appProperties = new Properties();
			}
		}
		return appProperties;
	}

}
