package br.com.temasistemas.workshop.testes.service;

import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.ImmutableList;

import br.com.temasistemas.workshop.testes.dao.ContatoDao;
import br.com.temasistemas.workshop.testes.dto.ContatoDTO;
import br.com.temasistemas.workshop.testes.model.Contato;
import br.com.temasistemas.workshop.testes.utils.BusinessException;
import net.vidageek.mirror.dsl.Mirror;

@RunWith(PowerMockRunner.class)
public class ServicoContatoTest {

	@Mock
	ContatoDao contatoDao;

	@InjectMocks
	ServicoContato servicoContato;

	@Captor
	ArgumentCaptor<Contato> captorContatoSalvar;

	@Captor
	ArgumentCaptor<Contato> captorContatoSalvarNovo;

	@Captor
	ArgumentCaptor<Contato> captorContatoExcluir;

	private Contato contatoMock;

	private Contato contatoMockAlter;

	@Before
	public void antes() {
		this.contatoMock = Contato.novo(ContatoDTO.novo("teste", "teste@gmail.com", null));
		(new Mirror()).on(this.contatoMock).set().field("id").withValue(1);
		this.contatoMockAlter = Contato.novo(ContatoDTO.novo("teste2", "teste2@gmail.com", null));
		(new Mirror()).on(this.contatoMockAlter).set().field("id").withValue(2);
		when(this.contatoDao.todos()).thenReturn(ImmutableList.of(this.contatoMock, this.contatoMockAlter));
		doNothing().when(this.contatoDao).salvar(this.captorContatoSalvar.capture());
		doNothing().when(this.contatoDao).delete(this.captorContatoExcluir.capture());
		when(this.contatoDao.obterPorId(1)).thenReturn(this.contatoMock);
		when(this.contatoDao.obterPorId(2)).thenReturn(this.contatoMockAlter);
		when(this.contatoDao.obterPorNome(Mockito.anyString())).thenAnswer(invocation -> {
			final String valorPesquisa = invocation.getArgument(0);
			if (StringUtils.isBlank(valorPesquisa) || !valorPesquisa.equalsIgnoreCase("t")) {
				return ImmutableList.of();
			}
			return ImmutableList.of(this.contatoMock, this.contatoMockAlter);
		});
	}

	@Test
	public void testObterTodos() {
		final List<ContatoDTO> lista = this.servicoContato.obterContatos();
		Assert.assertEquals(2, lista.size());
		Assert.assertEquals("teste", lista.get(0).getNome());
		Assert.assertEquals(1, lista.get(0).getId());
		Assert.assertEquals("teste@gmail.com", lista.get(0).getEmail());
		Assert.assertNull(lista.get(0).getTelefone());
	}

	@Test(expected = BusinessException.class)
	public void testAlterComErro() {
		this.servicoContato.alterar(ContatoDTO.clone(3, "teste3", "teste3@email.com", "21988776"));
	}

	@Test
	public void testAlter() {
		this.servicoContato.alterar(ContatoDTO.clone(2, "testealter", "testealter@email.com", "21988776"));
		final List<Contato> lista = this.captorContatoSalvar.getAllValues();
		Assert.assertEquals(1, lista.size());
		final Contato contatoAlter = lista.get(0);
		Assert.assertEquals(2, contatoAlter.getId());
		Assert.assertEquals("testealter", contatoAlter.getNome());
		Assert.assertEquals("testealter@email.com", contatoAlter.getEmail());
		Assert.assertEquals("21988776", contatoAlter.getTelefone());
	}

	@Test(expected = BusinessException.class)
	public void testExcluirComErro() {
		this.servicoContato.excluir(ContatoDTO.clone(3, "teste3", "teste3@email.com", "21988776"));
	}

	@Test
	public void testExcluir() {
		this.servicoContato.excluir(ContatoDTO.clone(2, "teste3", "teste3@email.com", "21988776"));
		final Contato excluido = this.captorContatoExcluir.getValue();
		Assert.assertEquals(2, excluido.getId());
	}

	@Test
	public void testFiltroPorId() {
		final List<ContatoDTO> obtidos = this.servicoContato.obterPorFiltro("id", "2");
		Assert.assertEquals(1, obtidos.size());
		Assert.assertEquals(2, obtidos.get(0).getId());
	}

	@Test
	public void testFiltroPorNome() {
		final List<ContatoDTO> obtidos = this.servicoContato.obterPorFiltro("nome", "t");
		Assert.assertEquals(2, obtidos.size());
		Assert.assertEquals(1, obtidos.get(0).getId());
		Assert.assertEquals(0, this.servicoContato.obterPorFiltro("nome", "a").size());
	}

	@Test
	public void testNovo() {
		final ContatoDao contatoNovoDao = PowerMockito.mock(ContatoDao.class);
		final ServicoContato servicoNovo = new ServicoContato();
		(new Mirror()).on(servicoNovo).set().field("contatoDao").withValue(contatoNovoDao);
		PowerMockito.doNothing().when(contatoNovoDao).salvar(this.captorContatoSalvarNovo.capture());
		final ContatoDTO dto = ContatoDTO.novo("tales", "talessemassert@email.com", null);
		servicoNovo.novo(dto);
		final Contato novo = this.captorContatoSalvarNovo.getValue();
		Assert.assertEquals(dto.getEmail(), novo.getEmail());
		Assert.assertEquals(dto.getNome(), novo.getNome());
		Assert.assertEquals(dto.getTelefone(), novo.getTelefone());
		Assert.assertEquals(0, novo.getId());
	}

}
