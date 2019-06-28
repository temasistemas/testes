package br.com.temasistemas.workshop.testes;

import java.io.IOException;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.message.MessageFormatMessageFactory;

import br.com.temasistemas.workshop.testes.service.DataSourceApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App {

	@Inject
	FXMLLoader loader;

	private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(MainApp.class,
			new MessageFormatMessageFactory());

	public void start(@Observes @StartupScene final Stage stage) {
		try {
			LOG.info("Starting Workshop Testes Application");
			stage.setScene(this.carregarFormularios());
			stage.setTitle("Workshop - Testes");
			stage.setOnCloseRequest(event -> DataSourceApplication.close());
			stage.show();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private Scene carregarFormularios() throws IOException {
		final String fxmlFile = "main.fxml";
		LOG.debug("Loading FXML for main view from: {0}", fxmlFile);
		final Parent rootNode = this.loader.load(this.getClass().getResourceAsStream(fxmlFile));
		final Scene scene = new Scene(rootNode, 800, 500);
		scene.getStylesheets().add("/styles/styles.css");
		return scene;
	}

}
