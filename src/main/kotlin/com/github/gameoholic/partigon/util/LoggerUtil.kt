package com.github.gameoholic.partigon.util

import com.github.gameoholic.partigon.FancyAnimations
import java.util.*

object LoggerUtil {
    fun debug(msg: String, particleId: UUID? = null) {
        if (FancyAnimations.configSettings.logLevel.value <= LogLevel.DEBUG.value)
            FancyAnimations.plugin.logger.info("DEBUG: $msg")
    }
    fun info(msg: String, particleId: UUID? = null) {
        if (FancyAnimations.configSettings.logLevel.value <= LogLevel.INFO.value)
            FancyAnimations.plugin.logger.info("INFO: $msg")
    }
    fun error(msg: String, particleId: UUID? = null) {
        if (FancyAnimations.configSettings.logLevel.value <= LogLevel.ERROR.value)
            FancyAnimations.plugin.logger.info("ERROR: $msg")
    }
}