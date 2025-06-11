package io.github.cooperlyt.mis.work.message

import java.io.Serial

data class WorkCreateMessage(
    val define: String,
    val workId: Long,
    val tags: Set<String>,
    val isProcess: Boolean = false,
    val documentation: ProcessDocumentation? = null,
    val variables: Map<String, Any> = emptyMap(),
) : java.io.Serializable {

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1L
    }
}