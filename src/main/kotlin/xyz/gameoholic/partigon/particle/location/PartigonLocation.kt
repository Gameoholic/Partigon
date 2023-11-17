package xyz.gameoholic.partigon.particle.location

import org.bukkit.Location

/**
 * Represents an origin location for Partigon particles.
 */
interface PartigonLocation {

    /**
     * Gets the location for the particle.
     */
    fun getLocation(): Location
}