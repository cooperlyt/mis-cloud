package io.github.cooperlyt.mis.work.data

import java.time.LocalDateTime

enum class WorkActionType(val label: String) {
  CREATE("录入"),
  TASK(""),
  APPLY("申请")
}


interface WorkTaskBase {
  val taskName: String?
}

interface WorkTask: WorkTaskBase {

  val taskId: String

  val message: String?

  val pass: Boolean?

}


interface WorkOperatorBase {

  val userId: String

  val userName: String

  val orgName: String?

}

interface WorkOperatorBasic: WorkOperatorBase {

  val type: WorkActionType

  val workTime: LocalDateTime

}

interface WorkOperatorSupplier: WorkOperatorBase {

  val corpInfoId: Long?

  val employeeInfoId: Long?

  data class Sample(
    override val userId: String,
    override val userName: String,
    override val orgName: String? = null,
    override val corpInfoId: Long? = null,
    override val employeeInfoId: Long? = null
  ): WorkOperatorSupplier
}

interface WorkOperator: WorkOperatorSupplier, WorkOperatorBasic {

}

interface WorkActionBasic: WorkOperatorBasic, WorkTask {

  data class Sample(
    override val type: WorkActionType,
    override val workTime: LocalDateTime,
    override val userId: String,
    override val userName: String,
    override val orgName: String?,
    override val taskId: String,
    override val message: String?,
    override val pass: Boolean?,
    override val taskName: String?
  ): WorkActionBasic
}

interface WorkAction: WorkActionBasic, WorkOperator {

  data class Sample(
    override val type: WorkActionType,
    override val workTime: LocalDateTime,
    override val userId: String,
    override val userName: String,
    override val orgName: String?,
    override val taskId: String,
    override val taskName: String?,
    override val message: String?,
    override val pass: Boolean?,
    override val corpInfoId: Long?,
    override val employeeInfoId: Long?
  ): WorkAction
}


interface WorkActionSummary: WorkOperatorBasic, WorkTaskBase {

  val actionName: String
    get() = if (type == WorkActionType.TASK) taskName!! else type.label

  data class Sample(
    override val type: WorkActionType,
    override val workTime: LocalDateTime,
    override val userId: String,
    override val userName: String,
    override val orgName: String?,
    override val taskName: String?
  ): WorkActionSummary

}

interface WorkSummary: WorkIdentify {

  val workName: String

  val actions: List<WorkActionSummary>


}