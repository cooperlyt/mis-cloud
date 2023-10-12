package io.github.cooperlyt.commons.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonImpl extends PowerBodyImpl implements Person{

  public PersonImpl(Person person){
    super(person.getIdType(), person.getName(), person.getIdNumber(), person.getTel());
    this.sex = person.getSex();
    this.birthday = person.getBirthday();
    this.ethnicity = person.getEthnicity();
    this.nation = person.getNation();
    this.address = person.getAddress();
    this.photo = person.getPhoto();
  }

  private Sex sex;

  private java.time.LocalDateTime birthday;

  private int ethnicity;

  private String nation;

  private String address;

  private String photo;

}
