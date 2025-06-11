package io.github.cooperlyt.mis.work.data

import io.github.cooperlyt.mis.work.message.WorkStatus
import java.time.LocalDateTime

interface WorkInfo: WorkIdentify {

  companion object {
    @Deprecated("")
    const val SOURCE_FROM_SYSTEM: String = "SYS"

    const val SOURCE_FROM_JOINT: String = "JOINT"

    const val SOURCE_FROM_OFFICE: String = "OFFICE"

    const val SOURCE_FROM_PATCH: String = "PATCH"
  }

  val dataSource: String

  val createdAt: LocalDateTime?

  val updatedAt: LocalDateTime?

  val completedAt: LocalDateTime?

  val validatedAt: LocalDateTime?

  val historyAt: LocalDateTime?

  val workName: String

  val status: WorkStatus

  val type: String?

  val process: Boolean

  val defineId: String

  val history: Boolean
    get() = historyAt != null
}