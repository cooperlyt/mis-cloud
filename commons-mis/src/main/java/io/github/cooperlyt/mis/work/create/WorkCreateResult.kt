package io.github.cooperlyt.mis.work.create

import io.github.cooperlyt.mis.work.message.ProcessDocumentation

interface WorkCreateResult {

    companion object {
        fun empty() = DefaultImpl()

        fun of(
            documentation: ProcessDocumentation? = null,
            variables: Map<String, Any> = emptyMap()
        ): WorkCreateResult  = DefaultImpl(documentation, variables)
    }

    val documentation: ProcessDocumentation?
    val variables: Map<String, Any>

    data class DefaultImpl(
        override val documentation: ProcessDocumentation? = null,
        override val variables: Map<String, Any> = emptyMap()): WorkCreateResult

}