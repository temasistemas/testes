package br.com.temasistemas.workshop.testes.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.com.temasistemas.workshop.testes.injector.ServiceInjectorFactory;
import br.com.temasistemas.workshop.testes.mocks.MocksForGenericDao;
import br.com.temasistemas.workshop.testes.model.Contato;
import br.com.temasistemas.workshop.testes.service.DataSourceApplication;
import br.com.temasistemas.workshop.testes.transaction.TransactionManagerJavaFxFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = { DataSourceApplication.class, ServiceInjectorFactory.class,
		TransactionManagerJavaFxFactory.class })
@PowerMockIgnore(value = { "javax.transaction.*", "javax.management.*" })
public class ContatoDaoTest {

	@InjectMocks
	ContatoDao contatoDao;

	@Before
	public void antes() {
		MocksForGenericDao.instance().addSchemaSQLAsResource("testes_schema.sql")
				.addDataSetAsResource("testes_dataset.xml").executeAll().adicionarClasseEntidade(Contato.class)
				.criarContextoPersistencia();
		MocksForGenericDao.instance().getMocksForServiceInjectorFactory()
				.injectTargetWithInjectAnotationPresent(this.contatoDao);
	}

	@Test
	public void testObterPorId() {
		final Contato contato = this.contatoDao.obterPorId(1);
		Assert.assertNotNull(contato);
		Assert.assertEquals(1, contato.getId());
	}

}
