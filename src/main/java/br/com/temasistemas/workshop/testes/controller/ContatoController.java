package br.com.temasistemas.workshop.testes.controller;

import java.util.List;

import javax.inject.Inject;

import br.com.temasistemas.workshop.testes.model.Contato;
import br.com.temasistemas.workshop.testes.service.ServicoContato;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ContatoController {

	private List<Contato> contatos;

	@Inject
	ServicoContato servicoContato;

	@FXML
	public void initialize() {
		final List<Contato> contatos = this.servicoContato.obterContatos();
	}

	@FXML
	private TextField jdbcOrigem;

	@FXML
	private TextField usuarioOrigem;

	@FXML
	private PasswordField senhaOrigem;

	@FXML
	private TextField jdbcDestino;

	@FXML
	private TextField usuarioDestino;

	@FXML
	private PasswordField senhaDestino;

	public void salva(final ActionEvent event) {
		event.consume();
	}

	public void cancela(final ActionEvent event) {
		event.consume();
	}

}
