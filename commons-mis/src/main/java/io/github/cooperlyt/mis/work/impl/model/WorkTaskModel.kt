package io.github.cooperlyt.mis.work.impl.model

import io.github.cooperlyt.mis.work.data.WorkTask
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("work_task")
data class WorkTaskModel(
  @Id
  @Column("task_id")
  override val taskId: String,
  override val taskName: String,
  override val message: String?,
  override val pass: Boolean
): WorkTask, Persistable<String> {
  override fun getId(): String? = taskId

  override fun isNew(): Boolean = true
}
