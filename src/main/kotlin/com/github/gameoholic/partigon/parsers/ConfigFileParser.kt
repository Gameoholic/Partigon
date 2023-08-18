package com.github.gameoholic.partigon.parsers

import com.github.gameoholic.partigon.FancyAnimations
import com.github.gameoholic.partigon.util.LogLevel
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object ConfigFileParser {

    /**
     * Should be executed asynchronously or when the server starts.
     */
    fun parseFile(): ConfigSettings {
        val config: YamlConfiguration =
            YamlConfiguration.loadConfiguration(File(FancyAnimations.plugin.dataFolder, "config.yml"))
        return ConfigSettings(
            LogLevel.valueOf(config.getString("LogLevel")!!),
        )
    }
}