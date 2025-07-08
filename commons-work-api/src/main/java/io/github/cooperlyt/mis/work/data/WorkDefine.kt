package io.github.cooperlyt.mis.work.data

interface WorkDefine {

  val defineId: String
  val workName: String
  val type: String
  val process: Boolean
  val enabled: Boolean
  val tags: List<String>
}