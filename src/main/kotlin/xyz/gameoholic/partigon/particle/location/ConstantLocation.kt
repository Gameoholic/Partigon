package xyz.gameoholic.partigon.particle.location

import org.bukkit.Location

class ConstantLocation(private val location: Location): PartigonLocation {
    override fun getLocation() = location

}