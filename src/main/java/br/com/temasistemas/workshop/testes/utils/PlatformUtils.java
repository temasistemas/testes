package br.com.temasistemas.workshop.testes.utils;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFormatMessageFactory;

import javafx.application.Platform;

public final class PlatformUtils {

	private PlatformUtils() {
		super();
	}

	private static final Logger LOG = LogManager.getLogger(PlatformUtils.class, new MessageFormatMessageFactory());

	public static void runLater(final Runnable runnable) {
		Platform.runLater(runnable);
	}

	public static void runAndWait(final Runnable runnable) {
		if (runnable == null) {
			return;
		}

		if (Platform.isFxApplicationThread()) {
			runnable.run();
			return;
		}

		final CountDownLatch countDownLatch = new CountDownLatch(1);
		Platform.runLater(() -> {
			try {
				runnable.run();
			} finally {
				countDownLatch.countDown();
			}
		});

		try {
			countDownLatch.await();
		} catch (final InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public static <V> V runAndWait(final Callable<V> callable) {
		return runAndWait(callable, Optional.of(exe -> LOG.error(exe.getMessage(), exe)));
	}

	public static <V> V runAndWait(final Callable<V> callable, final Optional<Consumer<Exception>> exeHandler) {
		if (callable == null) {
			return null;
		}
		if (Platform.isFxApplicationThread()) {
			try {
				return callable.call();
			} catch (final Exception e) {
				exeHandler.ifPresent(handler -> handler.accept(e));
				return null;
			}
		} else {
			final CountDownLatch countDownLatch = new CountDownLatch(1);
			final AtomicReference<V> result = new AtomicReference<>();

			Platform.runLater(() -> {
				try {
					result.set(callable.call());
				} catch (final Exception e) {
					exeHandler.ifPresent(handler -> handler.accept(e));
				} finally {
					countDownLatch.countDown();
				}
			});

			try {
				countDownLatch.await();
			} catch (final InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			return result.get();
		}
	}

}
