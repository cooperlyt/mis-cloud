package io.github.cooperlyt.commons.data

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter


class StringList(
  source: String?,
): ListDelegation<String> {

  private val delimiters: String = ","

  override val list: List<String> = source?.split(delimiters) ?: emptyList()

  override fun toString(): String {
    return this.joinToString(separator = delimiters)
  }

}

@ReadingConverter
class StringListReadingConverter: Converter<String, StringList> {

  override fun convert(source: String): StringList? {
    return StringList(source)
  }

}

@WritingConverter
class StringListWritingConverter: Converter<StringList, String> {

  override fun convert(source: StringList): String? {
    return source.toString()
  }

}