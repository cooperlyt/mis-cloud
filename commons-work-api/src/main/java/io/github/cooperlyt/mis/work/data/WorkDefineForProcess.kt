package io.github.cooperlyt.mis.work.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize( `as` = WorkDefineForProcess::class)
@JsonDeserialize( `as` = WorkDefineForProcess.Sample::class )
interface WorkDefineForProcess: WorkDefineForCreate {

  companion object {
    fun of(define: WorkDefine, workId: Long, attachments: List<WorkAttachmentInfo>): WorkDefineForProcess = DefaultImpl(
      define,
      workId,
      attachments
    )
  }

  val attachments: List<WorkAttachmentInfo>

  private data class DefaultImpl(
    private val define: WorkDefine,
    override val workId: Long,
    @JsonSerialize(contentAs = WorkAttachmentInfo::class)
    @JsonDeserialize(contentAs = WorkAttachmentImpl::class)
    override val attachments: List<WorkAttachmentInfo>
  ): WorkDefineForProcess, WorkDefine by define


  data class Sample(
    @JsonSerialize(contentAs = WorkAttachmentInfo::class)
    @JsonDeserialize(contentAs = WorkAttachmentImpl::class)
    override val attachments: List<WorkAttachmentInfo>,
    override val defineId: String,
    override val workName: String,
    override val type: String,
    override val process: Boolean,
    override val enabled: Boolean,
    override val tags: List<String>,
    override val workId: Long
  ): WorkDefineForProcess
}