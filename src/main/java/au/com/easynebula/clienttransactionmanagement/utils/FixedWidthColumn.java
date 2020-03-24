package au.com.easynebula.clienttransactionmanagement.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
public @interface FixedWidthColumn
{
	int startAt() default 0;
	int width() default 0;
	boolean exclude() default false;
	boolean isDate() default false;
}
