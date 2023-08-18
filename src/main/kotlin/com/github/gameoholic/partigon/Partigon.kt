package com.github.gameoholic.partigon

import com.github.gameoholic.partigon.commands.TestCommand
import com.github.gameoholic.partigon.parsers.ConfigFileParser
import com.github.gameoholic.partigon.parsers.ConfigSettings

object Partigon {
    lateinit var plugin: PartigonPlugin
    lateinit var configSettings: ConfigSettings

    fun onEnable(fancyAnimationsPlugin: PartigonPlugin) {
        plugin = fancyAnimationsPlugin

        plugin.saveResource("config.yml", false)
        configSettings = ConfigFileParser.parseFile()

        plugin.getCommand("test")?.setExecutor(TestCommand)

        plugin.logger.info("Plugin enabled - Log level set to ${configSettings.logLevel}")
    }


    //Every tick:
    //Go over every envelope, apply envelope
}