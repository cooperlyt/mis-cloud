package io.github.cooperlyt.mis.work.message

data class WorkStatusChangedMessage(
  val status: WorkStatus,

  val workId: Long ,

  val define: String,
)