package xyz.gameoholic.partigon

import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.bstats.charts.SingleLineChart
import org.bukkit.plugin.java.JavaPlugin
import xyz.gameoholic.partigon.parsers.ConfigFileParser
import xyz.gameoholic.partigon.parsers.ConfigSettings
import xyz.gameoholic.partigon.util.bind

internal class PartigonPlugin: JavaPlugin() {
    lateinit var configSettings: ConfigSettings

    val metrics = Metrics(this, 20318)
    override fun onEnable() {
        bind()

        saveResource("config.yml", false)
        configSettings = ConfigFileParser.parseFile()

        logger.info("Plugin enabled - Log level set to ${configSettings.logLevel}")
    }
}