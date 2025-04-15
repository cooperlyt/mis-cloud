package io.github.cooperlyt.mis.work.camunda.mq

import io.github.cooperlyt.cloud.addons.rabbit.ConfirmPublisher
import io.github.cooperlyt.mis.work.message.StatusChangeMessage
import io.github.cooperlyt.mis.work.message.WorkChangeMessage
import io.github.cooperlyt.mis.work.message.WorkMessage.MESSAGE_HEADER_WORK_DEFINE
import io.github.cooperlyt.mis.work.message.WorkStatus
import org.camunda.bpm.engine.RepositoryService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.repository.ProcessDefinition
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ProcessChangeEventService(private val repositoryService: RepositoryService) {

    companion object {
        private val logger = LoggerFactory.getLogger(ProcessChangeEventService::class.java)

        //const val MESSAGE_HEADER_TYPE = "type"
    }


    private val statusChangedMessagePublisher = object: ConfirmPublisher<StatusChangeMessage>() {

        fun statusChangedSinks() = sinks()

        fun sendStatusChangedMessage(type: String, playLoad: StatusChangeMessage): Mono<Boolean> {
            return sendMessage(playLoad, MESSAGE_HEADER_WORK_DEFINE to type)
        }
    }

    private val processChangedMessagePublisher = object: ConfirmPublisher<WorkChangeMessage>() {

        fun processChangedSinks() = sinks()

        fun sendProcessChangedMessage(type: String, playLoad: WorkChangeMessage): Mono<Boolean> {
            return sendMessage(playLoad, MESSAGE_HEADER_WORK_DEFINE to type)
        }
    }

    @Bean
    fun statusChanged() = statusChangedMessagePublisher.statusChangedSinks()

    @Bean
    fun processChanged() = processChangedMessagePublisher.processChangedSinks()

    @Throws(java.lang.Exception::class)
    fun statusChange(delegateExecution: DelegateExecution, status: WorkStatus) {
        statusChange(delegateExecution.processBusinessKey.toLong(), status, delegateExecution.processDefinitionId)
    }

    @Throws(Exception::class)
    fun statusChange(workId: Long, status: WorkStatus, processDefinitionId: String) {

        statusChangedMessagePublisher.sendStatusChangedMessage(
            getDefineId(processDefinitionId),
            StatusChangeMessage.builder().status(status).workId(workId).build())
            .subscribe(
                { logger.info("status change mq is send: {} -> {}", workId, status) },
                { throw Exception("message send fail!") }
            )
    }

    @Throws(java.lang.Exception::class)
    fun processChange(changeMessage: WorkChangeMessage, processDefinitionId: String) {
        processChangedMessagePublisher.sendProcessChangedMessage(
            getDefineId(processDefinitionId),
            changeMessage)
            .subscribe(
                { logger.info("process change mq is send: {} -> {}", changeMessage.workId, changeMessage) },
                { throw Exception("message send fail!") }
            )
    }

    private fun getDefineId(processDefinitionId: String): String {
        val definition: ProcessDefinition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(processDefinitionId)
            .singleResult()
        logger.info("process define id: {}, key: {}", definition.id, definition.key)
        return definition.key
    }
}