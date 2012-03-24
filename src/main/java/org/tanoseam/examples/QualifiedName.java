package org.tanoseam.examples;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.inject.Named;
import javax.enterprise.event.Observes;
import javax.enterprise.util.AnnotationLiteral;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class QualifiedName implements Extension {

	public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {

		final AnnotatedType<T> at = event.getAnnotatedType();

		log("ProcessAnnotatedType: AnnotatedType" + at);

		AnnotatedType<T> wrapped = new AnnotatedType<T>() {
			Set<Annotation> annotations = new HashSet<Annotation>();
			HashMap<String, String> valueMap = new HashMap<String, String>();

			@Override
			public Set<AnnotatedConstructor<T>> getConstructors() {
				return at.getConstructors();
			}

			@Override
			public Set<AnnotatedField<? super T>> getFields() {
				return at.getFields();
			}

			@Override
			public Class<T> getJavaClass() {
				return at.getJavaClass();
			}

			@Override
			public Set<AnnotatedMethod<? super T>> getMethods() {
				return at.getMethods();
			}

			@Override
			public <X extends Annotation> X getAnnotation(final Class<X> annType) {
				if (Named.class.equals(annType)) {
					class NamedLiteral extends AnnotationLiteral<Named>
							implements Named {
						
						String qualifiedName;

						@Override
						public String value() {
							if (qualifiedName == null) {
								Package pkg = at.getJavaClass().getPackage();
								String unqualifiedName = at.getAnnotation(
										Named.class).value();
	
								if (unqualifiedName.isEmpty()) {
									String className = at.getJavaClass().getSimpleName();
									unqualifiedName = StringUtils.uncapitalize(className);
								}
								
								qualifiedName = pkg.getAnnotation(
										Named.class).value()
										+ '.' + unqualifiedName;
								log("\t unqualifeidName=" + unqualifiedName);
								log("\t qualifeidName=" + qualifiedName);
								
							}
							return qualifiedName;
						}
					}
					return (X) new NamedLiteral();
				} else {
					return at.getAnnotation(annType);
				}
			}

			@Override
			public Set<Annotation> getAnnotations() {
				if (annotations.isEmpty()) {
					for (Annotation a : at.getAnnotations()) {
						annotations.add(getAnnotation(a.annotationType()));
					}
				}
				return annotations;
			}

			@Override
			public Type getBaseType() {
				return at.getBaseType();
			}

			@Override
			public Set<Type> getTypeClosure() {
				return at.getTypeClosure();
			}

			@Override
			public boolean isAnnotationPresent(
					Class<? extends Annotation> annType) {
				return at.isAnnotationPresent(annType);
			}
		};
		event.setAnnotatedType(wrapped);
	}

	void log(String messages) {
		System.out.println("CDI: " + messages);
	}
}
