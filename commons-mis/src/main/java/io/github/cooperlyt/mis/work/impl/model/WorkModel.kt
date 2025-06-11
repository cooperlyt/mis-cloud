package io.github.cooperlyt.mis.work.impl.model

import io.github.cooperlyt.mis.work.data.WorkDefine
import io.github.cooperlyt.mis.work.data.WorkInfo
import io.github.cooperlyt.mis.work.message.WorkStatus
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("work")
data class WorkModel(
  @Id
  override val workId: Long,
  override val dataSource: String,
  override val workName: String,
  override var status: WorkStatus,




  override val type: String?,
  override val process: Boolean,
  override val defineId: String,

  override val historyAt: LocalDateTime? = null,
  override var completedAt: LocalDateTime? = null,

  override var validatedAt: LocalDateTime? = null,
  @CreatedDate
  override var createdAt: LocalDateTime? = null,
  @LastModifiedDate
  override var updatedAt: LocalDateTime? = null,
  @Version
  var version: Long? = null
): WorkInfo {

  constructor(define: WorkDefine, workId: Long, status: WorkStatus, dataSource: String, historyAt: LocalDateTime? = null) :this(
    workId = workId,
    dataSource = dataSource,
    workName = define.workName,
    status = status,
    historyAt = historyAt,
    type = define.type,
    process = define.isProcess,
    defineId = define.defineId,
  )
}
