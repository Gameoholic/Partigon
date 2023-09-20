package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.Loop
import net.objecthunter.exp4j.ExpressionBuilder
import org.bukkit.util.Vector


/**
 * An envelope used for creating linear lines between 2 points.
 *
 * @param propertyType The property for the envelope to affect.
 * @param value1 The first value to interpolate.
 * @param value2 The second value to interpolate.
 * @param loop The loop to be used with the envelope.
 * @param isAbsolute Whether the values are absolute, or relative to the original particle's values.
 *
 * @throws IllegalArgumentException If either value1 or value2 is not a double or an integer.
 */
class LineEnvelope<T>(
    override val propertyType: Envelope.PropertyType,
    value1: T,
    value2: T,
    override val loop: Loop,
    override val isAbsolute: Boolean = false): BasicEnvelope(propertyType, loop, "", isAbsolute)
{
    override val envelopeExpression: String

    init {
        if (value1 is Int && value2 is Int)
            envelopeExpression = "$value1 + frame_index * ${(value2 - value1).toDouble() / (loop.envelopeDuration - 1)}"
        else if (value1 is Double && value2 is Double)
            envelopeExpression = "$value1 + frame_index * ${(value2 - value1) / (loop.envelopeDuration - 1)}"
        else if (value1 is Double && value2 is Envelope) {
            envelopeExpression = "$value1 + frame_index * ((@ENV0 - $value1) / (${loop.envelopeDuration} - 1))"
            passedEnvelopes.add(value2)
        }
        else if (value1 is Envelope && value2 is Double) {
            envelopeExpression = "@ENV0 + frame_index * (($value2 - $value1) / (${loop.envelopeDuration} - 1))"
            passedEnvelopes.add(value1)
        }
        else
            throw IllegalArgumentException("Unsupported value types.")

    }

}