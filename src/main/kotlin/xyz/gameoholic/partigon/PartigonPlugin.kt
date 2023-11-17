package xyz.gameoholic.partigon

import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import xyz.gameoholic.partigon.parsers.ConfigFileParser
import xyz.gameoholic.partigon.parsers.ConfigSettings
import xyz.gameoholic.partigon.util.bind

internal class PartigonPlugin: JavaPlugin() {
    lateinit var configSettings: ConfigSettings

    override fun onEnable() {
        bind()

        val pluginId = 20318
        val metrics = Metrics(this, pluginId)

        saveResource("config.yml", false)
        configSettings = ConfigFileParser.parseFile()

        logger.info("Plugin enabled - Log level set to ${configSettings.logLevel}")
    }
}