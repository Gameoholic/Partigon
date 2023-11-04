package xyz.gameoholic.partigon

import org.bukkit.plugin.java.JavaPlugin


internal class PartigonPlugin: JavaPlugin() {
    override fun onEnable() {
        Partigon.onEnable(this)
    }
}