package br.com.temasistemas.workshop.testes.model;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import br.com.temasistemas.workshop.testes.dto.ContatoDTO;
import br.com.temasistemas.workshop.testes.utils.BusinessException;

public class ContatoTest {

	@Test
	public void testNovo() {
		final Contato contato = Contato.novo(ContatoDTO.novo("teste", null, null));
		Assert.assertEquals("teste", contato.getNome());
		Assert.assertNull(contato.getEmail());
		Assert.assertNull(contato.getTelefone());
	}

	@Test(expected = BusinessException.class)
	public void testNovoSemNome() {
		Contato.novo(ContatoDTO.novo(null, "teste", ""));
	}

	@Test(expected = BusinessException.class)
	public void testNovoEmailInvalido() {
		final Contato contato = Contato.novo(ContatoDTO.novo("teste", "teste", ""));
	}

	@Test
	public void testAlterar() {
		final ContatoDTO dto = ContatoDTO.novo("teste", "teste@gmail.com", "219998828");
		final Contato contato = Contato.novo(dto);
		dto.setNome("testealterado");
		dto.setEmail(null);
		dto.setTelefone(null);
		contato.alterar(dto);
		Assert.assertNull(contato.getTelefone());
		Assert.assertNull(contato.getEmail());
		Assert.assertEquals(dto.getNome(), contato.getNome());
	}

	@Test(expected = BusinessException.class)
	public void testTelefoneComLetra() {
		Contato.novo(ContatoDTO.novo("teste", "teste@gmail.com", "asdasdasd"));
	}

	@Test(expected = BusinessException.class)
	public void testTelefoneComNumeroGrandao() {
		Contato.novo(ContatoDTO.novo("teste", "teste@gmail.com", "21999999999999999999999"));
	}

	@Test(expected = BusinessException.class)
	public void testNomeMaxLength() {
		Contato.novo(ContatoDTO.novo(StringUtils.repeat("a", 101), "teste@gmail.com", "21999999999"));
	}

}
