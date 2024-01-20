package io.github.cooperlyt.mis.work.message;

import lombok.Getter;

@Getter
public enum WorkSignal {

  FILE_UPLOADED("fileUploadedSignal");

  private final String ref;

  WorkSignal(String ref) {
    this.ref = ref;
  }
}
