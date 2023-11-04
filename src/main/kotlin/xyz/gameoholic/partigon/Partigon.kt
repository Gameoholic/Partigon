package xyz.gameoholic.partigon

import xyz.gameoholic.partigon.parsers.ConfigFileParser
import xyz.gameoholic.partigon.parsers.ConfigSettings

object Partigon {
    lateinit var plugin: PartigonPlugin
    lateinit var configSettings: ConfigSettings

    fun onEnable(partigonPlugin: PartigonPlugin) {
        plugin = partigonPlugin

        plugin.saveResource("config.yml", false)
        configSettings = ConfigFileParser.parseFile()

        plugin.logger.info("Plugin enabled - Log level set to ${configSettings.logLevel}")
    }
}