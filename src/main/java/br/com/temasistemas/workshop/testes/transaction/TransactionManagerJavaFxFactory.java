package br.com.temasistemas.workshop.testes.transaction;

public class TransactionManagerJavaFxFactory {

	private TransactionManagerJavaFxFactory() {
		super();
	}

	private static TransactionManagerJavaFx instance;

	public static TransactionManagerJavaFx instance() {
		if (instance == null) {
			instance = new TransactionManagerJavaFx();
		}
		return instance;
	}

}
