package br.com.temasistemas.workshop.testes.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.com.temasistemas.workshop.testes.dao.ContatoDao;
import br.com.temasistemas.workshop.testes.dto.ContatoDTO;
import br.com.temasistemas.workshop.testes.model.Contato;
import br.com.temasistemas.workshop.testes.utils.BusinessException;

public class ServicoContato {

	@Inject
	ContatoDao contatoDao;

	public List<ContatoDTO> obterContatos() {
		return this.contatoDao.todos().stream().map(entity -> entity.dto()).collect(Collectors.toList());
	}

	public void novo(final ContatoDTO dto) {
		final Contato contato = Contato.nova(dto);
		this.contatoDao.salvar(contato);
	}

	public void excluir(final ContatoDTO dto) {
		final Contato contato = this.contatoDao.obterPorId(dto.getId());
		if (contato == null) {
			throw new BusinessException("Não foi encontrado o contato com o id informado para exclusão");
		}
		this.contatoDao.delete(contato);
	}

	public void alterar(final ContatoDTO dto) {
		final Contato contato = this.contatoDao.obterPorId(dto.getId());
		if (contato == null) {
			throw new BusinessException("Não foi encontrado o contato com o id informado para alteração");
		}
		contato.alterar(dto);
		this.contatoDao.salvar(contato);
	}

}
