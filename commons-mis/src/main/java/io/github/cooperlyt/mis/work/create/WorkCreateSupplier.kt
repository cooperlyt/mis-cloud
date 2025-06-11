package io.github.cooperlyt.mis.work.create

import io.github.cooperlyt.commons.data.PowerBody
import io.github.cooperlyt.mis.work.data.WorkOperatorSupplier
import java.time.LocalDateTime

interface WorkCreateSupplierBase {
  val note: String?
  /**
   * 某此业务需要先申请 workId 后建立业务
   */
  val workId: Long?

  val historyDateTime: LocalDateTime?

  val history: Boolean
    get() = historyDateTime != null
}

interface WorkCreateSupplierBasic: WorkCreateSupplierBase {

  val applicant: PowerBody?

}

interface EmployeeWorkCreateSupplier: WorkCreateSupplierBase {

  val username: String
}

interface WorkCreateParams: WorkCreateSupplierBasic {

  companion object {
    fun of (supplier: WorkCreateSupplierBasic, operator: WorkOperatorSupplier? = null): WorkCreateParams = DefaultImpl(
      supplier,
      operator
    )


  }

  val operator: WorkOperatorSupplier?

  private data class DefaultImpl(
    private val supplier: WorkCreateSupplierBasic,
    override val operator: WorkOperatorSupplier?
  ): WorkCreateParams, WorkCreateSupplierBasic by supplier

  data class Sample(
    override val workId: Long?,
    override val note: String? = null,
    override val applicant: PowerBody? = null,
    override val operator: WorkOperatorSupplier? = null,
    override val historyDateTime: LocalDateTime? = null
  ): WorkCreateParams
}

interface WorkCreateSupplier<T>: WorkCreateSupplierBasic {

  companion object {
    fun <T> of(data: T, supplier: WorkCreateSupplierBasic): WorkCreateSupplier<T> = DefaultImpl(
      supplier, data
    )
  }

  val data: T

  private data class DefaultImpl<T>(
    private val supplier: WorkCreateSupplierBasic,
    override val data: T
  ): WorkCreateSupplier<T>, WorkCreateSupplierBasic by supplier

}

