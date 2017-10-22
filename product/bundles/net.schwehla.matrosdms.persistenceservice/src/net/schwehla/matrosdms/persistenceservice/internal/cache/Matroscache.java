package net.schwehla.matrosdms.persistenceservice.internal.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Matroscache {

	String name() default "NONE";

	boolean evictAll() default false;
}
