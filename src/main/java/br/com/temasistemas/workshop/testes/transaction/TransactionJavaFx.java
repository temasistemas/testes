package br.com.temasistemas.workshop.testes.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;

import br.com.temasistemas.workshop.testes.utils.GenericException;

public class TransactionJavaFx implements Transaction {

	private final Connection conn;

	private int status = Status.STATUS_NO_TRANSACTION;

	private boolean rollBackOnly;

	public TransactionJavaFx(final Connection conn) {
		this.conn = conn;
	}

	@Override
	public void commit() throws HeuristicMixedException, HeuristicRollbackException, SystemException {
		try {
			this.conn.commit();
			this.status = Status.STATUS_COMMITTED;
		} catch (final SQLException e) {
			throw new GenericException(e);
		}
	}

	@Override
	public boolean delistResource(final XAResource xaRes, final int flag) throws SystemException {
		return false;
	}

	@Override
	public boolean enlistResource(final XAResource xaRes) throws SystemException {
		return false;
	}

	@Override
	public int getStatus() throws SystemException {
		return this.status;
	}

	@Override
	public void registerSynchronization(final Synchronization synch) throws SystemException {
		// Nao fazer nada

	}

	@Override
	public void rollback() throws SystemException {
		try {
			this.conn.rollback();
			this.status = Status.STATUS_ROLLEDBACK;
		} catch (final SQLException e) {
			throw new GenericException(e);
		}
	}

	@Override
	public void setRollbackOnly() throws SystemException {
		this.rollBackOnly = true;
	}

	public void begin() {
		try {
			this.conn.setAutoCommit(false);
			this.status = Status.STATUS_ACTIVE;
		} catch (final SQLException e) {
			throw new GenericException(e);
		}
	}

	public boolean isRollBackOnly() {
		return this.rollBackOnly;
	}

}
