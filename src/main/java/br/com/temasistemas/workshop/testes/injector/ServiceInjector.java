package br.com.temasistemas.workshop.testes.injector;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

public interface ServiceInjector {

	<T> T locate(Class<? extends T> serviceClass);

	<T> Optional<T> locateOptionally(Class<? extends T> serviceClass);

	<T> T locate(Class<? extends T> serviceClass, Annotation... qualifiers);

	<T> T locate(String serviceName);

	<T> T locate(String serviceName, Class<T> serviceClass);

	<T> List<T> locateInstances(final Class<T> type);

	<T> T locateJndi(String jndiName);

}
