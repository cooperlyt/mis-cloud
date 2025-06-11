package io.github.cooperlyt.mis.work.impl.model

import io.github.cooperlyt.commons.data.IdentityType
import io.github.cooperlyt.commons.data.PowerBody
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table

@Table("work_applicant")
data class WorkApplicantModel(
  @Id
  val workId: Long,

  override val name: String,
  override val idType: IdentityType,
  override val idNumber: String,
  override val tel: String?,
  private val _new: Boolean = false,
): PowerBody,Persistable<Long>{

  constructor(workId: Long, powerBody: PowerBody, isNew: Boolean = true) : this(
    workId,
    powerBody.name,
    powerBody.idType,
    powerBody.idNumber,
    powerBody.tel,
    isNew,
  )

  override fun getId(): Long? = workId

  override fun isNew(): Boolean = _new


}
