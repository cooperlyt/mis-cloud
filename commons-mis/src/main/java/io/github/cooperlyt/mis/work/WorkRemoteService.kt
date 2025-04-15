package io.github.cooperlyt.mis.work

import io.github.cooperlyt.mis.work.data.WorkDefine
import io.github.cooperlyt.mis.work.data.WorkDefineForCreate
import io.github.cooperlyt.mis.work.data.WorkDefineForProcess
import reactor.core.publisher.Mono

interface WorkRemoteService {

    fun prepareCreate(defineId: String): Mono<WorkDefineForCreate>

    fun recreate(defineId: String, originalWorkId: Long): Mono<WorkDefineForCreate>

    fun prepareProcess(defineId: String): Mono<WorkDefineForProcess>

    fun define(defineId: String): Mono<WorkDefine>

    fun sendWorkCreateMessage(
        defineId: String,
        workId: Long, processData: Map<String, Any>
    ): Mono<Long>


    fun sendWorkEventMessage(
        messageName: String,
        defineId: String,
        workId: Long,
        processData: Map<String, Any>
    ): Mono<Long>

    fun applyWorkId(): Mono<Long>
}