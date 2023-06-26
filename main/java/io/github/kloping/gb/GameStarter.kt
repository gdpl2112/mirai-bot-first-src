package io.github.kloping.gb

import java.util.*

/**
 * @author github.kloping
 */
abstract class GameStarter {
    abstract fun handler(bot: BotInterface, context: MessageContext);
}

class MessageContext {
    val msgs = LinkedList<MessageData>()

    var data: Any? = null

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

    constructor(sid: Long, gid: Long, oid: Long) {
        this.sid = sid.toString()
        this.gid = gid.toString()
        this.oid = oid.toString()
    }

    fun getAt(): DataAt? {
        for (msg in msgs) {
            if (msg is DataAt) return msg;
        }
        return null;
    }
}

abstract class MessageData {
    var data: Any? = null;
}

data class DataText(val text: String) : MessageData();

data class DataImage(val id: String, var url: String) : MessageData();

data class DataAt(val id: String) : MessageData();