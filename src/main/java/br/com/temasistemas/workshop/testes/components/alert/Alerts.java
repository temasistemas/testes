package br.com.temasistemas.workshop.testes.components.alert;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public final class Alerts {

	private static final String MENSAGENS_DO_PROCESSO = "Mensagens do Processo";

	private Alerts() {
		super();
	}

	public static Optional<ButtonType> showWarnAndWait(final String mensagem) {
		return warn(mensagem).showAndWait();
	}

	public static Alert warn(final String message) {
		return warn(Optional.<String>of("Atenção!"), message);
	}

	public static Alert warn(final Optional<String> headerMessage, final String message) {
		final Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(headerMessage.orElse(MENSAGENS_DO_PROCESSO));
		headerMessage.ifPresent(alert::setHeaderText);
		alert.setContentText(message);
		return alert;
	}

	public static void info(final String message) {
		info(Optional.<String>of("Operação realizada com sucesso!"), message).showAndWait();
	}

	public static Alert info(final Optional<String> headerMessage, final String message) {
		final Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(headerMessage.orElse(MENSAGENS_DO_PROCESSO));
		headerMessage.ifPresent(alert::setHeaderText);
		alert.setContentText(message);
		return alert;
	}

	public static void error(final String mensagem) {
		error(Optional.empty(), mensagem, Optional.<Throwable>empty()).showAndWait();
	}

	public static void error(final Exception e) {
		showErrorAndWait(e.getMessage(), Optional.of(e));
	}

	public static Optional<ButtonType> showErrorAndWait(final String mensagem, final Optional<Throwable> exception) {
		return error(Optional.empty(), mensagem, exception).showAndWait();
	}

	public static Alert error(final Optional<String> headerMessage, final String mensagem,
			final Optional<Throwable> exception) {
		final Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setTitle(headerMessage.orElse(MENSAGENS_DO_PROCESSO));
		errorAlert.setHeaderText("Ops!, Aconteceu algo de errado.");
		errorAlert.setContentText(mensagem);

		if (exception.isPresent()) {
			final StringWriter sw = new StringWriter();
			try (PrintWriter pw = new PrintWriter(sw, true)) {
				exception.get().printStackTrace(pw);
			}

			final Label label = new Label("Informações técnicas para análise:");
			final TextArea textArea = new TextArea(sw.toString());
			textArea.setEditable(false);
			textArea.setWrapText(true);

			textArea.setMaxWidth(Double.MAX_VALUE);
			textArea.setMaxHeight(Double.MAX_VALUE);
			GridPane.setVgrow(textArea, Priority.ALWAYS);
			GridPane.setHgrow(textArea, Priority.ALWAYS);

			final GridPane expContent = new GridPane();
			expContent.setMaxWidth(Double.MAX_VALUE);
			expContent.add(label, 0, 0);
			expContent.add(textArea, 0, 1);

			errorAlert.getDialogPane().setExpandableContent(expContent);
		}

		return errorAlert;
	}
}
