package io.github.cooperlyt.mis.work.create

import io.github.cooperlyt.mis.work.data.WorkAction
import io.github.cooperlyt.mis.work.data.WorkActionType
import org.springframework.aot.hint.annotation.Reflective
import org.springframework.core.annotation.AliasFor

/**
 *
 * TODO 考虑是否可以不clone 使用同一个 workId 仅再重新启动一个工作流, 没必要， 直接使用消息重建数据即可， 但要考虑正在运行的工作流
 *
 * TODO 任何时何都可以中止业务，只不过在业务已生效的状下，应再重新启动一个注销业务，而在生效前直接中止即可
 *
 * 要求： 方法必须返回 Mono<WorkCreateResult>
 *      不要在方法内修改Mono Context 中的 WorkId
 *
 * 功能：
 *  1.生成 Work  和 WorkOperator(如果参数中有从参数中取，没有取当前用户) 表
 *  2.将 workId 存储到 Mono 上下文中
 *  3.从 Mono 上下文中获取 工作流启动数据并发送消息
 *  4. 开启事务
 *
 *
 */

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Reflective
annotation class WorkRecreate(
  /**
   * 这时是原业务 workId
   */
  @get:AliasFor(attribute = "workId")
  val value: String = "",

  val workId: String = "",

  /**
   * 业务操作记录类型
   *
   * 仅 CREATE (默认)， APPLY
   */
  //TODO 从原业务的操作记录中查询原业务的 建立类型
  @Deprecated("remove")
  val actionType: WorkActionType = WorkActionType.CREATE,
)
