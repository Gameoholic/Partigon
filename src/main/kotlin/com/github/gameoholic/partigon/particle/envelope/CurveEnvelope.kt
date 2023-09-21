package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.Loop
import com.github.gameoholic.partigon.util.LoggerUtil

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
    override val isAbsolute: Boolean = false): BasicEnvelope(propertyType, loop, isAbsolute)
{

    override val envelopeExpression: String
    override val nestedEnvelopes: List<Envelope>

    enum class TrigFunc(val value: String) {SIN("sin"), COS("cos"), TAN("tan")}

    init {
        if ((value1 !is Int && value1 !is Double && value1 !is Envelope)
            || (value2 !is Int && value2 !is Double && value2 !is Envelope))
            throw IllegalArgumentException("Unsupported value types.")

        val animProgress = "frame_index / ${(loop.envelopeDuration - 1)}" //The animation progress, from 0.0 to 1.0

        val nestedEnvelopesList = mutableListOf<Envelope>()
        var value1String = value1.toString()
        if (value1 is Envelope) {
            /**
             * Since we don't know the actual nested envelope value initialization-time,
             * we give it a placeholder (@ENV_X@) and replace it with the nested envelope's
             * value every tick.
             */
            value1String = "@ENV_0@"
            nestedEnvelopesList.add(value1)
        }

        var value2String = value2.toString()
        if (value2 is Envelope) {
            value2String = "@ENV_${nestedEnvelopesList.size}@"
            nestedEnvelopesList.add(value2)
        }

        envelopeExpression = "$value1String + ($value2String - $value1String) * ${trigFunc.value}(pi * $animProgress * $semiCircles) * $width"
        nestedEnvelopes = nestedEnvelopesList.toList()

        LoggerUtil.debug("Created curve envelope: $envelopeExpression with ${nestedEnvelopes.size} nested envelopes")

    }



}