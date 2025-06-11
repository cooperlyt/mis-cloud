package io.github.cooperlyt.mis.work.create

import org.springframework.aot.hint.annotation.Reflective
import org.springframework.core.annotation.AliasFor

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Reflective
annotation class WorkEvent(
  @get:AliasFor(attribute = "defineId")
  val value: String = "",

  val defineId: String = "",

  val workId: String,

  val event: String

)
