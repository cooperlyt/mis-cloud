package io.github.cooperlyt.commons.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonCardInfo{


  private String name;
  private String sex;
  private String ethnic;
  private LocalDateTime birthday;
  private String address;
  private String id;
  private String agency;
  private LocalDateTime expireStart;
  private LocalDateTime expireEnd;
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

  private final DateTimeFormatter validDateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");


  public String getValidBeginDate(){
    return expireStart.format(validDateFormat);
  }

  public String getValidEndDate(){
    if (expireEnd == null){
      return "长期";
    }
    return expireEnd.format(validDateFormat);
  }
}