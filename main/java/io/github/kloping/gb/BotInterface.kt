package io.github.kloping.gb

/**
 * @author github.kloping
 */
interface BotInterface {
    val botId: String

    fun onReturn(context: MessageContext, command: String, data: Any?): Int

    fun getSender(context: MessageContext): Sender

    fun getInfoGetter(context: MessageContext): InfoGetter;
}

interface InfoGetter {
    fun getName(id: String): String;
    fun getNameFromEnv(id: String): String;
}

interface Sender {
    fun sendEnv(data: List<MessageData>)

    fun sendEnv(text: String)

    fun sendEnvWithAt(text: String)

    fun sendEnvReply(text: String)

    fun sendEnvReplyWithAt(text: String)
}