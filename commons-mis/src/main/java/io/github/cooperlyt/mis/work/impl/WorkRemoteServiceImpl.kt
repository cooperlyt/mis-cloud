package io.github.cooperlyt.mis.work.impl

import io.github.cooperlyt.cloud.addons.rabbit.ConfirmPublisher
import io.github.cooperlyt.mis.RemoteResponseService
import io.github.cooperlyt.mis.work.Constant
import io.github.cooperlyt.mis.work.WorkRemoteService
import io.github.cooperlyt.mis.work.data.WorkDefine
import io.github.cooperlyt.mis.work.data.WorkDefineForCreate
import io.github.cooperlyt.mis.work.data.WorkDefineForProcess
import io.github.cooperlyt.mis.work.message.ProcessDocumentation
import io.github.cooperlyt.mis.work.message.WorkCreateMessage
import io.github.cooperlyt.mis.work.message.WorkEventMessage
import io.github.cooperlyt.mis.work.message.WorkMessage.MESSAGE_HEADER_WORK_DEFINE
import org.springframework.http.MediaType
import org.springframework.messaging.Message
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.util.function.Supplier

class WorkRemoteServiceImpl(private val webClient: WebClient, private val serverName: String): RemoteResponseService(), WorkRemoteService {

    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(WorkRemoteServiceImpl::class.java)
    }

    private val workEventMessagePublisher = object : ConfirmPublisher<WorkEventMessage>() {
        fun sendWorkEventMessage(messageName: String, defineId: String,
                                 workId: Long, processData: Map<String,Any>): Mono<Boolean> {
            return sendMessage(
                WorkEventMessage(messageName, workId.toString(), processData),
                MESSAGE_HEADER_WORK_DEFINE to defineId
            )
        }

        fun workEventMessageSinks() = sinks()
    }

    private val workCreateMessagePublisher = object : ConfirmPublisher<WorkCreateMessage>() {
        fun sendWorkCreateMessage(defineId: String,
                                  workId: Long,
                                  variables: Map<String,Any>,
                                  documentation: ProcessDocumentation?): Mono<Boolean> {
            return sendMessage(
                WorkCreateMessage(defineId, workId, documentation, variables),
                MESSAGE_HEADER_WORK_DEFINE to defineId
            )
        }

        fun workCreateMessageSinks() = sinks()
    }

    override fun sendWorkEventMessage(
        messageName: String,
        defineId: String,
        workId: Long,
        processData: Map<String, Any>
    ): Mono<Long> {
        return workEventMessagePublisher.sendWorkEventMessage(messageName,defineId, workId, processData)
            .filter { ifSend -> ifSend }
            .map { _ -> workId }
            .switchIfEmpty(Mono.error(Constant.ErrorDefine.MESSAGE_SEND_FAIL.exception()))
    }

    override fun workCreateMessageSinks(): Supplier<Flux<Message<WorkCreateMessage>>> {
        return workCreateMessagePublisher.workCreateMessageSinks()
    }

    override fun workEventMessageSinks(): Supplier<Flux<Message<WorkEventMessage>>> {
        return workEventMessagePublisher.workEventMessageSinks()
    }


    override fun sendWorkCreateMessage(
        defineId: String,
        workId: Long,
        variables: Map<String, Any>,
        documentation: ProcessDocumentation?
    ): Mono<Long> {
        return workCreateMessagePublisher.sendWorkCreateMessage(defineId, workId, variables, documentation)
            .filter { ifSend -> ifSend }
            .map { _ -> workId }
            .switchIfEmpty(Mono.error(Constant.ErrorDefine.MESSAGE_SEND_FAIL.exception()))
    }

//
//    @Value("\${mis.internal.work.serverName}")
//    private var serverName: String? = null

    override fun prepareCreate(defineId: String): Mono<WorkDefineForCreate> {
        logger.debug("prepare create work {}", defineId)
        return webClient
            .get()
            .uri("http://$serverName/internal/create/{defineId}", defineId)
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono { response ->
                sourceResponse(
                    WorkDefineForCreate::class.java, response
                )
            }
    }

    override fun recreate(defineId: String, originalWorkId: Long): Mono<WorkDefineForCreate> {
        logger.debug("prepare create work {}", defineId)
        return webClient
            .get()
            .uri("http://$serverName/internal/create/{defineId}/{workId}", defineId, originalWorkId)
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono { response ->
                sourceResponse(
                    WorkDefineForCreate::class.java, response
                )
            }
    }

    override fun prepareProcess(defineId: String): Mono<WorkDefineForProcess> {
        return webClient
            .get()
            .uri("http://$serverName/internal/create/process/{defineId}", defineId)
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono { response ->
                sourceResponse(
                    WorkDefineForProcess::class.java, response
                )
            }
    }

    override fun define(defineId: String): Mono<WorkDefine> {
        return webClient
            .get()
            .uri("http://$serverName/internal/define/{defineId}", defineId)
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono { response ->
                sourceResponse(
                    WorkDefine::class.java, response
                )
            }
    }

    override fun applyWorkId(): Mono<Long> {
        return webClient
            .get()
            .uri("http://$serverName/internal/id")
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono { response ->
                sourceResponse(
                    Long::class.java, response
                )
            }
    }

}