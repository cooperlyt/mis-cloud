package io.github.cooperlyt.commons.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonCardInfo{


  @NotBlank
  private String name;
  @NotBlank
  private String sex;
  @NotBlank
  private String ethnic;
  @NotNull
  private LocalDateTime birthday;
  @NotBlank
  private String address;
  @NotBlank
  private String id;
  @NotBlank
  private String agency;


  @NotNull
  @JsonProperty("expire_start")
  private LocalDateTime expireStart;

  @JsonProperty("expire_end")
  private LocalDateTime expireEnd;
  @NotBlank
  private String picture;

  public PersonCardInfo(PersonCardInfo cardInfo) {
    this.name = cardInfo.getName();
    this.sex = cardInfo.getSex();
    this.ethnic = cardInfo.getEthnic();
    this.birthday = cardInfo.getBirthday();
    this.address = cardInfo.getAddress();
    this.id = cardInfo.getId();
    this.agency = cardInfo.getAgency();
    this.expireStart = cardInfo.getExpireStart();
    this.expireEnd = cardInfo.getExpireEnd();
    this.picture = cardInfo.getPicture();
  }

}