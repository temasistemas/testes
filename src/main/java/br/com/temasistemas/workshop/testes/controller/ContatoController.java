package br.com.temasistemas.workshop.testes.controller;

import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.temasistemas.workshop.testes.dto.ContatoDTO;
import br.com.temasistemas.workshop.testes.service.ServicoContato;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ContatoController extends AbstractController {

	@FXML
	TextField detalheId;

	@FXML
	TextField detalheNome;

	@FXML
	TextField detalheEmail;

	@FXML
	TextField detalheTelefone;

	@FXML
	TableView<ContatoDTO> tabela;

	@Inject
	ServicoContato servicoContato;

	@FXML
	public void initialize() {
		this.tabela.getItems().addAll(this.servicoContato.obterContatos());
	}

	public void salvar(final ActionEvent event) {
		if (StringUtils.isBlank(this.detalheId.getText())) {
			this.executarComTratamento(
					() -> this.servicoContato.novo(ContatoDTO.novo(this.detalheNome.getText(),
							this.detalheEmail.getText(), this.detalheTelefone.getText())),
					Optional.of("Incluído com sucesso"));
		} else {
			this.executarComTratamento(
					() -> this.servicoContato.alterar(ContatoDTO.clone(Integer.valueOf(this.detalheId.getText()),
							this.detalheNome.getText(), this.detalheEmail.getText(), this.detalheTelefone.getText())),
					Optional.of("Alterado com sucesso"));
		}
		event.consume();
	}

	public void excluir(final ActionEvent event) {
		this.executarComTratamento(
				() -> this.servicoContato.excluir(ContatoDTO.clone(Integer.valueOf(this.detalheId.getText()),
						this.detalheNome.getText(), this.detalheEmail.getText(), this.detalheTelefone.getText())),
				Optional.of("Excluído com sucesso"));
		event.consume();
	}

	public void pesquisar(final ActionEvent event) {
		event.consume();
	}

	public void novo(final ActionEvent event) {
		this.limparFields();
		this.detalheNome.requestFocus();
		event.consume();
	}

	private void limparFields() {
		this.detalheId.clear();
		this.detalheEmail.clear();
		this.detalheNome.clear();
		this.detalheTelefone.clear();
	}

}
