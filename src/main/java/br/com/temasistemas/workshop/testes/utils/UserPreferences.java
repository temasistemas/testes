package br.com.temasistemas.workshop.testes.utils;

import static java.util.prefs.Preferences.userNodeForPackage;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFormatMessageFactory;

import br.com.temasistemas.workshop.testes.MainApp;

public class UserPreferences {

	private static final Logger LOG = LogManager.getLogger(UserPreferences.class, new MessageFormatMessageFactory());

	private static final Preferences USER_PREFERENCES = userNodeForPackage(MainApp.class);

	public static final String JDBC_DATASOURCE = "jdbc";
	public static final String USUARIO_DATASOURCE = "usuario";
	public static final String SENHA_DATASOURCE = "senha";

	private UserPreferences() {
		super();
	}

	public static String get(final String key, final String def) {
		return USER_PREFERENCES.get(key, def);
	}

	public static void put(final String key, final String value) {
		USER_PREFERENCES.put(key, value);
	}

	public static void save(final String key, final String value) {
		put(key, value);
		flush();
	}

	public static void flush() {
		flush(Optional.of(exe -> LOG.error(exe.getMessage(), exe)));
	}

	public static void flush(final Optional<Consumer<BackingStoreException>> handle) {
		try {
			USER_PREFERENCES.flush();
		} catch (final BackingStoreException e) {
			handle.ifPresent(handleExe -> handleExe.accept(e));
		}
	}

}
