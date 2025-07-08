package io.github.cooperlyt.mis.service.work.domain.model

import io.github.cooperlyt.commons.data.StringList
import io.github.cooperlyt.mis.work.data.WorkDefine
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table

@Table("work_define")
data class WorkDefineModel(
  @Id
  override val defineId: String,
  override val workName: String,
  override val type: String,
  override val process: Boolean,
  override val enabled: Boolean,
  override val tags: StringList,
  @Version
  val version: Long? = null
) : WorkDefine