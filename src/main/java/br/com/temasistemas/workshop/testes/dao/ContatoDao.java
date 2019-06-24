package br.com.temasistemas.workshop.testes.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.temasistemas.workshop.testes.model.Contato;

public class ContatoDao extends GenericDao {

	@Inject
	EntityManager em;

	public List<Contato> todos() {
		return this.em.createQuery("from Contato", Contato.class).getResultList();
	}

	@Override
	protected EntityManager getEm() {
		return this.em;
	}

	public Contato obterPorId(final int id) {
		return this.em.find(Contato.class, id);
	}

	public List<Contato> obterPorNome(final String valorPesquisa) {
		final Query query = this.em.createQuery("from Contato where nome like :nome ", Contato.class);
		query.setParameter("nome", valorPesquisa.concat("%"));
		return query.getResultList();
	}

	public List<Contato> obterPorEmail(final String valorPesquisa) {
		final Query query = this.em.createQuery("from Contato where email like :email ", Contato.class);
		query.setParameter("email", valorPesquisa.concat("%"));
		return query.getResultList();
	}

}
