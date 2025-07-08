package io.github.cooperlyt.mis.work.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize( `as` = WorkDefineForCreate::class)
@JsonDeserialize( `as` = WorkDefineForCreate.Sample::class )
interface WorkDefineForCreate: WorkDefine, WorkIdentify {

  companion object {
    fun of(define: WorkDefine, workId: Long): WorkDefineForCreate = DefaultImpl(define, workId)
  }


  private data class DefaultImpl(
    private val define: WorkDefine,
    override val workId: Long
  ): WorkDefineForCreate, WorkDefine by define


  data class Sample(
    override val defineId: String,
    override val workName: String,
    override val type: String,
    override val process: Boolean,
    override val enabled: Boolean,
    override val tags: List<String>,
    override val workId: Long
  ): WorkDefineForCreate
}