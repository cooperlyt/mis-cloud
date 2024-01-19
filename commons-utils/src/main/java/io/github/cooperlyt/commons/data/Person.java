package io.github.cooperlyt.commons.data;

import lombok.Getter;

import java.time.LocalDateTime;

public interface Person extends PowerBody{

  @Getter
  enum Sex {
    MALE("男"),
    FEMALE("女");

    private final String label;

    Sex(String label) {
      this.label = label;
    }
  }

  Sex getSex();

  LocalDateTime getBirthday();

  String getNation();

  int getEthnicity();

  String getAddress();

  String getPhoto();



}
