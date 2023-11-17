package xyz.gameoholic.partigon.util

import xyz.gameoholic.partigon.PartigonPlugin
import java.util.*

object LoggerUtil {
    private val plugin: PartigonPlugin by inject()

    fun debug(msg: String, particleId: UUID? = null) {
        if (plugin.configSettings.logLevel.value <= LogLevel.DEBUG.value)
            plugin.logger.info("DEBUG: $msg")
    }
    fun info(msg: String, particleId: UUID? = null) {
        if (plugin.configSettings.logLevel.value <= LogLevel.INFO.value)
            plugin.logger.info("INFO: $msg")
    }
    fun error(msg: String, particleId: UUID? = null) {
        if (plugin.configSettings.logLevel.value <= LogLevel.ERROR.value)
            plugin.logger.info("ERROR: $msg")
    }
}