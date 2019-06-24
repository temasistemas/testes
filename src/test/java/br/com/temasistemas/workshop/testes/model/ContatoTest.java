package br.com.temasistemas.workshop.testes.model;

import org.junit.Assert;
import org.junit.Test;

import br.com.temasistemas.workshop.testes.dto.ContatoDTO;

public class ContatoTest {

	@Test
	public void testNovo() {
		final Contato contato = Contato.novo(ContatoDTO.novo("teste", "teste", ""));
		Assert.assertEquals("teste", contato.getNome());
		Assert.assertEquals("teste", contato.getEmail());
		Assert.assertNull(contato.getTelefone());
	}

}
