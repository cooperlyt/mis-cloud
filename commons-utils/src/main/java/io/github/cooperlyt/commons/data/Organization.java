package io.github.cooperlyt.commons.data;

public interface Organization extends PowerBody{

  enum LegalType{
    OWNER,
    MANAGER
  }

  LegalType getLegalType();

  String getLegalName();

  String getAddress();
}
