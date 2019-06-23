package br.com.temasistemas.workshop.testes.injector;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

public class ServiceInjectorFactory {

	private static ServiceInjector cache;

	private ServiceInjectorFactory() {
		super();
	}

	@SuppressWarnings("unchecked")
	public static ServiceInjector getLocator() {
		if (cache == null) {
			final BeanManager beanManager = CDI.current().getBeanManager();
			final Set<Bean<?>> beans = beanManager.getBeans(ServiceInjector.class);
			final Bean<ServiceInjector> bean = (Bean<ServiceInjector>) beans.iterator().next();
			final CreationalContext<ServiceInjector> context = beanManager.createCreationalContext(bean);
			cache = (ServiceInjector) beanManager.getReference(bean, ServiceInjector.class, context);
		}

		return cache;
	}

}
