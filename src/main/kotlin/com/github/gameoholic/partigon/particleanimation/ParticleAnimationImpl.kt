package com.github.gameoholic.partigon.particleanimation

import com.github.gameoholic.partigon.FancyAnimations
import com.github.gameoholic.partigon.particleanimation.envelope.Envelope
import com.github.gameoholic.partigon.util.LoggerUtil
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import java.util.*


abstract class ParticleAnimationImpl(
    override var world: World,
    override var centerPosition: Vector,
    override var particleType: Particle,
    override var count: Int,
    override var offset: Vector,
    override var stopCondition: (t: Int) -> Boolean,
    override val envelopes: List<Envelope>
) : ParticleAnimation {

    private var t = -1
    private var task: BukkitTask? = null

    //position: Position relative to centerPosition
    private var position = Vector(0.0, 0.0, 0.0)
    private var extra = 0.0
    var id = UUID.randomUUID()!!

    init {
        LoggerUtil.info("Creating ParticleAnimationImpl $id with params: {world: $world, centerPosition: $centerPosition, xExpression: $xExpression, yExpression: $yExpression, zExpression: $zExpression, particleType: $particleType, countExpression: $countExpression, offsetXExpression: $offsetXExpression, offsetYExpression: $offsetYExpression, offsetZExpression: $offsetZExpression, extraExpression: $extraExpression, stopCondition: $stopCondition}", id)
    }


    override fun start() {
        LoggerUtil.info("Starting ParticleAnimationImpl", id)
        position = Vector(0, 0, 0)
        t = -1

        task?.cancel()
        task = object : BukkitRunnable() {
            override fun run() {
                onTimerTickPassed()
            }
        }.runTaskTimer(FancyAnimations.plugin, 0L, 1L)
    }

    override fun pause() {
        LoggerUtil.info("Pausing ParticleAnimationImpl", id)
        task?.cancel()
    }

    override fun resume() {
        LoggerUtil.info("Resuming ParticleAnimationImpl", id)
        if (task != null) return
        task = FancyAnimations.plugin.server.scheduler.runTaskTimer(FancyAnimations.plugin, Runnable {
            onTimerTickPassed()
        }, 0L, 1L)
    }

    override fun stop() {
        LoggerUtil.info("Stopping ParticleAnimationImpl", id)
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

        onEndFrame(t)

    }

    fun applyEnvelopes() {

    }

    private fun calculateNewPosition() {
        LoggerUtil.debug("Calculating new position", id)
        val eX: Expression = ExpressionBuilder(xExpression)
            .variables("t")
            .build()
            .setVariable("t", t.toDouble())
        val newX: Double = eX.evaluate()

        val eY: Expression = ExpressionBuilder(yExpression)
            .variables("t")
            .build()
            .setVariable("t", t.toDouble())
        val newY: Double = eY.evaluate()

        val eZ: Expression = ExpressionBuilder(zExpression)
            .variables("t")
            .build()
            .setVariable("t", t.toDouble())
        val newZ: Double = eZ.evaluate()

        position.x = newX
        position.y = newY
        position.z = newZ
        LoggerUtil.debug("New position is $position", id)
    }

    private fun calculateNewParameters() {
        LoggerUtil.debug("Calculating new parameters", id)
        val eCount: Expression = ExpressionBuilder(countExpression)
            .variables("t")
            .build()
            .setVariable("t", t.toDouble())
        val newCount: Int = eCount.evaluate().toInt()

        val eOffsetX: Expression = ExpressionBuilder(offsetXExpression)
            .variables("t")
            .build()
            .setVariable("t", t.toDouble())
        val newOffsetX: Double = eOffsetX.evaluate()

        val eOffsetY: Expression = ExpressionBuilder(offsetYExpression)
            .variables("t")
            .build()
            .setVariable("t", t.toDouble())
        val newOffsetY: Double = eOffsetY.evaluate()

        val eOffsetZ: Expression = ExpressionBuilder(offsetZExpression)
            .variables("t")
            .build()
            .setVariable("t", t.toDouble())
        val newOffsetZ: Double = eOffsetZ.evaluate()

        val eExtra: Expression = ExpressionBuilder(extraExpression)
            .variables("t")
            .build()
            .setVariable("t", t.toDouble())
        val newExtra: Double = eExtra.evaluate()

        count = newCount
        offset.x = newOffsetX
        offset.y = newOffsetY
        offset.z = newOffsetZ
        extra = newExtra
        LoggerUtil.debug("New parameters are: {count: $count, offset: $offset, extra: $extra}", id)
    }

    private fun spawnParticle() {
        LoggerUtil.debug("Spawning particle", id)
        var finalPos = centerPosition.clone().add(position)

        val location = Location(world, finalPos.x, finalPos.y, finalPos.z)

        world.spawnParticle(particleType, location, count, offset.x, offset.y, offset.z, extra)
        LoggerUtil.debug("Spawned particle at $location with params: {count: $count, offset: $offset, extra: $extra}", id)
    }

}