package br.com.temasistemas.workshop.testes.transaction;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;

public class JavaFxJtaPlatform extends AbstractJtaPlatform {

	private static final long serialVersionUID = 1L;

	@Override
	protected boolean canCacheUserTransactionByDefault() {
		return true;
	}

	@Override
	protected boolean canCacheTransactionManagerByDefault() {
		return true;
	}

	@Override
	protected TransactionManager locateTransactionManager() {
		return TransactionManagerJavaFx.instance();
	}

	@Override
	protected UserTransaction locateUserTransaction() {
		return TransactionManagerJavaFx.instance();
	}

}
