package io.github.kloping.gb

/**
 * @author github.kloping
 */
abstract class GameStarter(private val bot: BotInterface) {
    abstract fun handler(context: MessageContext): Any?
}

abstract class MessageContext : BotInterface {
    val data = emptyArray<MessageData>()

    /**
     * sender id
     */
    val sid: String;

    /**
     * env id
     */
    val gid: String;

    /**
     * other id
     */
    var oid: String = "";

    constructor(sid: String, gid: String, oid: String) {
        this.sid = sid
        this.gid = gid
        this.oid = oid
    }
}

abstract class MessageData {
    var data: Any? = null;
}

data class DataText(val text: String) : MessageData();

data class DataImage(val id: String, var url: String) : MessageData();

data class DataAt(val id: String) : MessageData();