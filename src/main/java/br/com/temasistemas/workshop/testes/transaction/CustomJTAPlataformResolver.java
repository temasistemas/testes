package br.com.temasistemas.workshop.testes.transaction;

import java.util.Map;

import org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.StandardJtaPlatformResolver;
import org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform;
import org.hibernate.service.spi.ServiceRegistryImplementor;

public class CustomJTAPlataformResolver extends StandardJtaPlatformResolver {

	private static final long serialVersionUID = -4170433194822952511L;

	public CustomJTAPlataformResolver() {
		super();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JtaPlatform resolveJtaPlatform(final Map configurationValues, final ServiceRegistryImplementor registry) {
		final JtaPlatform plataform = super.resolveJtaPlatform(configurationValues, registry);

		if (plataform instanceof NoJtaPlatform) {
			return new JavaFxJtaPlatform();
		}
		return plataform;
	}

}
