package io.github.cooperlyt.mis.work.converter

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.cooperlyt.mis.work.impl.model.WorkActionsView
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter

@ReadingConverter
class WorkActionsConverter(private val objectMapper: ObjectMapper) : Converter<String, WorkActionsView> {

  companion object {
    private val logger = org.slf4j.LoggerFactory.getLogger(WorkActionsConverter::class.java)
  }

  override fun convert(source: String): WorkActionsView? {
    logger.debug("WorkActionsConverter: {}", source)

    //val decodedString = URLDecoder.decode(source, StandardCharsets.UTF_8)
    try {
      return objectMapper.readValue(source, WorkActionsView::class.java)
    } catch (e: JsonProcessingException) {
      logger.error("WorkOperatorHeaderConverter error: {}", e.message)
      throw RuntimeException(e)
    }
  }
}