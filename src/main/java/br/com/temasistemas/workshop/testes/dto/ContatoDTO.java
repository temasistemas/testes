package br.com.temasistemas.workshop.testes.dto;

public class ContatoDTO {

	private int id;

	private String nome;

	private String email;

	private String telefone;

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getTelefone() {
		return this.telefone;
	}

	public void setTelefone(final String telefone) {
		this.telefone = telefone;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public static ContatoDTO clone(final int id2, final String nome2, final String email2, final String telefone2) {
		final ContatoDTO dto = novo(nome2, email2, telefone2);
		dto.setId(id2);
		return dto;
	}

	public static ContatoDTO novo(final String nome2, final String email2, final String telefone2) {
		final ContatoDTO dto = new ContatoDTO();
		dto.setEmail(email2);
		dto.setNome(nome2);
		dto.setTelefone(telefone2);
		return dto;
	}

}
