package io.github.cooperlyt.mis.work.impl.model

import io.github.cooperlyt.commons.data.ListDelegation
import io.github.cooperlyt.mis.work.data.WorkActionSummary

data class WorkActionsView(
  private val actions: List<WorkActionSummary.Sample>?,
): ListDelegation<WorkActionSummary> {
  override val list: List<WorkActionSummary>
    get() = actions ?: emptyList()
}