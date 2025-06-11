package io.github.cooperlyt.mis.work.create

import io.github.cooperlyt.commons.data.PowerBody
import io.github.cooperlyt.mis.work.Constant
import io.github.cooperlyt.mis.work.ProcessConstant
import io.github.cooperlyt.mis.work.WorkRemoteService
import io.github.cooperlyt.mis.work.WorkService
import io.github.cooperlyt.mis.work.data.WorkDefineForCreate
import io.github.cooperlyt.mis.work.data.WorkOperatorSupplier
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.Ordered
import org.springframework.core.ResolvableType
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.reactive.TransactionalOperator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.util.*

//TODO 处理补录
//TODO 检查方法上是否有@Transation 注解进行更细的事务控制
/**
 * 事务是必要的，反射操作和方法中的操作必须是原子的
 */
@Aspect
class WorkAspect(private val workService: WorkService, private val workRemoteService: WorkRemoteService) : Ordered,
  ApplicationContextAware {
  companion object {

    private val logger = org.slf4j.LoggerFactory.getLogger(WorkAspect::class.java)
  }

  private class CreateActionContext(joinPoint: ProceedingJoinPoint, forCreate: Boolean = false) {

    val operator: Optional<WorkOperatorSupplier>

    val workId: Optional<Long>

    private val params: Optional<WorkCreateParams>

    val isMonoResult: Boolean

    val isFluxResult: Boolean

    val resultType: Class<*>

    val applicant: Optional<PowerBody>
      get() = params.flatMap { Optional.ofNullable(it.applicant) }

    val historyDateTime: Optional<LocalDateTime>
      get() = params.flatMap { Optional.ofNullable(it.historyDateTime) }

    fun createProcessVariables(original: Map<String, Any>): Map<String, Any> {

      return params.map {
        original + (ProcessConstant.NOTE_VAR to (it.note ?: ""))
      }
        .orElse(original)

    }

    init {
      val methodSignature = joinPoint.signature as MethodSignature

      if (forCreate) {
        var createParam: WorkCreateParams? = null

        var createParamIndex =
          methodSignature.method.parameters.indexOfFirst { WorkCreateParams::class.java.isAssignableFrom(it.type) }

        if (createParamIndex >= 0) {
          createParam = joinPoint.args[createParamIndex] as WorkCreateParams
        } else {
          createParamIndex =
            methodSignature.method.parameters.indexOfFirst { WorkCreateSupplierBasic::class.java.isAssignableFrom(it.type) }
          if (createParamIndex > 0) {
            createParam = WorkCreateParams.of(joinPoint.args[createParamIndex] as WorkCreateSupplierBasic)
          }
        }

        params = Optional.ofNullable(createParam)

        operator = createParam?.let { Optional.ofNullable(it.operator) } ?: Optional.empty()

        workId = createParam?.let { Optional.ofNullable(it.workId) } ?: Optional.empty()

      } else {
        params = Optional.empty()
        operator = Optional.empty()
        workId = Optional.empty()
      }

      isMonoResult = Mono::class.java.isAssignableFrom(methodSignature.returnType)

      isFluxResult = Flux::class.java.isAssignableFrom(methodSignature.returnType)

      resultType =
        if (isMonoResult || isFluxResult) {
          ResolvableType
            .forMethodReturnType(methodSignature.method)
            .getGeneric(0)
            .resolve()
        } else methodSignature.returnType

    }

  }

  //TODO 处理补录
  @Around("@annotation(workCreateAnnotation)")
  fun workCreateAction(joinPoint: ProceedingJoinPoint, workCreateAnnotation: WorkCreate): Any {

    logger.debug("business create aspect")

    val workCreate: WorkCreate =
      AnnotationUtils.getAnnotation(workCreateAnnotation, WorkCreate::class.java)!!

    val actionContext = CreateActionContext(joinPoint, true)

    val transactionalOperator = applicationContext?.let {
      TransactionalOperator.create(it.getBean(ReactiveTransactionManager::class.java))
    } ?: throw IllegalStateException("ApplicationContext or ReactiveTransactionManager not available")


    return Mono.justOrEmpty(actionContext.workId)
      .flatMap { workId ->
        workRemoteService.define(workCreate.defineId)
          .map {
            WorkDefineForCreate(
              workId,
              it
            )
          }
      }
      .switchIfEmpty(workRemoteService.prepareProcess(workCreate.defineId))
      .filter { it.isEnabled }
      .switchIfEmpty(Mono.error { Constant.ErrorDefine.WORK_IS_DISABLED.exception() })
      .filter { !it.isProcess || WorkCreateResult::class.java.isAssignableFrom(actionContext.resultType) }
      .switchIfEmpty(Mono.error { IllegalStateException("process work must return a WorkCreateResult!") })
      .flatMap { define ->
        workService.createWork(
          define,
          define.workId,
          workCreate.actionType,
          workCreate.dataSource,
          actionContext.operator.orElse(null),
          actionContext.applicant.orElse(null),
          actionContext.historyDateTime.orElse(null)
        )
          .then(createProceed(joinPoint, actionContext, define))
          .`as` { transactionalOperator.transactional(it) }
      }
      .`as` { result ->
        if (actionContext.isMonoResult) result else if (actionContext.isFluxResult) result.flatMapMany {
          Flux.fromIterable(
            it as List<*>
          )
        } else result.block()
      }
  }


  @Around("@annotation(workStatusChangeAnnotation)")
  fun workStatusChangeAction(
    joinPoint: ProceedingJoinPoint,
    workStatusChangeAnnotation: WorkStatusChange
  ): Any? {
    logger.debug("business status change aspect")

    val workStatusChange: WorkStatusChange =
      AnnotationUtils.getAnnotation(workStatusChangeAnnotation, WorkStatusChange::class.java)!!

    val actionContext = CreateActionContext(joinPoint)

    val transactionalOperator = applicationContext?.let {
      TransactionalOperator.create(it.getBean(ReactiveTransactionManager::class.java))
    } ?: throw IllegalStateException("ApplicationContext or ReactiveTransactionManager not available")

    val workId = getWorkIdBySpEL(joinPoint, workStatusChange.workId)

    val changeMono = workService.workStatusChange(workId, workStatusChange.status)
      .`as` { transactionalOperator.transactional(it) } // 状态变更在事务中

    return when {
      actionContext.isMonoResult -> changeMono
        .then(
          Mono.defer { joinPoint.proceed(joinPoint.args) as Mono<*> }
        )
        .`as` { transactionalOperator.transactional(it) }
        .doOnError { logger.error("Mono execution failed: ${it.message}", it) }

      actionContext.isFluxResult -> changeMono
        .thenMany(
          Flux.defer { joinPoint.proceed(joinPoint.args) as Flux<*> }
        )
        .`as` { transactionalOperator.transactional(it) }
        .doOnError { logger.error("Flux execution failed: ${it.message}", it) }

      else -> changeMono
        .then(
          Mono.fromCallable { joinPoint.proceed(joinPoint.args) }
        )
        .`as` { transactionalOperator.transactional(it) }
        .doOnError { logger.error("Blocking execution failed: ${it.message}", it) }
        .block()
    }
  }

  @Around("@annotation(workEventAnnotation)")
  fun workEventAction(
    joinPoint: ProceedingJoinPoint,
    workEventAnnotation: WorkEvent
  ): Any? {
    logger.debug("business event aspect")

    val workEvent: WorkEvent =
      AnnotationUtils.getAnnotation(workEventAnnotation, WorkEvent::class.java)!!

    val actionContext = CreateActionContext(joinPoint)

    val transactionalOperator = applicationContext?.let {
      TransactionalOperator.create(it.getBean(ReactiveTransactionManager::class.java))
    } ?: throw IllegalStateException("ApplicationContext or ReactiveTransactionManager not available")

    val workId = getWorkIdBySpEL(joinPoint, workEvent.workId)

    return proceed(joinPoint, actionContext)
      .map { Optional.ofNullable(it) }
      .defaultIfEmpty(Optional.empty())
      .flatMap { result ->
        workRemoteService.sendWorkEventMessage(
          workEvent.event,
          workEvent.defineId,
          workId,
          emptyMap()
        )
          .then(Mono.justOrEmpty(result))
      }
      .`as` { transactionalOperator.transactional(it) }
      .`as` { result ->
        if (actionContext.isMonoResult) result else if (actionContext.isFluxResult) result.flatMapMany {
          Flux.fromIterable(
            it as List<*>
          )
        } else result.block()
      }
  }

  //TODO 不结束工作流，直接重置数据 ，加一条操作日志
  @Deprecated("不结束工作流，直接重置数据")
  @Around("@annotation(workRecreateAnnotation)")
  fun workRecreateAction(joinPoint: ProceedingJoinPoint, workRecreateAnnotation: WorkRecreate): Any {
    logger.debug("business recreate aspect")

    val createAnnotation: WorkRecreate =
      AnnotationUtils.getAnnotation(workRecreateAnnotation, WorkRecreate::class.java)!!

    val transactionalOperator = TransactionalOperator.create(
      applicationContext!!.getBean(ReactiveTransactionManager::class.java)
    )

    val workId = getWorkIdBySpEL(joinPoint, createAnnotation.workId)
    val actionContext = CreateActionContext(joinPoint)


    return workService.workInfo(workId)
      .flatMap { workInfo ->
        workRemoteService.prepareProcess(workInfo.defineId)
          .filter { it.isEnabled }
          .switchIfEmpty(Mono.error { Constant.ErrorDefine.WORK_IS_DISABLED.exception() })
          .filter { !it.isProcess || WorkCreateResult::class.java.isAssignableFrom(actionContext.resultType) }
          .switchIfEmpty(Mono.error { IllegalStateException("process work must return a WorkCreateResult!") })
          .flatMap { define ->


            workService.createWork(
              define,
              workId,
              createAnnotation.actionType,
              workInfo.dataSource
            )
              .then(workService.cloneWorkApplicant(define.workId, workId))
              .then(
                createProceed(joinPoint, actionContext, define)
              )
              .`as` { transactionalOperator.transactional(it) }
          }
      }
      .`as` { result ->
        if (actionContext.isMonoResult) result else if (actionContext.isFluxResult) result.flatMapMany {
          Flux.fromIterable(
            it as List<*>
          )
        } else result.block()
      }

//    return if (actionContext.isMonoResult)
//      result
//    else
//      result.block()
  }

  private fun proceed(joinPoint: ProceedingJoinPoint, actionContext: CreateActionContext): Mono<out Any> {
    return if (actionContext.isMonoResult) {
      Mono.defer { (joinPoint.proceed(joinPoint.args) as Mono<*>) }
    } else if (actionContext.isFluxResult) {
      Mono.defer {
        (joinPoint.proceed(joinPoint.args) as Flux<*>).collectList()
      }
    } else {
      Mono.fromCallable { joinPoint.proceed(joinPoint.args) }
    }
  }


  private fun createProceed(
    joinPoint: ProceedingJoinPoint,
    actionContext: CreateActionContext,
    define: WorkDefineForCreate
  ): Mono<out Any> {
    return proceed(joinPoint, actionContext)
      .contextWrite { it.put(Constant.WORK_ID_PARAM, define.workId) }
      .map { Optional.ofNullable(it) }
      .defaultIfEmpty(Optional.empty())
      .flatMap { result ->
        Mono.justOrEmpty(result.orElse(null) as? WorkCreateResult)
          .flatMap {
            workRemoteService.sendWorkCreateMessage(
              define.defineId,
              define.workId,
              define.tags,
              define.isProcess,
              it!!.documentation,
              actionContext.createProcessVariables(it.variables)
            )
          }
          .switchIfEmpty(
            workRemoteService.sendWorkCreateMessage(
              define.defineId,
              define.workId,
              define.tags,
            )
          )
          .then(Mono.justOrEmpty(result))
      }
  }


  // 判断字符串是否为 SpEL 表达式
  private fun isSpEL(expression: String): Boolean {
    // 简单的检查：SpEL 表达式通常包含 # 或 ${}
    return expression.contains("#") || expression.contains("\${")
  }


  private fun getWorkIdBySpEL(joinPoint: ProceedingJoinPoint, workIdSpEL: String): Long {
    val args = joinPoint.args
    val methodSignature = joinPoint.signature as MethodSignature
    val parameterNames = methodSignature.parameterNames

    val context = StandardEvaluationContext()
    parameterNames.forEachIndexed { index, name ->
      context.setVariable(name, args[index])
    }

    // 解析 workId 的 SpEL 表达式
    val parser = SpelExpressionParser()
    return parser.parseExpression(workIdSpEL).getValue(context, Long::class.java)
      ?: throw IllegalStateException("Failed to parse workId expression: $workIdSpEL")
  }

//  private fun saveWork(
//    define: WorkDefine,
//    workId: Long,
//    actionType: WorkActionType,
//    dataSource: String,
//    isPrepared: Boolean,
//    operator: WorkOperator? = null
//  ): Mono<Void> {
//
//    return if (isPrepared)
//      workService.runWork(workId, actionType, operator)
//    else
//      workService.createWork(define, workId, actionType, dataSource, operator)
//
//  }


  private var applicationContext: ApplicationContext? = null

  override fun setApplicationContext(applicationContext: ApplicationContext) {
    this.applicationContext = applicationContext
  }

  override fun getOrder(): Int {
    return Ordered.LOWEST_PRECEDENCE - 1
  }
}