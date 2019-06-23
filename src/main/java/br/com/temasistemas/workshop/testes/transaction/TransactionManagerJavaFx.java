package br.com.temasistemas.workshop.testes.transaction;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.hibernate.internal.SessionImpl;

import br.com.temasistemas.workshop.testes.injector.ServiceInjectorFactory;
import br.com.temasistemas.workshop.testes.utils.GenericException;

public class TransactionManagerJavaFx implements TransactionManager, UserTransaction {

	private static TransactionManagerJavaFx instance;
	private final ThreadLocal<TransactionJavaFx> transactions = ThreadLocal.withInitial(() -> null);

	public TransactionManagerJavaFx() {
		super();
	}

	@Override
	public void begin() throws NotSupportedException, SystemException {
		final EntityManager em = ServiceInjectorFactory.getLocator().locate(EntityManager.class);
		final TransactionJavaFx transaction = new TransactionJavaFx(
				((SessionImpl) em.getDelegate()).getJdbcCoordinator().getLogicalConnection().getPhysicalConnection());
		transaction.begin();
		this.transactions.set(transaction);
	}

	@Override
	public void commit()
			throws HeuristicMixedException, HeuristicRollbackException, RollbackException, SystemException {
		this.obterTransacaoCorrente().commit();
		this.transactions.remove();
	}

	private Transaction obterTransacaoCorrente() {
		final TransactionJavaFx transaction = this.transactions.get();
		if (transaction == null) {
			throw new GenericException(
					"Nao existe transação presente nessa thread [" + Thread.currentThread().getName() + "]");
		}
		return transaction;
	}

	@Override
	public int getStatus() throws SystemException {
		if (this.transactions.get() == null) {
			return Status.STATUS_NO_TRANSACTION;
		}
		return this.obterTransacaoCorrente().getStatus();
	}

	@Override
	public Transaction getTransaction() throws SystemException {
		return this.obterTransacaoCorrente();
	}

	@Override
	public void resume(final Transaction tobj) throws InvalidTransactionException, SystemException {
		//
	}

	@Override
	public void rollback() throws SystemException {
		this.obterTransacaoCorrente().rollback();
		this.transactions.remove();
	}

	@Override
	public void setRollbackOnly() throws SystemException {
		this.obterTransacaoCorrente().setRollbackOnly();
		this.transactions.remove();
	}

	@Override
	public void setTransactionTimeout(final int seconds) throws SystemException {
		// Nao fara nada
	}

	@Override
	public Transaction suspend() throws SystemException {
		return this.obterTransacaoCorrente();
	}

	public boolean isActive() {
		try {
			return this.getStatus() == Status.STATUS_ACTIVE;
		} catch (final SystemException e) {
			return false;
		}
	}

	public static TransactionManagerJavaFx instance() {
		if (instance == null) {
			instance = new TransactionManagerJavaFx();
		}
		return instance;
	}

}
