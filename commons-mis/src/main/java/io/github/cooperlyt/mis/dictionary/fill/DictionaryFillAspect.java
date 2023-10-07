package io.github.cooperlyt.mis.dictionary.fill;

import io.github.cooperlyt.mis.ErrorDefine;
import io.github.cooperlyt.mis.dictionary.DictionaryRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Slf4j
@Aspect
public class DictionaryFillAspect {

  private final String[] excludePackages = new String[]{
      "java.",
      "javax.",
      "org.springframework.",
      "org.apache.",
      "org.hibernate.",
      "org.slf4j.",
      "org.apache.logging.",
      "org.aspectj."};

  private final DictionaryRemoteService dictionaryRemoteService;

  public DictionaryFillAspect(DictionaryRemoteService dictionaryRemoteService) {
    this.dictionaryRemoteService = dictionaryRemoteService;
  }

  private boolean isExclude(Class<?> clazz){
    if (Iterable.class.isAssignableFrom(clazz) || clazz.isArray() || Map.class.isAssignableFrom(clazz)){
      return false;
    }
    return clazz.isPrimitive() || clazz.isEnum() || Arrays.stream(excludePackages).anyMatch(p -> clazz.getPackageName().startsWith(p));
  }

  private String getTargetName(Method source, Dictionary dictionary){

    String result = dictionary.targetName();
    if (StringUtils.isBlank(result)){
      assert source.getName().startsWith("get");
      result = source.getName().replaceFirst("get","");

      if (StringUtils.isBlank(dictionary.targetPrefix()) && dictionary.targetIsField()){
        result = StringUtils.uncapitalize(result);
      }
      result = dictionary.targetPrefix() + result + StringUtils.capitalize(dictionary.targetSuffix());
    }


    if (!dictionary.targetIsField() && !result.startsWith("set")){
      result = StringUtils.capitalize(result);
      result =  "set" + result;
    }

    return result;
  }

  private String getTargetName(Field source, Dictionary dictionary){
    String result = dictionary.targetName();
    if (StringUtils.isBlank(result)) {
      result = source.getName();
      if (StringUtils.isNotBlank(dictionary.targetPrefix())){
        result = StringUtils.capitalize(result);
      }
      result = dictionary.targetPrefix() + result + StringUtils.capitalize(dictionary.targetSuffix());
    }

    if (!dictionary.targetIsField() && !result.startsWith("set")){
      result = StringUtils.capitalize(result);
      result =  "set" + result;
    }else {
      result = StringUtils.uncapitalize(result);
    }
    return result;
  }

  private Mono<String> getDictionaryLabel(String category, Number key, Dictionary.NotFoundAction notFoundAction){
    //return Mono.just("test");
    return dictionaryRemoteService.dictionaryLabel(category, key.intValue())
        .switchIfEmpty(Mono.defer(() -> switch (notFoundAction) {
          case NULL -> Mono.empty();
          case BLANK -> Mono.just("");
          case KEY -> Mono.just(key.toString());
          default ->
              Mono.error(ErrorDefine.DICTIONARY_VALUE_INVALID.exception(new String[]{category, key.toString()}));
        }));
  }

  private void assignValue(Class<?> targetClass, Object target,
                           String targetName, boolean isField, String label){
    try {
      if (isField) {
        Field targetField = targetClass.getDeclaredField(targetName);
        targetField.setAccessible(true);
        if (!targetField.getType().isAssignableFrom(String.class))
          throw new IllegalArgumentException("target field type must be String");
        targetField.set(target, label);
      }else{
        Method targetMethod = targetClass.getDeclaredMethod(targetName, String.class);
        targetMethod.setAccessible(true);
        if (!(targetMethod.getParameterCount() == 1) && !targetMethod.getParameterTypes()[0].isAssignableFrom(String.class))
          throw new IllegalArgumentException("target method parameter type must be String");
        targetMethod.invoke(target, label);
      }
      log.info("{} -> {} fill dictionary for target: {} to {}", targetClass.getSimpleName(), targetName, targetName, label);
    } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }

  }

  private Mono<Void> fillField(Field field, Object target,Set<Object> visited) {
    Dictionary dictionary = AnnotationUtils.getAnnotation(field, Dictionary.class);
    if (dictionary != null){
      try{
        if (Number.class.isAssignableFrom(field.getType()) || field.getType().isPrimitive()){
          field.setAccessible(true);
          Number key = (Number) field.get(target);
          if (key == null){
            if (dictionary.optional()){
              return Mono.empty();
            }else{
              return Mono.error(ErrorDefine.DICTIONARY_FIELD_REQUIRED.exception(new String[]{field.getName()}));
            }
          }
           return getDictionaryLabel(dictionary.category(), key.intValue(), dictionary.notFoundAction())
              .flatMap(label -> Mono.fromCallable(() -> {assignValue(field.getDeclaringClass(), target, getTargetName(field, dictionary), dictionary.targetIsField(), label);return null;}) );
        }else {
          log.error("{} -> {} is not Number", field.getDeclaringClass().getSimpleName(), field.getName());
          throw new IllegalArgumentException("field type must be Number");
        }
      }catch (IllegalAccessException e) {
        log.error("{} -> {} is not accessible", field.getDeclaringClass().getSimpleName(), field.getName());
        throw new RuntimeException(e);
      }
    }else if (!isExclude(field.getType())) {
      field.setAccessible(true);
      try{
        return fillDictionary(field.get(target), visited);
      }catch (IllegalAccessException e){
        return Mono.error(e);
      }

    }else{
      return Mono.empty();
    }
  }

  private Mono<Void> fillMethod(Method method, Object target, Set<Object> visited){
    Dictionary dictionary = AnnotationUtils.getAnnotation(method, Dictionary.class);
    if (dictionary != null){
      try{
        Method sourceMethod = null;
        if(method.getName().startsWith("get") || method.getParameterCount() == 0){
          sourceMethod = method;
        }else if (method.getName().startsWith("set") || method.getParameterCount() == 1){
          sourceMethod = method.getDeclaringClass()
              .getDeclaredMethod(method.getName().replaceFirst("set","get"),
                  method.getParameterTypes());
        }
        if (sourceMethod == null){
          log.error("{} -> {} is not getter or setter", target.getClass().getSimpleName(), method.getName());
          throw new IllegalArgumentException("method must be getter or setter");
        }


        if (Number.class.isAssignableFrom(method.getReturnType()) || method.getReturnType().isPrimitive()){
          method.setAccessible(true);
          Number key = (Number) method.invoke(target);
          if (key == null){
            if (dictionary.optional()){
              return Mono.empty();
            }else{
              return Mono.error(ErrorDefine.DICTIONARY_FIELD_REQUIRED.exception(new String[]{method.getName()}));
            }
          }

          return Mono.just(sourceMethod)
                  .flatMap(src -> getDictionaryLabel(dictionary.category(),key.intValue(), dictionary.notFoundAction())
                      .flatMap(label -> Mono.fromCallable(() -> {assignValue(method.getDeclaringClass(), target, getTargetName(src, dictionary), dictionary.targetIsField(), label); return null;}) )
                  );

        }else {
          log.error("{} -> {} is not Number", target.getClass().getSimpleName(), method.getName());
          throw new IllegalArgumentException("field type must be Number");
        }

      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }


    }else if (!isExclude(method.getReturnType())) {
      method.setAccessible(true);
      if (!method.getName().startsWith("get") || method.getParameterCount() > 0)
        return Mono.empty();
      try {
        return fillDictionary(method.invoke(target), visited);
      } catch (IllegalAccessException | InvocationTargetException e) {
        log.error("{} -> {} is not accessible", target.getClass().getSimpleName(), method.getName());
        return Mono.error(e);
      }
    }else {
      return Mono.empty();
    }
  }

  private Mono<Void> fillDictionary(Object target, Set<Object> visited){

    log.debug("Prepare fill dictionary for target: {}", target);

    if (target == null){
      return Mono.empty();
    }

    if (Iterable.class.isAssignableFrom(target.getClass())){
      return Flux.fromIterable((Iterable<?>) target)
          .flatMap(t -> fillDictionary(t, visited))
          .then();
    }

    if (target.getClass().isArray()){
      return Flux.fromArray((Object[]) target)
          .flatMap(t -> fillDictionary(t, visited))
          .then();
    }


    if (target instanceof Map){
      return Flux.fromIterable(((Map<?,?>) target).entrySet())
          .flatMap(entry -> fillDictionary(entry.getValue(), visited))
          .then();
    }

    if (isExclude(target.getClass())){
      return Mono.empty();
    }

    if (visited.contains(target)){
      return Mono.empty();
    }

    visited.add(target);

    List<Method> methods = new ArrayList<>();
    List<Field> fields = new ArrayList<>();

    Class<?> tempClass = target.getClass();

    while (tempClass != null && !isExclude(tempClass)) {//当父类为null的时候说明到达了最上层的父类(Object类).
      Arrays.stream(tempClass.getDeclaredFields()).forEach(field -> {
        if (field.isAnnotationPresent(Dictionary.class) ||
            !isExclude(field.getType())) {
          fields.add(field);
        }
      });

      Arrays.stream(tempClass.getDeclaredMethods()).forEach(method -> {
        if (method.isAnnotationPresent(Dictionary.class) ||
            (method.getName().startsWith("get") &&
                method.getParameterCount() == 0) &&
                !isExclude(method.getReturnType())) {
          methods.add(method);
        }
      });
      tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
    }

    return Flux.fromIterable(fields)
        .flatMap(field -> Mono.defer(() -> fillField(field, target, visited)))
        .thenMany(Flux.fromIterable(methods)
            .flatMap(method -> Mono.defer(() -> fillMethod(method, target, visited))))
        .then();
  }

  @Around("@annotation(requestMapping) && execution(public * *(..,@org.springframework.web.bind.annotation.RequestBody (*),..))")
  public Object fillDictionary(ProceedingJoinPoint joinPoint, RequestMapping requestMapping) throws Throwable {

    var allow = new RequestMethod[]{RequestMethod.PUT, RequestMethod.POST, RequestMethod.PATCH };
    if (Arrays.stream(requestMapping.method()).noneMatch(m ->Arrays.asList(allow).contains(m))) {
      return joinPoint.proceed(joinPoint.getArgs());
    }

    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

    Parameter[] parameters = methodSignature.getMethod().getParameters();
    Object[] originalArgs = joinPoint.getArgs();

    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      Object paramValue = originalArgs[i];

      if (paramValue != null && parameter.isAnnotationPresent(RequestBody.class)) {
        log.info("{} -> {}() Prepare fill dictionary for parameter: {}",joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName(), parameter.getName());
        if (Mono.class.isAssignableFrom(methodSignature.getReturnType())){
          return fillDictionary(paramValue,new HashSet<>())
                    .then(Mono.defer(() -> {
                          log.info("dictionary fill complete, proceed method: {}", joinPoint.getSignature().getName());
                          try {
                            return (Mono<?>) joinPoint.proceed(originalArgs);
                          } catch (Throwable throwable) {
                            return Mono.error(throwable);
                          }
                    }));
        }else if (Flux.class.isAssignableFrom(methodSignature.getReturnType())){
          return fillDictionary(paramValue,new HashSet<>())
              .thenMany(Flux.defer(() -> {
                    log.info("dictionary fill complete, proceed method: {}", joinPoint.getSignature().getName());
                    try {
                      return (Flux<?>) joinPoint.proceed(originalArgs);
                    } catch (Throwable throwable) {
                      log.error("proceed method error", throwable);
                      return Flux.error(throwable);
                    }
                  }
              ));
        }else{
          fillDictionary(paramValue,new HashSet<>()).block();
        }
      }
    }


    return joinPoint.proceed(originalArgs);
  }
}
