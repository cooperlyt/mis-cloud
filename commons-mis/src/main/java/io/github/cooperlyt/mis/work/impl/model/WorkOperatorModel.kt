package io.github.cooperlyt.mis.work.impl.model

import io.github.cooperlyt.mis.work.data.WorkActionType
import io.github.cooperlyt.mis.work.data.WorkOperator
import io.github.cooperlyt.mis.work.data.WorkOperatorSupplier
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("work_operator")
data class WorkOperatorModel(
  @Id
  val taskId: String,
  val workId: Long,
  override val userId: String,
  override val userName: String,
  override val type: WorkActionType,

  override val workTime: LocalDateTime,

  override val orgName: String?,
  override val corpInfoId: Long?,
  override val employeeInfoId: Long?
): WorkOperator, Persistable<String> {

  constructor(taskId: String, workId: Long, type: WorkActionType, operator: WorkOperatorSupplier): this(
    taskId,
    workId,
    operator.userId,
    operator.userName,
    type,
    LocalDateTime.now(),
    operator.orgName,
    operator.corpInfoId,
    operator.employeeInfoId
  )

  override fun getId(): String? = taskId

  override fun isNew(): Boolean = true
}
