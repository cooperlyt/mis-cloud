package io.github.cooperlyt.mis.work.create

import io.github.cooperlyt.mis.work.message.WorkStatus
import org.springframework.aot.hint.annotation.Reflective
import org.springframework.core.annotation.AliasFor

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Reflective
annotation class WorkStatusChange(

  @get:AliasFor(attribute = "workId")
  val value: String = "",

  val workId: String = "",

  val status: WorkStatus
)
