package org.laroche.multidispatch.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation specifying the methods to multi dispatch.
 * <br />
 * Date: 26/09/12
 * Time: 12:45
 *
 * @author françois LAROCHE
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface MultiDispatched {}
