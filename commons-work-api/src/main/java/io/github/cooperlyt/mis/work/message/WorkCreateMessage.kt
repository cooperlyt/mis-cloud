package io.github.cooperlyt.mis.work.message

import java.io.Serial

data class WorkCreateMessage(
    val define: String,
    val workId: Long,
    val documentation: ProcessDocumentation?,
    val variables: Map<String, Any>
) : java.io.Serializable {

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1L
    }
}