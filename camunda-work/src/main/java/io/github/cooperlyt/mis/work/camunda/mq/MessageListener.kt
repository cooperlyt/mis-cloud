package io.github.cooperlyt.mis.work.camunda.mq

import io.github.cooperlyt.mis.work.message.WorkCreateMessage
import io.github.cooperlyt.mis.work.message.WorkEventMessage
import io.github.cooperlyt.mis.work.message.WorkMessage
import io.github.cooperlyt.rocketmq.client.TypedConsumer
import io.github.cooperlyt.rocketmq.client.support.ReactiveListenerContainer
import lombok.extern.slf4j.Slf4j
import org.camunda.bpm.engine.MismatchingMessageCorrelationException
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.impl.util.StringUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import java.util.function.Consumer

@Slf4j
@Configuration
open class MessageListener(private val runtimeService: RuntimeService) {

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(ReactiveListenerContainer::class.java)
    }

    //  process_project_license
    @Bean
    open fun processCreateChannel() = object: TypedConsumer<Message<WorkCreateMessage>>(
        Consumer<Message<WorkCreateMessage>> { msg ->
            val arg = msg.headers
            log.info(
                Thread.currentThread().name + " Receive New Create Messages: " + msg.payload + " ARG:"
                        + arg
            )
            val define =
                msg.headers.get(WorkMessage.MESSAGE_HEADER_WORK_DEFINE, String::class.java)
            val workId = msg.payload.workId.toString()

            //      approval
            runtimeService.startProcessInstanceByKey(define, workId, workId, msg.payload.data)
        }){}



    @Bean
    open fun signalEventChannel() = object: TypedConsumer<Message<Map<String, Any>>>(
        Consumer<Message<Map<String, Any>>> { msg  ->
            val arg = msg.headers
            log.info(
                Thread.currentThread().name + " Receive Signal Messages: " + msg.payload + " ARG:"
                        + arg
            )

            val signal = arg.get(WorkMessage.MESSAGE_HEADER_SIGNAL, String::class.java)
            if (StringUtil.hasText(signal)) {
                val vars = msg.payload
                if (vars.isEmpty()) {
                    runtimeService.signalEventReceived(signal)
                } else {
                    runtimeService.signalEventReceived(signal, vars)
                }
            }
        }){}


    @Bean
    open fun eventEventChannel() = object: TypedConsumer<Message<WorkEventMessage>>(
        Consumer<Message<WorkEventMessage>> { msg ->
            val arg = msg.headers
            log.info(
                Thread.currentThread().name + " Receive Messages Event: " + msg.payload + " ARG:"
                        + arg
            )

            val messageName =
                arg.get(WorkEventMessage.MESSAGE_HEADER_EVENT_MESSAGE, String::class.java)
            if (StringUtil.hasText(messageName)) {
                val event = msg.payload

                try {
                    if (event.args.isEmpty()) {
                        runtimeService.correlateMessage(messageName, event.businessKey)
                    } else {
                        runtimeService.correlateMessage(messageName, event.businessKey, event.args)
                    }
                } catch (e: MismatchingMessageCorrelationException) {
                    log.error(
                        "MismatchingMessageCorrelationException",
                        e
                    )
                }
            }
        }) {}

}