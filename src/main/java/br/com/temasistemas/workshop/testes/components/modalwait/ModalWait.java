package br.com.temasistemas.workshop.testes.components.modalwait;

import br.com.temasistemas.workshop.testes.utils.PlatformUtils;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ModalWait {
	private final Stage dialog;
	private static final ImageView LOADING_IMGVIEW = new ImageView(
			new Image(ModalWait.class.getResourceAsStream("gloading.gif")));

	public ModalWait(Window primaryStage) {
		this(primaryStage, 330, 210);
	}
	
	public ModalWait(Window primaryStage, double width, double heigth) {
		this.dialog = new Stage(javafx.stage.StageStyle.TRANSPARENT);
		this.dialog.initModality(Modality.WINDOW_MODAL);
		this.dialog.initOwner(primaryStage);
		createScene(primaryStage, width, heigth);
		configureOnShowAndOnCloseEvents();
		configureOnMousePressedAndDragEvent();
	}

	private void configureOnShowAndOnCloseEvents() {
		this.dialog.setOnShown(we -> ModalWait.this.dialog.getOwner().getScene().getRoot().setEffect(new BoxBlur()));
		this.dialog.setOnCloseRequest(we -> ModalWait.this.dialog.getOwner().getScene().getRoot().setEffect(null));
		this.dialog.setOnHidden(we -> ModalWait.this.dialog.getOwner().getScene().getRoot().setEffect(null));
	}

	private void configureOnMousePressedAndDragEvent() {
		Node root = this.dialog.getScene().getRoot();
		final Delta dragDelta = new Delta();
		root.setOnMousePressed(mouseEvent -> {
			dragDelta.x = (ModalWait.this.dialog.getX() - mouseEvent.getScreenX());
			dragDelta.y = (ModalWait.this.dialog.getY() - mouseEvent.getScreenY());
		});

		root.setOnMouseDragged(mouseEvent -> {
			ModalWait.this.dialog.setX(mouseEvent.getScreenX() + dragDelta.x);
			ModalWait.this.dialog.setY(mouseEvent.getScreenY() + dragDelta.y);
		});
	}

	private void createScene(Window primaryStage, double width, double heigth) {
		Label labelProcessando = new Label("Aguarde, procesando...");
		VBox layout = new VBox();
		layout.setPrefWidth(width);
		layout.setPrefHeight(heigth);
		layout.getStyleClass().add("modal-dialog");
		layout.getChildren().addAll(new Node[] { LOADING_IMGVIEW, labelProcessando });

		Scene scene = new Scene(layout, Color.TRANSPARENT);
		scene.getStylesheets().add(getClass().getResource("modal-dialog.css").toExternalForm());
		this.dialog.setScene(scene);
	}

	public void show() {
		if (Platform.isFxApplicationThread()) {
			this.dialog.show();
		} else {
			PlatformUtils.runAndWait(() -> ModalWait.this.dialog.show());
		}
	}

	public void close() {
		if (Platform.isFxApplicationThread()) {
			this.dialog.close();
		} else {
			PlatformUtils.runAndWait(() -> ModalWait.this.dialog.close());
		}
	}

	class Delta {
		double x;
		double y;
		Delta() {
		}
	}
}
