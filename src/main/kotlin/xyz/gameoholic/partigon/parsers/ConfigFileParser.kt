package xyz.gameoholic.partigon.parsers

import xyz.gameoholic.partigon.util.LogLevel
import org.bukkit.configuration.file.YamlConfiguration
import xyz.gameoholic.partigon.PartigonPlugin
import xyz.gameoholic.partigon.util.inject
import java.io.File

object ConfigFileParser {
    private val plugin: PartigonPlugin by inject()

    /**
     * Should be executed asynchronously or when the server starts.
     */
    fun parseFile(): ConfigSettings {
        val config: YamlConfiguration =
            YamlConfiguration.loadConfiguration(File(plugin.dataFolder, "config.yml"))
        return ConfigSettings(
            LogLevel.valueOf(config.getString("LogLevel")!!),
        )
    }
}