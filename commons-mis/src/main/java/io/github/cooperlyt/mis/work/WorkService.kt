package io.github.cooperlyt.mis.work

import io.github.cooperlyt.commons.data.PowerBody
import io.github.cooperlyt.mis.work.data.WorkAction
import io.github.cooperlyt.mis.work.data.WorkActionBasic
import io.github.cooperlyt.mis.work.data.WorkActionType
import io.github.cooperlyt.mis.work.data.WorkDefine
import io.github.cooperlyt.mis.work.data.WorkInfo
import io.github.cooperlyt.mis.work.data.WorkOperatorSupplier
import io.github.cooperlyt.mis.work.message.WorkStageChangedMessage
import io.github.cooperlyt.mis.work.message.WorkStatus
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface WorkService {

    fun workTasks(workId: Long): Mono<List<WorkAction>>

    fun workRejectTasks(workId: Long): Mono<List<WorkAction>>

    fun workInfo(workId: Long): Mono<WorkInfo>

    fun createWorkApplicant(workId: Long, applicant: PowerBody): Mono<Long>

    fun updateWorkApplicant(workId: Long, applicant: PowerBody): Mono<Long>

    fun cloneWorkApplicant(workId: Long, originalWorkId: Long): Mono<Void>

    fun getWorkApplicant(workId: Long): Mono<PowerBody>

    fun getWorkCreateOperator(workId: Long): Mono<WorkActionBasic>

    fun workStatusChange(workId: Long, status: WorkStatus): Mono<Void>

    fun workChange(message: WorkStageChangedMessage): Mono<Void>

//    @Deprecated("不要 prepare , 仅使用work id 上传图片就好")
//    fun prepareWork(work: WorkDefineForCreate): Mono<Long>

    fun createWork(
      define: WorkDefine, workId: Long,
      type: WorkActionType, dataSource: String, operator: WorkOperatorSupplier? = null,
      applicant: PowerBody? = null, historyDateTime: LocalDateTime? = null
    ): Mono<Void>

//    fun runWork(workId: Long, type: WorkActionType, operator: WorkOperator? = null): Mono<Void>

    //--------------- remote


  fun workActionBasic(workId: Long): Flux<WorkActionBasic>
}