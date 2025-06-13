package io.github.cooperlyt.mis.dictionary.serialize

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.cooperlyt.commons.data.JsonWriting
import io.github.cooperlyt.mis.dictionary.DistrictName
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class DistrictWritingConverter(objectMapper: ObjectMapper): JsonWriting<DistrictName>(objectMapper)