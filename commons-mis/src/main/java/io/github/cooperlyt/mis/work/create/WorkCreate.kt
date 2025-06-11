package io.github.cooperlyt.mis.work.create

import io.github.cooperlyt.mis.work.data.WorkActionType
import io.github.cooperlyt.mis.work.data.WorkInfo
import org.springframework.aot.hint.annotation.Reflective
import org.springframework.core.annotation.AliasFor


/**
 * 要求： 方法必须返回 Mono<WorkCreateResult>
 *      不要在方法内修改Mono Context 中的 WorkId
 *
 *      以 方法是否返回 WorkCreateResult 为建立工作流的依据， define 里的 process 为是否必须创建工作流
 *
 * 功能：
 *  1.生成 Work  和 WorkOperator(如果参数中有从参数中取，没有取当前用户) 表
 *  2.将 workId 存储到 Mono 上下文中
 *  3.从 Mono 上下文中获取 工作流启动数据并发送消息
 *  4. 开启事务
 *
 * 不再管理事务， 因无法细粒度的控制事务
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Reflective
annotation class WorkCreate(
    @get:AliasFor(attribute = "defineId")
    val value: String = "",

    val defineId: String = "",
    /**
     * EL 表达式， 从参数取得 workId 并认为业务已经存在一个PREPARED的Work, 如未设置则生成一个并放入 Mono Context 中
     */
    //val workId: String = "",
    /**
     * 用于 设置业务都更改了那部分数据， 用于消息订阅区分数据是否需要更新
     */


    /**
     * 业务操作记录类型
     *
     * 业务来源， 放在这里而不是 Param 中是因为不同方式可能有不同的逻辑，
     *
     * 可发不同的消息来区分
     *
     * 仅 CREATE (默认)， APPLY
     */
    val actionType: WorkActionType = WorkActionType.CREATE,

    /**
     *
     */
    val dataSource: String = WorkInfo.SOURCE_FROM_OFFICE
    )
