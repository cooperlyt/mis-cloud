package io.github.cooperlyt.commons.data

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter
import org.springframework.util.StringUtils
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class JsonConverter<S: Any,T>(
  objectMapper: ObjectMapper? = null
): Converter<S, T> {

  protected val objectMapper: ObjectMapper = objectMapper ?: ObjectMapper()

}

abstract class JsonWriting<S : Any>(objectMapper: ObjectMapper? = null) : JsonConverter<S, String>(objectMapper) {

  override fun convert(source: S): String? {
    return objectMapper.writeValueAsString(source)
  }
}


abstract class JsonReading<T> : JsonConverter<String, T> {

  companion object {
    private val logger = LoggerFactory.getLogger(JsonReading::class.java)
  }

  private val typeReference: TypeReference<out T>

  private fun determineType(): TypeReference<out T> {
    // 获取泛型类型参数T的实际类型
    //TypeFactory typeFactory = objectMapper.getTypeFactory();

    val superClass = this.javaClass.getGenericSuperclass()
    val valueType = (superClass as ParameterizedType).actualTypeArguments[0]
    return object : TypeReference<T>() {
      override fun getType(): Type {
        return valueType
      }
    }
  }

  constructor(objectMapper: ObjectMapper? = null) : super(objectMapper) {
    this.typeReference = determineType()
  }

  constructor(typeReference: TypeReference<out T>, objectMapper: ObjectMapper? = null) : super(objectMapper) {
    this.typeReference = typeReference
  }

  override fun convert(source: String): T? {
    logger.debug("convert json: {}", source)
    if (!StringUtils.hasText(source)) {
      return null
    }

    try {
      return objectMapper.readValue(source, typeReference)
    } catch (e: JsonProcessingException) {
      throw RuntimeException(e)
    }
  }
}

