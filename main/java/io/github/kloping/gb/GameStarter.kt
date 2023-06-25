package io.github.kloping.gb

/**
 * @author github.kloping
 */
abstract class GameStarter(private val bot: BotInterface) {
    abstract fun handler(): Any?

}

abstract class MessageData {
}

class DataText : MessageData() {
}
