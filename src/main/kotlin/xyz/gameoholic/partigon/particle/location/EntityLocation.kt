package xyz.gameoholic.partigon.particle.location

import org.bukkit.entity.Entity
import org.bukkit.util.Vector

class EntityLocation(private val entity: Entity, private val offset: Vector = Vector(0, 0, 0)): PartigonLocation {
    override fun getLocation() = entity.location.apply {
        this.x += offset.x
        this.y += offset.y
        this.z += offset.z
    }

}