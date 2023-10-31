package xyz.gameoholic.partigon.util

import xyz.gameoholic.partigon.Partigon
import java.util.*

object LoggerUtil {
    fun debug(msg: String, particleId: UUID? = null) {
        if (xyz.gameoholic.partigon.Partigon.configSettings.logLevel.value <= LogLevel.DEBUG.value)
            xyz.gameoholic.partigon.Partigon.plugin.logger.info("DEBUG: $msg")
    }
    fun info(msg: String, particleId: UUID? = null) {
        if (xyz.gameoholic.partigon.Partigon.configSettings.logLevel.value <= LogLevel.INFO.value)
            xyz.gameoholic.partigon.Partigon.plugin.logger.info("INFO: $msg")
    }
    fun error(msg: String, particleId: UUID? = null) {
        if (xyz.gameoholic.partigon.Partigon.configSettings.logLevel.value <= LogLevel.ERROR.value)
            xyz.gameoholic.partigon.Partigon.plugin.logger.info("ERROR: $msg")
    }
}