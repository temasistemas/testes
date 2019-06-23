package br.com.temasistemas.workshop.testes;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import javafx.fxml.FXMLLoader;

public class FXMLLoaderProducer {

	@Inject
	Instance<Object> instance;

	@Produces
	public FXMLLoader createLoader() {
		final FXMLLoader loader = new FXMLLoader();
		loader.setControllerFactory(param -> this.instance.select(param).get());
		return loader;
	}

}
