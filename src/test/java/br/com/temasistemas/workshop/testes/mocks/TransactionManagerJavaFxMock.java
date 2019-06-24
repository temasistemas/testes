package br.com.temasistemas.workshop.testes.mocks;

import javax.persistence.EntityManager;

import br.com.temasistemas.workshop.testes.transaction.TransactionManagerJavaFx;

public class TransactionManagerJavaFxMock extends TransactionManagerJavaFx {

	@Override
	protected EntityManager getEntityManager() {
		return MocksForGenericDao.instance().getEntityManager();
	}

}
