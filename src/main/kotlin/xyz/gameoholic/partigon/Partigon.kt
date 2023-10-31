package xyz.gameoholic.partigon

import xyz.gameoholic.partigon.parsers.ConfigFileParser
import xyz.gameoholic.partigon.parsers.ConfigSettings

object Partigon {
    lateinit var plugin: xyz.gameoholic.partigon.PartigonPlugin
    lateinit var configSettings: xyz.gameoholic.partigon.parsers.ConfigSettings

    fun onEnable(partigonPlugin: xyz.gameoholic.partigon.PartigonPlugin) {
        xyz.gameoholic.partigon.Partigon.plugin = partigonPlugin

        xyz.gameoholic.partigon.Partigon.plugin.saveResource("config.yml", false)
        xyz.gameoholic.partigon.Partigon.configSettings = xyz.gameoholic.partigon.parsers.ConfigFileParser.parseFile()

        xyz.gameoholic.partigon.Partigon.plugin.logger.info("Plugin enabled - Log level set to ${xyz.gameoholic.partigon.Partigon.configSettings.logLevel}")
    }
}