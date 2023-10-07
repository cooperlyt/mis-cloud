package io.github.cooperlyt.mis.work.create;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkCreate {

  enum OperatorWay {
    FIRST,LAST,NONE
  }

  @AliasFor("defineId")
  String value() default "";

  @AliasFor("value")
  String defineId() default "";

  boolean resultIsWorkId() default false;

  boolean transactionalRequired()  default true;

  String binding() default "";

  /**
   *  如果为 true, 返回 Mono.empty() 或 Mono<Boolean> false 则不会创建工作
   * @return 是否可选
   */
  boolean optional() default false;

  OperatorWay operatorWay() default OperatorWay.LAST;


}
