package io.github.cooperlyt.mis.work.camunda.mq

import io.github.cooperlyt.cloud.addons.rabbit.AcknowledgeConsumer
import io.github.cooperlyt.mis.work.message.WorkCreateMessage
import io.github.cooperlyt.mis.work.message.WorkEventMessage
import org.camunda.bpm.engine.MismatchingMessageCorrelationException
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.impl.util.StringUtil
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class MessageListener(private val runtimeService: RuntimeService) {

    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(MessageListener::class.java)
    }


    @Bean
    fun workCreate() = createRequestConsumer.consumer()

    @Bean
    fun workEvent() = workEventMessageConsumer.consumer()

    private val createRequestConsumer = object : AcknowledgeConsumer<WorkCreateMessage>() {
        override fun processMessage(message: WorkCreateMessage): Mono<Void> {
//            val arg: MessageHeaders = message.getHeaders()
            logger.info(
                Thread.currentThread().name + " Receive New Create Messages: " + message)
//            val define: String =
//                message.getHeaders().get<String>(WorkMessage.MESSAGE_HEADER_WORK_DEFINE, String::class.java)
            val businessKey: String = message.workId.toString()


            //      approval

            return Mono.fromCallable {
                runtimeService.startProcessInstanceByKey(message.type, businessKey, businessKey, message.data)
            }.then()
        }
    }

//    val signalRequestConsumer = object : AcknowledgeConsumer<Map<String,Any>>() {
//        override fun processMessage(message: Map<String,Any>): Mono<Void> {
//            logger.info(
//                Thread.currentThread().name + " Receive New Signal Messages: " + message)
//            val businessKey: String = message.workId.toString()
//            return Mono.fromCallable {
//                val arg: MessageHeaders = msg.getHeaders()
//                MessageListener.log.info(
//                    Thread.currentThread().name + " Receive Signal Messages: " + msg.getPayload() + " ARG:"
//                            + arg
//                )
//
//                val signal = arg.get(WorkMessage.MESSAGE_HEADER_SIGNAL, String::class.java)
//                if (StringUtil.hasText(signal)) {
//                    val vars: Map<String, Any> = msg.getPayload()
//                    if (vars.isEmpty()) {
//                        runtimeService.signalEventReceived(signal)
//                    } else {
//                        runtimeService.signalEventReceived(signal, vars)
//                    }
//                }
//            }.then()
//        }
//    }


    private val workEventMessageConsumer = object : AcknowledgeConsumer<WorkEventMessage>() {
        override fun processMessage(message: WorkEventMessage): Mono<Void> {
            logger.info(
                Thread.currentThread().name + " Receive New Event Messages: " + message)
            return Mono.fromCallable {
                //val messageName = arg.get(WorkEventMessage.MESSAGE_HEADER_EVENT_MESSAGE, String::class.java)
                if (StringUtil.hasText(message.name)) {
                    try {
                        if (message.args.isEmpty()) {
                            runtimeService.correlateMessage(message.name, message.businessKey)
                        } else {
                            runtimeService.correlateMessage(message.name, message.businessKey, message.args)
                        }
                    } catch (e: MismatchingMessageCorrelationException) {
                        //这里测试一下 ack 是仅确认消息到达， 还是确认消息被成功处理
                        logger.error("MismatchingMessageCorrelationException", e)
                        throw io.github.cooperlyt.cloud.addons.rabbit.InvalidMessageException("MismatchingMessageCorrelationException!", e)
                    }
                }
            }.then()
        }
    }
}