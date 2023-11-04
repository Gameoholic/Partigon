package xyz.gameoholic.partigon.parsers

import xyz.gameoholic.partigon.Partigon
import xyz.gameoholic.partigon.util.LogLevel
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object ConfigFileParser {

    /**
     * Should be executed asynchronously or when the server starts.
     */
    fun parseFile(): ConfigSettings {
        val config: YamlConfiguration =
            YamlConfiguration.loadConfiguration(File(Partigon.plugin.dataFolder, "config.yml"))
        return ConfigSettings(
            LogLevel.valueOf(config.getString("LogLevel")!!),
        )
    }
}