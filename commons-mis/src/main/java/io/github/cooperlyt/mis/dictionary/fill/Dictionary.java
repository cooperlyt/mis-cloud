package io.github.cooperlyt.mis.dictionary.fill;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface Dictionary {

  static final String DISTRICT_CATEGORY = "_district";

  enum NotFoundAction{
    EXCEPTION,
    NULL,
    BLANK,
    KEY
  }

  @AliasFor("category")
  String value() default "";

  @AliasFor("value")
  String category() default "";



  /** 如果为空 prefix + 当前方法名 + suffix
   *  字典值字段
   * @return 字典值字段
   */
  String targetName() default "";

  /**
   * 如果 targetName 不为空 则忽略 targetPrefix 和 targetSuffix 如果目标为 set 方法，自动加 set...
   * @return 前缀
   */
  String targetPrefix() default "";
  String targetSuffix() default "Label";

  boolean targetIsField() default true;

  boolean optional() default true;


  NotFoundAction notFoundAction() default NotFoundAction.KEY;

}
