package io.github.cooperlyt.mis.work.create;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkMessageBinding {

  @AliasFor("binding")
  String value() default "";

  /**
   *
   *  @return spring stream binding name
   */
  @AliasFor("value")
  String binding() default "";
}
