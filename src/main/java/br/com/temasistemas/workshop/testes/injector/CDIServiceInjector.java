package br.com.temasistemas.workshop.testes.injector;

import static java.text.MessageFormat.format;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.InitialContext;

import br.com.temasistemas.workshop.testes.utils.GenericException;

@Singleton
public class CDIServiceInjector implements ServiceInjector {

	@Inject
	private BeanManager beanManager;

	public CDIServiceInjector() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T locate(final Class<? extends T> serviceClass) {
		final Set<Bean<?>> beans = this.beanManager.getBeans(serviceClass);
		if (beans.isEmpty()) {
			throw new GenericException(
					MessageFormat.format("Não foi encontrado nenhum bean do tipo: {0}", serviceClass));
		}
		final Bean<T> bean = (Bean<T>) beans.iterator().next();
		final CreationalContext<T> context = this.beanManager.createCreationalContext(bean);
		return (T) this.beanManager.getReference(bean, serviceClass, context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> locateOptionally(final Class<? extends T> serviceClass) {
		final Set<Bean<?>> beans = this.beanManager.getBeans(serviceClass);
		if (beans.isEmpty()) {
			return Optional.empty();
		}
		final Bean<T> bean = (Bean<T>) beans.iterator().next();
		final CreationalContext<T> context = this.beanManager.createCreationalContext(bean);
		return Optional.ofNullable((T) this.beanManager.getReference(bean, serviceClass, context));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T locate(final String serviceName) {
		final Set<Bean<?>> beans = this.beanManager.getBeans(serviceName);
		if (beans.isEmpty()) {
			throw new GenericException(
					MessageFormat.format("Não foi encontrado nenhum bean do nome: {0}", serviceName));
		}
		final Bean<T> bean = (Bean<T>) beans.iterator().next();
		final CreationalContext<T> context = this.beanManager.createCreationalContext(bean);
		return (T) this.beanManager.getReference(bean, Object.class, context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T locate(final String serviceName, final Class<T> serviceClass) {

		final Set<Bean<?>> beans = this.beanManager.getBeans(serviceName);
		if (beans.isEmpty()) {
			throw new GenericException(
					MessageFormat.format("Não foi encontrado nenhum bean do nome: {0}", serviceName));
		}

		for (final Bean<?> bean : beans) {
			if (serviceClass.isAssignableFrom(bean.getBeanClass())) {
				final Bean<T> targetBean = (Bean<T>) bean;
				final CreationalContext<T> context = this.beanManager.createCreationalContext(targetBean);
				return (T) this.beanManager.getReference(targetBean, serviceClass, context);
			}
		}

		throw new GenericException(MessageFormat.format("Não foi encontrado nenhum bean do nome: {0} do tipo: {1}",
				serviceName, serviceClass));

	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> locateInstances(final Class<T> type) {
		final Set<Bean<?>> beans = this.beanManager.getBeans(type);
		final List<T> result = new ArrayList<>(beans.size());

		for (final Bean<?> bean : beans) {
			final Bean<T> targetBean = (Bean<T>) bean;
			final CreationalContext<T> context = this.beanManager.createCreationalContext(targetBean);
			result.add((T) this.beanManager.getReference(targetBean, type, context));
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T locate(final Class<? extends T> serviceClass, final Annotation... qualifiers) {
		final Set<Bean<?>> beans = this.beanManager.getBeans(serviceClass, qualifiers);
		if (beans.isEmpty()) {
			throw new GenericException(
					MessageFormat.format("Não foi encontrado nenhum bean do tipo: {0} com os qualificadores: {1}",
							serviceClass, qualifiers));
		}
		final Bean<T> targetBean = (Bean<T>) beans.iterator().next();
		final CreationalContext<T> context = this.beanManager.createCreationalContext(targetBean);
		return (T) this.beanManager.getReference(targetBean, serviceClass, context);
	}

	@Override
	public <T> T locateJndi(final String jndiName) {
		try {
			return InitialContext.doLookup(jndiName);
		} catch (final Exception e) {
			throw new GenericException(
					format("Erro inesperado ao localizar o recurso: {0} no jndi. Causa: {1}", jndiName, e.getMessage()),
					e);
		}
	}

	public void setBeanManager(final BeanManager beanManager) {
		this.beanManager = beanManager;
	}
}