package io.github.cooperlyt.commons.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import jakarta.validation.constraints.Size


@JsonSerialize(`as` = PowerBody::class)
@JsonDeserialize(`as` = PowerBody.Sample::class)
interface PowerBody {



  val name: String

  val idType: IdentityType

  val idNumber: String

  val tel: String?

  data class Sample(
    @Size(max = 128)
    override val name: String,
    override val idType: IdentityType,
    @Size(max = 64)
    override val idNumber: String,
    @Size(max = 32)
    override val tel: String?
  ): PowerBody
}