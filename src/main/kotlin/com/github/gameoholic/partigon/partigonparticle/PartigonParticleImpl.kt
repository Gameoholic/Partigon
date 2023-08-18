package com.github.gameoholic.partigon.partigonparticle

import com.github.gameoholic.partigon.Partigon
import com.github.gameoholic.partigon.partigonparticle.envelope.Envelope
import com.github.gameoholic.partigon.partigonparticle.envelope.PropertyType
import com.github.gameoholic.partigon.util.LoggerUtil
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.util.*


class PartigonParticleImpl(
    override val world: World,
    override val centerPosition: Vector,
    override val particleType: Particle,
    override val count: Int,
    override val offset: Vector,
    override val stopCondition: (t: Int) -> Boolean,
    override val envelopes: List<Envelope>
) : PartigonParticle {

    private var t = -1
    private var task: BukkitTask? = null

    //position: Position relative to centerPosition
    private var position = Vector(0.0, 0.0, 0.0)
    private var currentCount = count
    private var currentOffset = offset.clone()

    var id = UUID.randomUUID()!!

    init {
        LoggerUtil.info("Creating PartigonParticleImpl $id with params: {world: $world, centerPosition: $centerPosition, particleType: $particleType, count: $count, offset: $offset, stopCondition: $stopCondition, envelopes: $envelopes}", id)
    }


    override fun start() {
        LoggerUtil.info("Starting PartigonParticleImpl", id)
        position = Vector(0, 0, 0)
        t = -1

        task?.cancel()
        task = object : BukkitRunnable() {
            override fun run() {
                onTimerTickPassed()
            }
        }.runTaskTimer(Partigon.plugin, 0L, 1L)
    }

    override fun pause() {
        LoggerUtil.info("Pausing PartigonParticleImpl", id)
        task?.cancel()
    }

    override fun resume() {
        LoggerUtil.info("Resuming PartigonParticleImpl", id)
        if (task != null) return
        task = Partigon.plugin.server.scheduler.runTaskTimer(Partigon.plugin, Runnable {
            onTimerTickPassed()
        }, 0L, 1L)
    }

    override fun stop() {
        LoggerUtil.info("Stopping PartigonParticleImpl", id)
        task?.cancel()
    }

    /**
     * Ran before the stop condition is checked on a new frame.
     */
    open fun onStartFrame(t: Int) {
        LoggerUtil.debug("Frame $t started", id)
    }

    /**
     * Ran after the particle is spawned on a frame.
     */
    open fun onEndFrame(t: Int) {
        LoggerUtil.debug("Frame $t ended", id)
    }

    private fun onTimerTickPassed() {
        LoggerUtil.debug("Timer tick passed", id)
        t++

        onStartFrame(t)
        if (stopCondition.invoke(t)) {
            LoggerUtil.debug("Stop condition met, stopping animation", id)
            stop()
        }
        applyEnvelopes()
        spawnParticle()

        onEndFrame(t)

    }

    private fun applyEnvelopes() {
        LoggerUtil.debug("Applying envelopes", id)

        //Reset all current-properties, because envelopes are additive
        position.x = 0.0
        position.y = 0.0
        position.z = 0.0
        currentCount = count
        currentOffset = offset.clone()

        for (envelope in envelopes) {
            val envelopePropertyType = envelope.propertyType
            val envelopeValue = envelope.getValueAt(t)
            when (envelopePropertyType) {
                PropertyType.POS_X -> {
                    position.x += envelopeValue
                }
                PropertyType.POS_Y -> {
                    position.y += envelopeValue
                }
                PropertyType.POS_Z -> {
                    position.z += envelopeValue
                }
                PropertyType.COUNT -> {
                    currentCount += envelopeValue.toInt()
                }
                PropertyType.OFFSET_X -> {
                    currentOffset.x += envelopeValue
                }
                PropertyType.OFFSET_Y -> {
                    currentOffset.y += envelopeValue
                }
                PropertyType.OFFSET_Z -> {
                    currentOffset.z += envelopeValue
                }
            }
        }

        LoggerUtil.debug("Current properties are: {position: $position, count: $count, offset: $offset}", id)



    }


    private fun spawnParticle() {
        LoggerUtil.debug("Spawning particle", id)
        var currentPosition = centerPosition.clone().add(position)

        val location = Location(world, currentPosition.x, currentPosition.y, currentPosition.z)

        world.spawnParticle(particleType, location, count, offset.x, offset.y, offset.z, 0.0)
        LoggerUtil.debug("Spawned particle at $location with params: {position: $position, count: $currentCount, offset: $currentOffset}", id)
    }

}