package io.github.cooperlyt.mis.work

import io.github.cooperlyt.mis.work.data.WorkDefine
import io.github.cooperlyt.mis.work.data.WorkDefineForCreate
import io.github.cooperlyt.mis.work.data.WorkDefineForProcess
import io.github.cooperlyt.mis.work.message.ProcessDocumentation
import io.github.cooperlyt.mis.work.message.WorkCreateMessage
import io.github.cooperlyt.mis.work.message.WorkEventMessage
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.Supplier

interface WorkRemoteService {

    fun prepareCreate(defineId: String): Mono<WorkDefineForCreate>

    fun recreate(defineId: String, originalWorkId: Long): Mono<WorkDefineForCreate>

    fun prepareProcess(defineId: String): Mono<WorkDefineForProcess>

    fun define(defineId: String): Mono<WorkDefine>

    fun sendWorkCreateMessage(
        defineId: String,
        workId: Long,
        processData: Map<String, Any>,
        documentation: ProcessDocumentation? = null,
    ): Mono<Long>


    fun sendWorkEventMessage(
        messageName: String,
        defineId: String,
        workId: Long,
        processData: Map<String, Any>
    ): Mono<Long>

    fun workCreateMessageSinks(): Supplier<Flux<Message<WorkCreateMessage>>>

    fun workEventMessageSinks(): Supplier<Flux<Message<WorkEventMessage>>>

    fun applyWorkId(): Mono<Long>
}