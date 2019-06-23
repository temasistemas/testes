package br.com.temasistemas.workshop.testes.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFormatMessageFactory;

import br.com.temasistemas.workshop.testes.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MainController {

	private static final Logger LOG = LogManager.getLogger(MainController.class, new MessageFormatMessageFactory());

	public MainController() {
		super();
	}

	@Inject
	FXMLLoader loaderDataSource;

	@Inject
	FXMLLoader loaderContato;

	@FXML
	private Label versaoAtualLabel;

	@FXML
	private BorderPane mainContainer;

	private Node dataSourceLoader;

	private Node contatoLoader;

	@FXML
	public void initialize() {
		this.configurarLabelVersaoAtual();
		LOG.info("INICIADO");
	}

	private void configurarLabelVersaoAtual() {
		final Properties properties = MainApp.getApplicationProperties();
		final String versao = properties.getProperty("application.versaoAtual", "0.0.0.0");
		final String buildPrefix = Optional.ofNullable(properties.getProperty("ci.prefix"))
				.filter(p -> !p.trim().isEmpty()).orElse("DSV");
		final String buildNumber = Optional.ofNullable(properties.getProperty("ci.buildNumber"))
				.filter(p -> !p.trim().isEmpty()).orElse("");
		this.versaoAtualLabel.setText(MessageFormat.format("{0}_{1}{2}", versao, buildPrefix, buildNumber));
		this.versaoAtualLabel.getTooltip().setText("Versão Atual: " + this.versaoAtualLabel.getText());
	}

	public void menuDataSource(final ActionEvent event) throws IOException {
		event.consume();
		if (this.dataSourceLoader == null) {
			this.dataSourceLoader = this.loaderDataSource.load(this.getClass().getResourceAsStream("dataSource.fxml"));
		}
		this.mainContainer.setCenter(this.dataSourceLoader);
	}

	public void menuContato(final ActionEvent event) throws IOException {
		if (this.contatoLoader == null) {
			event.consume();
			this.contatoLoader = this.loaderContato.load(this.getClass().getResourceAsStream("contato.fxml"));
		}
		this.mainContainer.setCenter(this.contatoLoader);
	}

}
