package io.github.cooperlyt.mis.work.data;

public interface WorkFileInfo {

  String getFid();

  String getSha256();

  String getTaskId();

  Integer getSize();

  String getMime();

  String getETag();

  String getFilename();

}

