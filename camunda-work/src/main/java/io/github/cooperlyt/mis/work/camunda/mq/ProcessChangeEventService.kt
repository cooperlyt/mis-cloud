package io.github.cooperlyt.mis.work.camunda.mq

import io.github.cooperlyt.cloud.addons.rabbit.ConfirmPublisher
import io.github.cooperlyt.mis.work.ProcessConstant
import io.github.cooperlyt.mis.work.ProcessConstant.MESSAGE_HEADER_DEFINE_KEY
import io.github.cooperlyt.mis.work.message.WorkStatusChangedMessage
import io.github.cooperlyt.mis.work.message.WorkStageChangedMessage
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


    private val statusChangedMessagePublisher = object: ConfirmPublisher<WorkStatusChangedMessage>() {

        fun statusChangedSinks() = sinks()

        fun sendStatusChangedMessage(playLoad: WorkStatusChangedMessage) {
            return sendNoConfirmMessage(playLoad, MESSAGE_HEADER_DEFINE_KEY to ProcessConstant.defineToRouterKey(playLoad.define))
        }
    }

    private val stageChangedMessagePublisher = object: ConfirmPublisher<WorkStageChangedMessage>() {

        fun stageChangedSinks() = sinks()

        fun sendStageChangedMessage(playLoad: WorkStageChangedMessage) {
            return sendNoConfirmMessage(playLoad, MESSAGE_HEADER_DEFINE_KEY to ProcessConstant.defineToRouterKey(playLoad.define))
        }
    }

    @Bean
    fun statusChangedChannel() = statusChangedMessagePublisher.statusChangedSinks()

    @Bean
    fun stageChangedChannel() = stageChangedMessagePublisher.stageChangedSinks()

    @Throws(java.lang.Exception::class)
    fun statusChange(delegateExecution: DelegateExecution, status: WorkStatus) {
        statusChange(delegateExecution.processBusinessKey.toLong(), status, delegateExecution.processDefinitionId)
    }

    @Throws(Exception::class)
    fun statusChange(workId: Long, status: WorkStatus, processDefinitionId: String) {

        statusChangedMessagePublisher.sendStatusChangedMessage(
            WorkStatusChangedMessage.builder().define(getDefineId(processDefinitionId)).status(status).workId(workId).build())

    }

    @Throws(java.lang.Exception::class)
    fun stageChange(changeMessage: WorkStageChangedMessage, processDefinitionId: String) {
        changeMessage.define = getDefineId(processDefinitionId)
        stageChangedMessagePublisher.sendStageChangedMessage(changeMessage)
    }

    private fun getDefineId(processDefinitionId: String): String {
        val definition: ProcessDefinition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(processDefinitionId)
            .singleResult()
        logger.info("process define id: {}, key: {}", definition.id, definition.key)
        return definition.key
    }
}