package io.github.cooperlyt.mis.dictionary.serialize

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.cooperlyt.mis.dictionary.DistrictName
import io.github.cooperlyt.mis.work.impl.model.WorkActionsView
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.github.cooperlyt.commons.data.JsonReading

@ReadingConverter
class DistrictReadingConverter(objectMapper: ObjectMapper): JsonReading<DistrictName>(objectMapper) {

  override fun convert(source: String): DistrictName {
    return DistrictName(objectMapper.readValue(source, jacksonTypeRef<List<String>>()))
  }

}