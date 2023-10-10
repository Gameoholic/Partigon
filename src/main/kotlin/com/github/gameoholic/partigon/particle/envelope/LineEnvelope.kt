package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.Loop
import com.github.gameoholic.partigon.util.EnvelopeTriple
import com.github.gameoholic.partigon.util.LoggerUtil


/**
 * An envelope used for creating linear lines between 2 points.
 *
 * @param propertyType The property for the envelope to affect.
 * @param value1 The first value to interpolate.
 * @param value2 The second value to interpolate.
 * @param loop The loop to be used with the envelope.
 * @param completion How much of the animation to animate. Must be positive. 1.0 for its entirety.
 */
class LineEnvelope(
    override val propertyType: Envelope.PropertyType,
    value1: Envelope,
    value2: Envelope,
    override val loop: Loop,
    override val completion: Double = 1.0): BasicEnvelope(propertyType, "", loop, completion, listOf())
{
    override val envelopeExpression: String
    override val nestedEnvelopes: List<Envelope>

    init {
        val nestedEnvelopesList = mutableListOf<Envelope>()
        var value1String = value1.toString()
        if (value1 is Envelope) { //todo: it's all envelopes now so it needs to be re-worked
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

        envelopeExpression = "$value1String + frame_index * (($value2String - $value1String) / ${loop.envelopeDuration - 1})"
        nestedEnvelopes = nestedEnvelopesList.toList()

        LoggerUtil.debug("Created line envelope: $envelopeExpression with ${nestedEnvelopes.size} nested envelopes")

    }

}