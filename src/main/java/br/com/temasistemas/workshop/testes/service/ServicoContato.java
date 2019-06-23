package br.com.temasistemas.workshop.testes.service;

import java.util.List;

import javax.inject.Inject;

import br.com.temasistemas.workshop.testes.dao.ContatoDao;
import br.com.temasistemas.workshop.testes.model.Contato;

public class ServicoContato {

	@Inject
	ContatoDao contatoDao;

	public List<Contato> obterContatos() {
		return this.contatoDao.todos();
	}

}
