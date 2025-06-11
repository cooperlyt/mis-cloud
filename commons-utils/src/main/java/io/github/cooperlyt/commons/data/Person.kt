package io.github.cooperlyt.commons.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.time.LocalDateTime

@JsonSerialize(`as` = Person::class)
@JsonDeserialize(`as` = Person.Sample::class)
interface Person : PowerBody {

  enum class Sex(val label: String) {
    MALE("男"),
    FEMALE("女");
  }

  val sex: Sex

  val birthday: LocalDateTime?

  val nation: String?

  val ethnicity: Int?

  val address: String?

  val email: String?

  data class Sample(
    override val sex: Sex,
    override val birthday: LocalDateTime?,
    override val nation: String?,
    override val ethnicity: Int?,
    override val address: String?,
    override val email: String?,
    override val name: String,
    override val idType: IdentityType,
    override val idNumber: String,
    override val tel: String?
  ): Person
}