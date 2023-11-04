package xyz.gameoholic.partigon.parsers

import xyz.gameoholic.partigon.Partigon
import xyz.gameoholic.partigon.util.LogLevel
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object ConfigFileParser {

    /**
     * Should be executed asynchronously or when the server starts.
     */
    fun parseFile(): xyz.gameoholic.partigon.parsers.ConfigSettings {
        val config: YamlConfiguration =
            YamlConfiguration.loadConfiguration(File(xyz.gameoholic.partigon.Partigon.plugin.dataFolder, "config.yml"))
        return xyz.gameoholic.partigon.parsers.ConfigSettings(
            LogLevel.valueOf(config.getString("LogLevel")!!),
        )
    }
}