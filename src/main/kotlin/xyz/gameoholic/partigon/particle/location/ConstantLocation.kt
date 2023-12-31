package xyz.gameoholic.partigon.particle.location

import org.bukkit.Location

/**
 * A constant PartigonPlugin location.
 * @param location The location of the particle
 */
class ConstantLocation(private val location: Location): PartigonLocation {
    override fun getLocation() = location

}