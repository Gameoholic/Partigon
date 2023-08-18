package com.github.gameoholic.partigon

import org.bukkit.plugin.java.JavaPlugin


class FancyAnimationsPlugin: JavaPlugin() {
    override fun onEnable() {
        FancyAnimations.onEnable(this)
    }
}