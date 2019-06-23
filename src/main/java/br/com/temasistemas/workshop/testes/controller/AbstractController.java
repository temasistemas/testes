package br.com.temasistemas.workshop.testes.controller;

import java.util.Optional;

import br.com.temasistemas.workshop.testes.components.alert.Alerts;
import br.com.temasistemas.workshop.testes.utils.BusinessException;

public class AbstractController {

	protected void executarComTratamento(final Runnable runnable, final Optional<String> sucesso) {
		try {
			runnable.run();
			if (sucesso.isPresent()) {
				Alerts.info(sucesso.get());
			}
		} catch (final BusinessException e) {
			Alerts.error(e.getMessage());
		} catch (final Exception e) {
			Alerts.error(e);
		}
	}

	protected void executarComTratamento(final Runnable runnable) {
		this.executarComTratamento(runnable, Optional.empty());
	}

}
