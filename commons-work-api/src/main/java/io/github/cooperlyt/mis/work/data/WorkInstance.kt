package io.github.cooperlyt.mis.work.data

import io.github.cooperlyt.mis.work.data.WorkInfo

interface WorkInstance<T>: WorkInfo {

  val instance: T
  
  val description: String


  data class SimpleWorkInfo(
    private val workInfo: WorkInfo,
    override val instance: String
  ): WorkInstance<String>, WorkInfo by workInfo {
    override val description: String = instance
  }

}