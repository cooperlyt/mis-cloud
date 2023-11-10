package io.github.cooperlyt.mis.work.create;


import io.github.cooperlyt.commons.cloud.keycloak.auth.KeycloakSecurityContext;
import io.github.cooperlyt.commons.cloud.keycloak.auth.ReactiveKeycloakSecurityContextHolder;

import io.github.cooperlyt.mis.work.Constant;
import io.github.cooperlyt.mis.work.WorkRemoteService;
import io.github.cooperlyt.mis.work.data.WorkAction;
import io.github.cooperlyt.mis.work.data.WorkDefine;
import io.github.cooperlyt.mis.work.data.WorkOperator;
import io.github.cooperlyt.mis.work.data.WorkOperatorSample;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Aspect
public class WorkCreateAspect implements ApplicationContextAware, Ordered {

  private ApplicationContext applicationContext;

  @Value("${mis.localization.organization:}")
  private String organization;

  @Override
  public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public int getOrder() {
    return Ordered.LOWEST_PRECEDENCE - 1;
  }

  private static class PrepareWorkDefine{

    @Builder
    public PrepareWorkDefine(WorkOperator operator,
                             WorkDefine define, Long workId,
                             String binding, boolean optional,
                             WorkCreate.OperatorWay operatorWay) {
      this.define = define;
      this.originalWorkId = workId;
      this.binding = binding;
      this.optional = optional;
      this.operatorWay = operatorWay;
      this.operator = operator;
    }

    private final WorkOperator operator;

    @Getter
    private final WorkDefine define;

    private final Long originalWorkId;

    @Getter
    private final String binding;

    @Getter
    private final boolean optional;

    @Getter
    private final WorkCreate.OperatorWay operatorWay;

    public Optional<Long> getWorkId() {
      return Optional.ofNullable(originalWorkId);
    }

    public boolean isEnabled() {
      return define.isEnabled();
    }

    public boolean isProcess(){
      return define.isProcess();
    }

    public String getDefineId(){
      return define.getDefineId();
    }

    public Optional<WorkOperator> getOperator(){
      return Optional.ofNullable(operator);
    }

  }
  private final WorkRemoteService workRemoteService;

  private final WorkOperatorPersistableHandler workPrepareCreateHandler;


  public WorkCreateAspect(WorkRemoteService workRemoteService,
                          WorkOperatorPersistableHandler workPrepareCreateHandler) {
    this.workRemoteService = workRemoteService;
    this.workPrepareCreateHandler = workPrepareCreateHandler;
  }

  private Class<?> getActuallyResultType(Type monoType){
    if (!(monoType instanceof ParameterizedType parameterizedType))
      return null;
    Type[] typeArguments = parameterizedType.getActualTypeArguments();
    assert typeArguments.length == 1;
    return (Class<?>) typeArguments[0];
  }

  @Around("@annotation(workCreateAnnotation)")
  public Object workCreateAction(ProceedingJoinPoint joinPoint, WorkCreate workCreateAnnotation) throws Throwable {

    WorkCreate workCreate = AnnotationUtils.getAnnotation(workCreateAnnotation,WorkCreate.class);
    assert workCreate != null : "work create annotation must be present";

    log.info("work create action: {} - work: {}", joinPoint.getSignature().getName(),workCreate.defineId());

    if (workCreate.resultIsWorkId() && WorkCreate.OperatorWay.FIRST.equals(workCreate.operatorWay())){
      throw new IllegalArgumentException("work create method resultIsWorkId can not be true when operator is FIRST");
    }

    if (StringUtils.isBlank(workCreate.defineId())){
      throw new IllegalArgumentException("work create method defineId must be present");
    }

    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

    if (!Mono.class.isAssignableFrom(methodSignature.getReturnType())) {
      throw new UnsupportedOperationException("work create method return type must be Mono");
    }

    Class<?> actualReturnType = getActuallyResultType(methodSignature.getReturnType());

    if (workCreate.resultIsWorkId() &&  !Mono.class.isAssignableFrom(methodSignature.getReturnType()) && (actualReturnType == null || !Number.class.isAssignableFrom(actualReturnType))){
      throw new UnsupportedOperationException("work create method return type must be Mono<Long> when resultIsWorkId is true");
    }

    Object[] args = joinPoint.getArgs();
    Parameter[] parameters = methodSignature.getMethod().getParameters();

    Optional<Long> workId = Optional.empty();
    Optional<WorkOperator> operator = Optional.empty();

    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      Object paramValue = args[i];
      if (parameter.isAnnotationPresent(WorkIdParam.class)){
        if (paramValue == null){
          throw new IllegalArgumentException("work id param can not be null");
        }
        if (parameter.getType().isPrimitive() || Number.class.isAssignableFrom(parameter.getType()))
          workId = Optional.of(((Number) paramValue).longValue());
        else
          throw new IllegalArgumentException("work id param type must be number or string");
      }
      if (WorkOperator.class.isAssignableFrom(parameter.getType())){
        if (paramValue == null){
          throw new IllegalArgumentException("work operator param can not be null");
        }
        operator = Optional.of((WorkOperator) paramValue);
      }
    }

    String binding = workCreate.binding();
    if (StringUtils.isBlank(binding)){
      WorkMessageBinding workMessageBinding = methodSignature.getMethod().getDeclaringClass().getAnnotation(WorkMessageBinding.class);
      if (workMessageBinding != null)
        workMessageBinding = AnnotationUtils.getAnnotation(workMessageBinding,WorkMessageBinding.class);
      if (workMessageBinding != null)
        binding = workMessageBinding.binding();
    }

    if (StringUtils.isBlank(binding)){
      throw new IllegalArgumentException("work message binding must be present");
    }

    return Mono.just(PrepareWorkDefine.builder()
            .optional(workCreate.optional()).operatorWay(workCreate.operatorWay())
            .binding(binding).workId(workId.orElse(null))
            .operator(operator.orElse(null)))
        .flatMap(builder -> Mono.justOrEmpty(builder.workId)
            .flatMap(id -> workRemoteService.define(workCreate.defineId())
                .map(builder::define)
            ).switchIfEmpty(workCreate.resultIsWorkId() ?
                workRemoteService.define(workCreate.defineId()).map(builder::define) :
                workRemoteService.prepareCreate(workCreate.defineId())
                    .map(pc -> builder.workId(pc.getWorkId()).define(pc))
            )
        )
        .switchIfEmpty(Mono.error(Constant.ErrorDefine.WORK_DEFINE_NOT_FOUND.exception()))
        .map(PrepareWorkDefine.PrepareWorkDefineBuilder::build)
        .filter(PrepareWorkDefine::isEnabled)
        .switchIfEmpty(Mono.error(Constant.ErrorDefine.WORK_IS_DISABLED.exception()))
        .flatMap(define ->  {
          log.info("work prepare complete , ready to create work : {}", joinPoint.getSignature().getName());
          try {
            Mono<?> result = Mono.just(define.operatorWay)
                .filter(WorkCreate.OperatorWay.FIRST::equals)
                .flatMap(way -> Mono.justOrEmpty(define.getOperator())
                    .flatMap(op -> persistEmployeeWorkOperator(define.getDefine(),define.getWorkId().orElseThrow(),op))
                    .switchIfEmpty(persistContextOperator(define.getDefine(),define.getWorkId().orElseThrow()))
                )
                .then(((Mono<?>) joinPoint.proceed(args)))
                .filter(r -> !define.isOptional() || !(r instanceof Boolean)  || ((Boolean) r))
                .flatMap(r -> Mono.defer(() -> createWork(define, r, define.binding).thenReturn(r)))
                .switchIfEmpty(define.isOptional() ? Mono.empty() : Mono.defer(() -> createWork(define, null, define.binding).then(Mono.empty())));

            if (define.getWorkId().isPresent() && !workCreate.resultIsWorkId()){
              result = result.contextWrite(ctx -> ctx.put(Constant.WORK_ID_PARAM,define.getWorkId().get()));
            }

            if (workCreate.transactionalRequired()){
              TransactionalOperator transactionalOperator = TransactionalOperator.create(applicationContext.getBean(ReactiveTransactionManager.class));
              return result.as(transactionalOperator::transactional);
            }
            return result;
          } catch (Throwable e) {
            log.error("work create error",e);
            return Mono.error(new RuntimeException(e));
          }
        });

  }

  private Mono<Void> persistEmployeeWorkOperator(WorkDefine define, long workId, WorkOperator operator){
    return workPrepareCreateHandler.persist(define,workId, WorkAction.ActionType.APPLY, operator);
  }

  private Mono<Void> persistContextOperator(WorkDefine define, long workId){
    return contextOperator()
        .flatMap(op -> workPrepareCreateHandler.persist(define,workId, WorkAction.ActionType.CREATE,op));
  }

  private Mono<WorkOperator> contextOperator(){
    return ReactiveKeycloakSecurityContextHolder.getContext()
        .filter(KeycloakSecurityContext::isAuthenticated)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)))
        .map(ReactiveKeycloakSecurityContextHolder.KeycloakUserSecurityContext::getUserInfo)
        .map(user -> WorkOperatorSample.builder()
            .userId(user.getUsername())
            .name(user.getName())
            .orgName(organization)
            .build());
  }

  @SuppressWarnings("unchecked")
  private Mono<Long> createWork(PrepareWorkDefine define,Object result, String bindingName){

    final long workId = define.getWorkId().orElseGet(() -> {
      if (result instanceof Number) {
        return  ((Number) result).longValue();
      }else if (result instanceof WorkIdResult) {
        return  ((WorkIdResult) result).getWorkId();
      }else{
        throw new UnsupportedOperationException("work create method return type must be Mono<Long> when resultIsWorkId is true");
      }
    });
    Mono<Long> prepare = Mono.just(define.operatorWay)
        .filter(WorkCreate.OperatorWay.LAST::equals)
        .flatMap(way -> Mono.justOrEmpty(define.getOperator())
            .flatMap(op -> persistEmployeeWorkOperator(define.getDefine(),workId,op).thenReturn(workId))
            .switchIfEmpty(persistContextOperator(define.define,workId).thenReturn(workId))
        )
        .defaultIfEmpty(workId);

    //define.getOrgId().map(orgId -> workPrepareCreateHandler.prepareCreate(workId,orgId,define.getEmpId()).thenReturn(workId)).orElse(prepareCreateForCurrentUser(workId));
    if (define.isProcess()){
      Map<String,Object> processData;
      log.debug("work create method return type: {}",result.getClass().getName());
      if (result instanceof Map){
        processData = (Map<String ,Object>) result;
      }else if (result instanceof ProcessWorkResult){
        processData = ((ProcessWorkResult) result).getVariables();
      }else {
        throw new UnsupportedOperationException("work create method return type must be Mono<Map<String,Object>> or Mono<ProcessWorkResult>");
      }
      return Mono.just(processData).flatMap(data -> prepare
              .flatMap(wid -> workRemoteService.sendWorkMessage(bindingName,define.getDefineId(),workId,data))
          );
    }
    return prepare;
  }
}
