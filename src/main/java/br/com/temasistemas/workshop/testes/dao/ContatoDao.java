package br.com.temasistemas.workshop.testes.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.temasistemas.workshop.testes.model.Contato;

public class ContatoDao {

	@Inject
	EntityManager em;

	public List<Contato> todos() {
		return this.em.createQuery("from Contato", Contato.class).getResultList();
	}

	public void salvar(final Contato contato) {
		this.em.persist(contato);
	}

	public void delete(final Contato contato) {
		this.em.remove(contato);
	}

}
