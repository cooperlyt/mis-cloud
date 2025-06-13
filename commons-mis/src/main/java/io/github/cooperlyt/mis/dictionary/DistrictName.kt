package io.github.cooperlyt.mis.dictionary

import io.github.cooperlyt.commons.data.ListDelegation

data class DistrictName(
  private val names: List<String>
) : ListDelegation<String> {
  override val list: List<String>
    get() = names

  val address: String
    get() = names.joinToString("")

  override fun toString(): String {
    return address
  }
}