package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.Loop
import net.objecthunter.exp4j.ExpressionBuilder
//TODO: fix extra frames bug
/**
 * An envelope used for creating curves between two points and circles using trigonometric functions.
 *
 * @param propertyType The property for the envelope to affect.
 * @param value1 The first value to interpolate.
 * @param value2 The second value to interpolate.
 * @param semiCircles How many semicircles to animate. By default, it's set to 1.0 and creates a curve. To make a circle, set it to 2.0.
 * @param trigFunc The trigonometric function to use to animate the curve.
 * @param width The width of the curve.
 * @param loop The loop to be used with the envelope.
 * @param isAbsolute Whether the values are absolute, or relative to the original particle's values.
 *
 * @throws IllegalArgumentException If either value1 or value2 is not a double or an integer.
 */
class CurveEnvelope<T>(
    override val propertyType: Envelope.PropertyType,
    value1: T,
    value2: T,
    trigFunc: TrigFunc,
    override val loop: Loop,
    semiCircles: Double = 1.0,
    width: Double = 1.0,
    override val isAbsolute: Boolean = false): BasicEnvelope(propertyType, loop, "", isAbsolute)
{
    enum class TrigFunc(val value: String) {SIN("sin"), COS("cos"), TAN("tan")}

    override val envelopeExpression: String
    init {
        //todo: make this better
        val animProgress = "frame_index / ${(loop.envelopeDuration - 1)}" //The animation progress, from 0 to 1

        if (value1 is Double && value2 is Envelope) {
            envelopeExpression = "$value1 + (@ENV0 - $value1) * ${trigFunc.value}(pi * $animProgress * $semiCircles) * $width"
            passedEnvelopes.add(value2)
        }
        else if (value1 is Envelope && value2 is Double) {
            envelopeExpression = "@ENV0 + ($value2 - @ENV0) * ${trigFunc.value}(pi * $animProgress * $semiCircles) * $width"
            passedEnvelopes.add(value1)
        }
        else if ((value1 !is Int && value1 !is Double) || (value2 !is Int && value2 !is Double))
            throw IllegalArgumentException("Unsupported value types.")
        else {
            val val1 = value1 as Double
            val val2 = value2 as Double
            envelopeExpression = "$val1 + ${(val2 - val1)} * ${trigFunc.value}(pi * $animProgress * $semiCircles) * $width"
        }



    }
    


}