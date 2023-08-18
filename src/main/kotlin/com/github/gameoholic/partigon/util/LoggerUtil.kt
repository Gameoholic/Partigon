package com.github.gameoholic.partigon.util

import com.github.gameoholic.partigon.Partigon
import java.util.*

object LoggerUtil {
    fun debug(msg: String, particleId: UUID? = null) {
        if (Partigon.configSettings.logLevel.value <= LogLevel.DEBUG.value)
            Partigon.plugin.logger.info("DEBUG: $msg")
    }
    fun info(msg: String, particleId: UUID? = null) {
        if (Partigon.configSettings.logLevel.value <= LogLevel.INFO.value)
            Partigon.plugin.logger.info("INFO: $msg")
    }
    fun error(msg: String, particleId: UUID? = null) {
        if (Partigon.configSettings.logLevel.value <= LogLevel.ERROR.value)
            Partigon.plugin.logger.info("ERROR: $msg")
    }
}