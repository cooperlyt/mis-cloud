package io.github.cooperlyt.mis.work.data

interface TaskMessage {

    val message: String
    val taskName: String
    val pass: Boolean?
}