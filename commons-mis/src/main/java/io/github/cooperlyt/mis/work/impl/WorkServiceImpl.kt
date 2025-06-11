package io.github.cooperlyt.mis.work.impl

import io.github.cooperlyt.commons.cloud.keycloak.auth.ReactiveKeycloakSecurityContextHolder
import io.github.cooperlyt.commons.data.PowerBody
import io.github.cooperlyt.mis.work.Constant
import io.github.cooperlyt.mis.work.WorkService
import io.github.cooperlyt.mis.work.data.WorkActionType
import io.github.cooperlyt.mis.work.data.WorkAction
import io.github.cooperlyt.mis.work.data.WorkActionBasic
import io.github.cooperlyt.mis.work.data.WorkDefine
import io.github.cooperlyt.mis.work.data.WorkInfo
import io.github.cooperlyt.mis.work.data.WorkOperatorBasic
import io.github.cooperlyt.mis.work.data.WorkOperatorSupplier
import io.github.cooperlyt.mis.work.impl.model.WorkOperatorModel
import io.github.cooperlyt.mis.work.impl.model.WorkApplicantModel
import io.github.cooperlyt.mis.work.impl.model.WorkModel
import io.github.cooperlyt.mis.work.impl.model.WorkTaskModel
import io.github.cooperlyt.mis.work.impl.repositories.WorkApplicantRepository
import io.github.cooperlyt.mis.work.impl.repositories.WorkOperatorRepository
import io.github.cooperlyt.mis.work.impl.repositories.WorkRepository
import io.github.cooperlyt.mis.work.impl.repositories.WorkTaskRepository
import io.github.cooperlyt.mis.work.message.WorkStageChangedMessage
import io.github.cooperlyt.mis.work.message.WorkStatus
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.Comparator
import java.util.EnumSet
import java.util.Locale
import java.util.UUID
import java.util.function.Predicate

open class WorkServiceImpl(
  private val workOperatorRepository: WorkOperatorRepository,
  private val workTaskRepository: WorkTaskRepository,
  private val workApplicantRepository: WorkApplicantRepository,
  private val workRepository: WorkRepository,
  private val organization: String
) : WorkService {

  companion object {
    private val logger = org.slf4j.LoggerFactory.getLogger(WorkServiceImpl::class.java)
  }

  override fun workTasks(workId: Long): Mono<List<WorkAction>> {
    return workOperatorRepository.workActions(workId)
      .cast(WorkAction::class.java)
      .collectSortedList(Comparator.comparing { obj: WorkAction -> obj.workTime }.reversed())
  }

  override fun workRejectTasks(workId: Long): Mono<List<WorkAction>> {
    return workOperatorRepository.workRejectActions(workId)
      .cast(WorkAction::class.java)
      .collectSortedList(Comparator.comparing { it.workTime })
  }

  override fun workInfo(workId: Long): Mono<WorkInfo> {
    return workRepository.findById(workId)
      .switchIfEmpty(Mono.error(Constant.ErrorDefine.WORK_NOT_EXISTS.exception()))
      .cast(WorkInfo::class.java)
  }

  override fun createWorkApplicant(workId: Long, applicant: PowerBody): Mono<Long> {
    return workApplicantRepository.save(WorkApplicantModel(workId , applicant,true ))
      .map { it.workId }
  }

  override fun updateWorkApplicant(workId: Long, applicant: PowerBody): Mono<Long> {
    return workApplicantRepository.save(WorkApplicantModel(workId , applicant, false))
      .map { it.workId }
  }

  override fun cloneWorkApplicant(
    workId: Long,
    originalWorkId: Long
  ): Mono<Void> {
    return workApplicantRepository.cloneApplicant(workId, originalWorkId)
  }

  override fun getWorkApplicant(workId: Long): Mono<PowerBody> {
    return workApplicantRepository.findById(workId).cast(PowerBody::class.java)
  }

  override fun getWorkCreateOperator(workId: Long): Mono<WorkActionBasic> {
    return workOperatorRepository
      .findFirstByWorkIdAndTypeIn(
        workId,
        EnumSet.of(WorkActionType.CREATE, WorkActionType.APPLY)
      )
      .cast(WorkActionBasic::class.java)
  }

  @Transactional
  override fun workStatusChange(workId: Long, status: WorkStatus): Mono<Void> {
    return workRepository.findById(workId)
      .switchIfEmpty(Mono.error(Constant.ErrorDefine.WORK_NOT_EXISTS.exception()))
      .filter{ it.status != status }
      .doOnNext{ work ->
        work.status = status
        if (status.valid) {
          work.validatedAt = LocalDateTime.now()
          if (WorkStatus.COMPLETED == status) {
            work.completedAt = LocalDateTime.now()
          }
        }
      }
      .flatMap { workRepository.save(it) }
      .then()
  }

  //TODO Fill employee_info_id ANd corp_info_id
  //@Transactional
  override fun workChange(message: WorkStageChangedMessage): Mono<Void> {
    return workOperatorRepository.save(
      WorkOperatorModel(
        UUID.randomUUID().toString().replace("-", "").lowercase(Locale.getDefault()),
        message.workId,
        message.userId,
        message.userName,
        WorkActionType.TASK,
        LocalDateTime.now(),
        null,
        null,
        null
      )
    )
      .flatMap { operator ->
        workTaskRepository.save(
          WorkTaskModel(
            operator.taskId,
            message.taskName,
            message.message,
            message.isPass
          )
        )
      }
      .then()
  }

//  @Transactional
//  override fun prepareWork(work: WorkDefineForCreate): Mono<Long> {
//    return workRepository.save(
//      WorkModel.builder()
//        .define(work)
//        .workId(work.workId)
//        .dataSource(WorkInfo.SOURCE_FROM_OFFICE)
//        .status(WorkStatus.PREPARE)
//        .build()
//    )
//      .thenReturn(work.workId)
//  }


  private fun saveWorkOperator(
    workId: Long,
    type: WorkActionType,
    operator: WorkOperatorSupplier?
  ): Mono<WorkOperatorModel> {
    return Mono.justOrEmpty(operator)
      .switchIfEmpty(contextOperator())
      .map {
        WorkOperatorModel(
          workId.toString(),
          workId,
          type,
          it!!
        )
      }
      .flatMap { workOperatorRepository.save(it) }
  }

//    @Value("\${mis.localization.organization:}")
//    private val organization: String? = null


  //TODO gov corp_info_id
  private fun contextOperator(): Mono<WorkOperatorSupplier> {
    return ReactiveKeycloakSecurityContextHolder.getContext()
      .filter{ it.isAuthenticated }
      .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.UNAUTHORIZED)))
      .map { it.userInfo }
      .map { user ->
        WorkOperatorSupplier.Sample(
          user.username,
          user.name,
          organization,
          null,
          null
        )
      }
  }

  // @Transactional
  override fun createWork(
    define: WorkDefine, workId: Long,
    type: WorkActionType, dataSource: String, operator: WorkOperatorSupplier?,
    applicant: PowerBody?, historyDateTime: LocalDateTime?
  ): Mono<Void> {
    return workRepository.save(
      WorkModel(
        define,
        workId,
        if (define.isProcess) WorkStatus.RUNNING else WorkStatus.COMPLETED,
        dataSource,
        historyDateTime
      )
    )
      .flatMap { work ->
        Mono.justOrEmpty(operator)
          .switchIfEmpty(contextOperator())
          .map {
            WorkOperatorModel(
              work.workId.toString(),
              work.workId,
              type,
              it!!
            )
          }
          .flatMap { workOperatorRepository.save(it) }
      }
      .then(Mono.justOrEmpty(applicant))
      .flatMap { createWorkApplicant(workId, it!!) }
      .then()
  }


//  override fun runWork(workId: Long, type: WorkActionType, operator: WorkOperator?): Mono<Void> {
//    return workRepository.updateWorkStatus(workId, WorkStatus.RUNNING)
//      .filter(Predicate { count: Long? -> count!! > 0 })
//      .switchIfEmpty(Mono.error(Constant.ErrorDefine.WORK_NOT_EXISTS.exception()))
//      .then(saveWorkOperator(workId, type, operator))
//      .then()
//  }


}