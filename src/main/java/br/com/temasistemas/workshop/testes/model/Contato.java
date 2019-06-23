package br.com.temasistemas.workshop.testes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import br.com.temasistemas.workshop.testes.dto.ContatoDTO;
import br.com.temasistemas.workshop.testes.persistence.Entidade;
import br.com.temasistemas.workshop.testes.utils.BusinessException;

@Entity
@Table(name = "Contato")
public class Contato implements Entidade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private int id;

	@Column(name = "Nome")
	private String nome;

	@Column(name = "Email")
	private String email;

	@Column(name = "telefone")
	private String telefone;

	@Column(name = "Endereco")
	private String endereco;

	public static Contato nova(final ContatoDTO dto) {
		final Contato contato = new Contato();
		contato.alterar(dto);
		return contato;
	}

	public void alterar(final ContatoDTO dto) {
		this.setNome(dto.getNome());
		this.setEmail(dto.getEmail());
		this.setTelefone(dto.getTelefone());
		this.setEndereco(dto.getEndereco());
	}

	public String getNome() {
		return this.nome;
	}

	public String getEmail() {
		return this.email;
	}

	public String getTelefone() {
		return this.telefone;
	}

	public String getEndereco() {
		return this.endereco;
	}

	protected void setNome(final String nome) {
		if (StringUtils.isBlank(nome)) {
			throw new BusinessException("Nome � requerido");
		}
		this.nome = nome;
	}

	protected void setEmail(final String email) {
		this.email = email;
	}

	protected void setTelefone(final String telefone) {
		this.telefone = telefone;
	}

	protected void setEndereco(final String endereco) {
		this.endereco = endereco;
	}

}
