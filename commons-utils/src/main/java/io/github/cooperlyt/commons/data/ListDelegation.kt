package io.github.cooperlyt.commons.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(`as` = List::class)
interface ListDelegation<T>: List<T> {

  @get:JsonIgnore
  val list: List<T>

  override fun contains(element: T): Boolean {
    return list.contains(element)
  }

  override fun containsAll(elements: Collection<T>): Boolean {
    return list.containsAll(elements)
  }

  override fun get(index: Int): T {
    return list[index]
  }

  override fun indexOf(element: T): Int {
    return list.indexOf(element)
  }

  override fun isEmpty(): Boolean {
    return list.isEmpty()
  }

  override fun iterator(): Iterator<T> {
    return list.iterator()
  }

  override fun lastIndexOf(element: T): Int {
    return list.lastIndexOf(element)
  }

  override fun listIterator(): ListIterator<T> {
    return list.listIterator()
  }

  override fun listIterator(index: Int): ListIterator<T> {
    return list.listIterator(index)
  }

  override fun subList(fromIndex: Int, toIndex: Int): List<T> {
    return list.subList(fromIndex, toIndex)
  }

  override val size: Int
    get() = list.size
}
