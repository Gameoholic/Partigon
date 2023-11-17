package xyz.gameoholic.partigon.particle.location

import org.bukkit.entity.Entity
import org.bukkit.util.Vector

/**
 * A PartigonPlugin location that follows the entity, with an optional offset.
 * @param entity The entity to set the location to
 * @param offset The offset to add to the entity's location
 */
class EntityLocation(private val entity: Entity, private val offset: Vector = Vector(0, 0, 0)): PartigonLocation {
    override fun getLocation() = entity.location.apply {
        this.x += offset.x
        this.y += offset.y
        this.z += offset.z
    }

}