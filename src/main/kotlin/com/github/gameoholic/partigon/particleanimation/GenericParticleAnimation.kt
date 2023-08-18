package com.github.gameoholic.partigon.particleanimation

import com.github.gameoholic.partigon.util.LoggerUtil
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.util.Vector


data class GenericParticleSettings(
    val world: World,
    val centerPosition: Vector,
    val particleType: Particle,
    val xExpression: String = "",
    val yExpression: String = "",
    val zExpression: String = "",
    val countExpression: String = "1",
    val offsetXExpression: String = "0.0",
    val offsetYExpression: String = "0.0",
    val offsetZExpression: String = "0.0",
    val extraExpression: String = "0.0",
    val stopCondition: (t: Int) -> Boolean = { false }
)

open class GenericParticleAnimation(
    particleSettings: GenericParticleSettings
) : ParticleAnimationImpl(
    particleSettings.world,
    particleSettings.centerPosition,
    particleSettings.xExpression,
    particleSettings.yExpression,
    particleSettings.zExpression,
    particleSettings.particleType,
    particleSettings.countExpression,
    particleSettings.offsetXExpression,
    particleSettings.offsetYExpression,
    particleSettings.offsetZExpression,
    particleSettings.extraExpression,
    particleSettings.stopCondition
) {

    init {
        LoggerUtil.info("Creating GenericParticleAnimation with params: {world: $world, centerPosition: $centerPosition, xExpression: $xExpression, yExpression: $yExpression, zExpression: $zExpression, particleType: $particleType, countExpression: $countExpression, offsetXExpression: $offsetXExpression, offsetYExpression: $offsetYExpression, offsetZExpression: $offsetZExpression, extraExpression: $extraExpression, stopCondition: $stopCondition}, id")
    }

}