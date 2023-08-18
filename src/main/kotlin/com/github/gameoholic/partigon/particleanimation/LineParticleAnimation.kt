package com.github.gameoholic.partigon.particleanimation

import com.github.gameoholic.partigon.util.LoggerUtil
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.util.Vector
import java.lang.RuntimeException


data class LineParticleSettings(
    val world: World,
    val point1: Vector,
    val point2: Vector,
    val duration: Int,
    val loopDuration: Int = duration,
    val animationLoopEndType: LineParticleLoopEndType = LineParticleLoopEndType.NONE,
    val particleType: Particle,
    val countExpression: String = "1",
    val offsetXExpression: String = "0.0",
    val offsetYExpression: String = "0.0",
    val offsetZExpression: String = "0.0",
    val extraExpression: String = "0.0",
    val stopCondition: (t: Int) -> Boolean = { false }
)

/**
 * Determine what happens to the particle animation at the end of a loop.
 *
 * @property CONTINUE Will continue running the animation in the same direction vector.
 * @property REPEAT Will restart the animation where it was on frame t=0
 * @property REVERSE Will reverse the animation, playing it the same way but from point 2 to point 1. WARNING: This will modify the centerPosition and switch it between point1 and point2 on every loop.
 * @property BOUNCE Similar to REVERSE, but will not repeat frames upon finishing the loop. WARNING: This will modify the centerPosition and switch it between point1 and point2 on every loop.
 * @property END Stops the particle animation.
 * @property NONE Does not loop.
 */
enum class LineParticleLoopEndType { CONTINUE, REPEAT, REVERSE, BOUNCE, END, NONE }


//todo: offsets and double/extra (ParticleType datatype)
class LineParticleAnimation(
    private val lineParticleSettings: LineParticleSettings
) : ParticleAnimationImpl(
    lineParticleSettings.world,
    lineParticleSettings.point1,
    "t * ${(lineParticleSettings.point2.x - lineParticleSettings.point1.x) / (lineParticleSettings.loopDuration - 1)}",
    "t * ${(lineParticleSettings.point2.y - lineParticleSettings.point1.y) / (lineParticleSettings.loopDuration - 1)}",
    "t * ${(lineParticleSettings.point2.z - lineParticleSettings.point1.z) / (lineParticleSettings.loopDuration - 1)}",
    lineParticleSettings.particleType,
    lineParticleSettings.countExpression,
    lineParticleSettings.offsetXExpression,
    lineParticleSettings.offsetYExpression,
    lineParticleSettings.offsetZExpression,
    lineParticleSettings.extraExpression,
    { t -> t >= lineParticleSettings.duration - 1 }
) {

    private var loopIndex = 0

    init {
        LoggerUtil.info(
            "Creating LineParticleAnimation with params: {$lineParticleSettings}",
            id
        )
        if (lineParticleSettings.loopDuration != lineParticleSettings.duration && lineParticleSettings.animationLoopEndType == LineParticleLoopEndType.NONE)
            throw IllegalArgumentException("Particle animation was set to loop but no loop end was specified! lastLoopFrameIndex property was specified, but animationLoopEndType is set to null.")
    }

    override fun onStartFrame(t: Int) {
        super.onStartFrame(t)
        if (t == (lineParticleSettings.loopDuration - 1) * (loopIndex + 1) && lineParticleSettings.animationLoopEndType == LineParticleLoopEndType.BOUNCE) {
            LoggerUtil.debug(
                "Loop $loopIndex finished. Resetting and starting next loop (${lineParticleSettings.loopDuration - 1} frames passed)",
                id
            )
            loopIndex += 1
            if (loopIndex % 2 == 0) {
                centerPosition = lineParticleSettings.point1
                xExpression =
                    "(t + $loopIndex - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point2.x - lineParticleSettings.point1.x) / (lineParticleSettings.loopDuration - 1)}"
                yExpression =
                    "(t + $loopIndex - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point2.y - lineParticleSettings.point1.y) / (lineParticleSettings.loopDuration - 1)}"
                zExpression =
                    "(t + $loopIndex - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point2.z - lineParticleSettings.point1.z) / (lineParticleSettings.loopDuration - 1)}"
            } else {
                centerPosition = lineParticleSettings.point2
                xExpression =
                    "(t + $loopIndex - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point1.x - lineParticleSettings.point2.x) / (lineParticleSettings.loopDuration - 1)}"
                yExpression =
                    "(t + $loopIndex - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point1.y - lineParticleSettings.point2.y) / (lineParticleSettings.loopDuration - 1)}"
                zExpression =
                    "(t + $loopIndex - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point1.z - lineParticleSettings.point2.z) / (lineParticleSettings.loopDuration - 1)}"
            }
            return
        }

        if (t == lineParticleSettings.loopDuration * (loopIndex + 1)) {
            LoggerUtil.debug(
                "Loop $loopIndex finished. Resetting and starting next loop (${lineParticleSettings.loopDuration} frames passed)",
                id
            )
            loopIndex += 1
            when (lineParticleSettings.animationLoopEndType) {
                LineParticleLoopEndType.CONTINUE -> {} //Will continue regardless of what we do
                LineParticleLoopEndType.REPEAT -> {
                    xExpression = "(t - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point2.x - lineParticleSettings.point1.x) / (lineParticleSettings.loopDuration - 1)}"
                    yExpression = "(t - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point2.y - lineParticleSettings.point1.y) / (lineParticleSettings.loopDuration - 1)}"
                    zExpression = "(t - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point2.z - lineParticleSettings.point1.z) / (lineParticleSettings.loopDuration - 1)}"
                }

                LineParticleLoopEndType.REVERSE -> {
                    if (loopIndex % 2 == 0) {
                        centerPosition = lineParticleSettings.point1
                        xExpression =
                            "(t - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point2.x - lineParticleSettings.point1.x) / (lineParticleSettings.loopDuration - 1)}"
                        yExpression =
                            "(t - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point2.y - lineParticleSettings.point1.y) / (lineParticleSettings.loopDuration - 1)}"
                        zExpression =
                            "(t - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point2.z - lineParticleSettings.point1.z) / (lineParticleSettings.loopDuration - 1)}"
                    } else {
                        centerPosition = lineParticleSettings.point2
                        xExpression =
                            "(t - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point1.x - lineParticleSettings.point2.x) / (lineParticleSettings.loopDuration - 1)}"
                        yExpression =
                            "(t - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point1.y - lineParticleSettings.point2.y) / (lineParticleSettings.loopDuration - 1)}"
                        zExpression =
                            "(t - ${loopIndex * lineParticleSettings.loopDuration}) * ${(lineParticleSettings.point1.z - lineParticleSettings.point2.z) / (lineParticleSettings.loopDuration - 1)}"
                    }
                }

                LineParticleLoopEndType.END -> stop()
                else -> {throw RuntimeException("Unsupported LineParticleLoopEndType")}
            }
        }
    }


}