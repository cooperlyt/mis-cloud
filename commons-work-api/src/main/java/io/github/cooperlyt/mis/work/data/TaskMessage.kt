package io.github.cooperlyt.mis.work.data

interface TaskMessage {

    val message: String
    val taskName: String
    val pass: Boolean?
}

class DefaultTaskMessageImpl(
    override val message: String,
    override val taskName: String,
    override val pass: Boolean?
): TaskMessage