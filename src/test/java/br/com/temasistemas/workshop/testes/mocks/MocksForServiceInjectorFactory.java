package br.com.temasistemas.workshop.testes.mocks;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.text.MessageFormat;

import javax.enterprise.inject.InjectionException;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.temasistemas.workshop.testes.injector.ServiceInjector;
import br.com.temasistemas.workshop.testes.injector.ServiceInjectorFactory;
import net.vidageek.mirror.dsl.Mirror;

public class MocksForServiceInjectorFactory {

	private static final String MSG_INJECAO = "Nao foi possivel atribuir no field [{0}] da classe [{1}] o valor [{1}]";

	private final ServiceInjector serviceInjector;

	private MocksForServiceInjectorFactory() {
		super();
		mockStatic(ServiceInjectorFactory.class);
		this.serviceInjector = mock(ServiceInjector.class);
		when(ServiceInjectorFactory.getLocator()).thenReturn(this.serviceInjector);
	}

	public static MocksForServiceInjectorFactory makeMock() {
		return new MocksForServiceInjectorFactory();
	}

	public <T> MocksForServiceInjectorFactory injectMock(final Class<T> classe) {
		final T mock = mock(classe);
		return this.injectMock(classe, mock);
	}

	public <T> MocksForServiceInjectorFactory injectMock(final Class<T> classe, final T mock) {
		when(this.serviceInjector.locate(classe)).thenReturn(mock);
		return this;
	}

	public MocksForServiceInjectorFactory injectTargetWithInjectAnotationPresent(final Object target) {
		if (target != null) {
			new Mirror().on(target.getClass()).reflectAll().fields().stream().forEach(field -> {
				if (field.isAnnotationPresent(Inject.class)) {
					final Object mock = this.obterETreinarSeNecessario(field.getType());
					try {
						field.setAccessible(true);
						field.set(target, mock);
					} catch (final IllegalArgumentException | IllegalAccessException e) {
						throw new InjectionException(
								MessageFormat.format(MSG_INJECAO, field.getName(), target.getClass(), mock));
					}
				}
			});
		}
		return this;
	}

	private Object obterETreinarSeNecessario(final Class<?> classe) {
		if (classe.isAssignableFrom(EntityManager.class)) {
			return MocksForGenericDao.instance().getEntityManager();
		}
		Object mock = this.serviceInjector.locate(classe);
		if (mock == null) {
			this.injectMock(classe);
			mock = this.serviceInjector.locate(classe);
		}
		return mock;
	}

}
