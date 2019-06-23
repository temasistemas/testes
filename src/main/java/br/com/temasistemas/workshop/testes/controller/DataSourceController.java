package br.com.temasistemas.workshop.testes.controller;

import static br.com.temasistemas.workshop.testes.service.DataSourceApplication.DEFAULT_URL_H2;

import com.zaxxer.hikari.HikariDataSource;

import br.com.temasistemas.workshop.testes.components.alert.Alerts;
import br.com.temasistemas.workshop.testes.service.DataSourceApplication;
import br.com.temasistemas.workshop.testes.utils.UserPreferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class DataSourceController {

	@FXML
	private TextField jdbc;

	@FXML
	private TextField usuario;

	@FXML
	private PasswordField senha;

	@FXML
	public void initialize() {
		this.jdbc.setText(UserPreferences.get(UserPreferences.JDBC_DATASOURCE, DEFAULT_URL_H2));
		this.usuario.setText(UserPreferences.get(UserPreferences.USUARIO_DATASOURCE, "sa"));
		this.senha.setText(UserPreferences.get(UserPreferences.SENHA_DATASOURCE, ""));
	}

	public void confirmar(final ActionEvent event) {
		event.consume();
		try {
			if (this.verificarDataSource()) {
				UserPreferences.put(UserPreferences.JDBC_DATASOURCE, this.jdbc.getText());
				UserPreferences.put(UserPreferences.USUARIO_DATASOURCE, this.usuario.getText());
				UserPreferences.put(UserPreferences.SENHA_DATASOURCE, this.senha.getText());
				Alerts.info("Data source verificado com sucesso");
			}
		} catch (final Exception e) {
			Alerts.error(e);
		}
	}

	private boolean verificarDataSource() {
		try {
			final HikariDataSource ds = DataSourceApplication.criarDataSourceSePossivel(this.jdbc.getText(),
					this.usuario.getText(), this.senha.getText());
			if (ds != null) {
				ds.close();
				return true;
			}
		} catch (final Exception e) {
			Alerts.error(e);
		}
		return false;
	}

}
