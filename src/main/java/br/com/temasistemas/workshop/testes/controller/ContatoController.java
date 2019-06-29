package br.com.temasistemas.workshop.testes.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.temasistemas.workshop.testes.dto.ContatoDTO;
import br.com.temasistemas.workshop.testes.service.ServicoContato;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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

	@FXML
	TableColumn<ContatoDTO, Integer> columnId;

	@FXML
	TableColumn<ContatoDTO, String> columnNome;

	@FXML
	TableColumn<ContatoDTO, String> columnEmail;

	@FXML
	TableColumn<ContatoDTO, String> columnTelefone;

	@FXML
	RadioButton rdId;

	@FXML
	RadioButton rdNome;

	@FXML
	RadioButton rdEmail;

	@FXML
	RadioButton rdTelefone;

	@FXML
	Button btnSalvar;

	@FXML
	Button btnNovo;

	@FXML
	Button btnPesquisar;

	@FXML
	Button btnExcluir;

	@Inject
	ServicoContato servicoContato;

	List<ContatoDTO> contatos;

	ObservableList<ContatoDTO> observableList;

	private List<RadioButton> radios;

	@FXML
	TextField txtPesquisa;

	@FXML
	public void initialize() {
		this.columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		this.columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		this.columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		this.columnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		this.carregarLista();
		this.rdNome.setSelected(true);
		this.radios = Arrays.asList(this.rdId, this.rdNome, this.rdEmail, this.rdTelefone);
		this.btnExcluir.setDisable(true);
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
		this.limparFields();
		this.carregarLista();
		event.consume();
	}

	private void carregarLista() {
		this.contatos = this.carregarComFiltro();
		this.observableList = FXCollections.observableArrayList(this.contatos);
		this.tabela.setItems(this.observableList);
		this.tabela.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> this.selecionarItem(newValue));
	}

	private List<ContatoDTO> carregarComFiltro() {
		if (this.txtPesquisa == null || StringUtils.isBlank(this.txtPesquisa.getText())) {
			return this.servicoContato.obterContatos();
		}
		return this.servicoContato.obterPorFiltro(
				this.getFieldPesquisa(this.radios.stream().filter(RadioButton::isSelected).findFirst().orElse(null)),
				this.txtPesquisa.getText());
	}

	private String getFieldPesquisa(final RadioButton radio) {
		if (radio == null) {
			return null;
		}
		return radio.getId().toLowerCase().replace("rd", "");
	}

	private void selecionarItem(final ContatoDTO selecionado) {
		this.limparFields();
		if (selecionado != null) {
			this.detalheId.setText(String.valueOf(selecionado.getId()));
			this.detalheNome.setText(selecionado.getNome());
			this.detalheEmail.setText(selecionado.getEmail());
			this.detalheTelefone.setText(selecionado.getTelefone());
			this.btnExcluir.setDisable(false);
		} else {
			this.btnExcluir.setDisable(true);
		}
	}

	public void excluir(final ActionEvent event) {
		this.executarComTratamento(
				() -> this.servicoContato.excluir(ContatoDTO.clone(Integer.valueOf(this.detalheId.getText()),
						this.detalheNome.getText(), this.detalheEmail.getText(), this.detalheTelefone.getText())),
				Optional.of("Excluído com sucesso"));
		this.carregarLista();
		event.consume();
	}

	public void radioSelecionado(final ActionEvent event) {
		this.radios.stream().forEach(radio -> radio.setSelected(false));
		((RadioButton) event.getSource()).setSelected(true);
	}

	public void pesquisar(final ActionEvent event) {
		this.executarComTratamento(() -> {
			event.consume();
			this.carregarLista();
		});
	}

	public void novo(final ActionEvent event) {
		this.btnExcluir.setDisable(true);
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
