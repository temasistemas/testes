package br.com.temasistemas.workshop.testes.controller;


import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Worker;

public enum Status {
	PRONTO(Worker.State.READY), AGENDADO(Worker.State.SCHEDULED), EXECUTANDO(Worker.State.RUNNING),
	FINALIZADO_COM_SUCESSO(Worker.State.SUCCEEDED), CANCELADO(Worker.State.CANCELLED),
	FINALIZADO_COM_ERRO(Worker.State.FAILED);

	private ObjectProperty<Worker.State> stateUnwrapProperty;

	private Status(Worker.State stateUnwrap) {
		this.stateUnwrapProperty = new SimpleObjectProperty<Worker.State>(this, "stateUnwrap", stateUnwrap);
	}

	public ObjectProperty<Worker.State> stateUnwrapProperty() {
		return this.stateUnwrapProperty;
	}

	public static ObjectBinding<Status> createBinding(ReadOnlyObjectProperty<Worker.State> stateProperty) {
		ObjectBinding<Status> statusBindingState = Bindings.when(stateProperty.isEqualTo(Worker.State.RUNNING))
				.then(EXECUTANDO)
				.otherwise(Bindings.when(stateProperty.isEqualTo(Worker.State.READY)).then(PRONTO)
						.otherwise(Bindings.when(stateProperty.isEqualTo(Worker.State.SCHEDULED)).then(AGENDADO)
								.otherwise(Bindings.when(stateProperty.isEqualTo(Worker.State.SUCCEEDED))
										.then(FINALIZADO_COM_SUCESSO)
										.otherwise(Bindings.when(stateProperty.isEqualTo(Worker.State.FAILED))
												.then(FINALIZADO_COM_ERRO).otherwise(CANCELADO)))));

		return statusBindingState;
	}
}
