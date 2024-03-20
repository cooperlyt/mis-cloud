package io.github.cooperlyt.commons.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.cooperlyt.commons.utils.OptionalUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeopleCardInfo {


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


  private String formatId(String id) {
    return OptionalUtils.justOrEmpty(id).map(String::trim).map(String::toUpperCase).orElse(null);
  }

  public void setId(String id) {
    this.id = formatId(id);
  }

  public String getId() {
    return formatId(this.id);
  }

  public PeopleCardInfo(PeopleCardInfo cardInfo) {
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