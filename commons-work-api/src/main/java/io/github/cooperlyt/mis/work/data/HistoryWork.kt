package io.github.cooperlyt.mis.work.data

import com.fasterxml.jackson.annotation.JsonRawValue
import io.github.cooperlyt.mis.work.message.WorkStatus
import java.time.LocalDateTime

data class HistoryWork(
  override val workId: Long,
  override val dataSource: String,
  override val createdAt: LocalDateTime?,
  override val updatedAt: LocalDateTime?,
  override val completedAt: LocalDateTime?,
  override val validatedAt: LocalDateTime?,
  override val historyAt: LocalDateTime?,
  override val workName: String,
  override val status: WorkStatus,
  override val type: String?,
  override val process: Boolean,
  override val defineId: String,
  val category: Category,
  val workType: WorkOperateType?,
  @JsonRawValue
  val operators: String? = null,
//  val infoId: Long,
  val beforeInfoId: Long? ,
  @JsonRawValue
  val description: String? = null

): WorkInfo {

  enum class Category {
    PROJECT,
    BUILD,
    HOUSE,

    CORP,

    CORP_RECORD,
  }


}
