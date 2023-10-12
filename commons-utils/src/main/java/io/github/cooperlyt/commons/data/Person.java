package io.github.cooperlyt.commons.data;

import java.time.LocalDateTime;

public interface Person extends PowerBody{

  enum Sex {
    MALE,
    FEMALE
  }

  Sex getSex();

  LocalDateTime getBirthday();

  String getNation();

  int getEthnicity();

  String getAddress();

  String getPhoto();



}
