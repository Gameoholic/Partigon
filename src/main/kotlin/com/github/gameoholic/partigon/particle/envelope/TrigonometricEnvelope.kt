package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.Loop
import com.github.gameoholic.partigon.util.EnvelopeTriple
import com.github.gameoholic.partigon.util.LoggerUtil

//TODO: fix extra frames bug
/**
 * An envelope used for creating curves between two points and circles using trigonometric functions.
 *
 * @param propertyType The property for the envelope to affect.
 * @param value1 The first value to interpolate.
 * @param value2 The second value to interpolate.
 * @param completion How much of the animation will be animated. If set to 1.0, an entire circle would be drawn. If set to 0.5, only half of it, etc. Must be positive.
 * @param trigFunc The trigonometric function to use to animate the curve.
 * @param loop The loop to be used with the envelope.
 *
 * @throws IllegalArgumentException If either value1 or value2 is not a double or an integer.
 */
//todo: if completion below 0.0 throw exception, in all envelopes and wrappers.
open class TrigonometricEnvelope(
    override val propertyType: Envelope.PropertyType,
    value1: Envelope,
    value2: Envelope,
    trigFunc: TrigFunc,
    override val loop: Loop,
    override val completion: Double = 1.0,
) : BasicEnvelope(propertyType, "", loop, completion, listOf()) {

    override val envelopeExpression: String
    override val nestedEnvelopes: List<Envelope>

    enum class TrigFunc(val value: String) { SIN("sin"), COS("cos"), TAN("tan"), COT("cot"), COSEC("cosec"), SEC("sec") }

    init {
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

        //Cos starts at 1 and heads down until pi radians. Because we interpolate the value from down, to up, we must switch the values of the two values.
        envelopeExpression = if (trigFunc == TrigFunc.COS)
            "$value2String + ($value1String - $value2String) * ${trigFunc.value}(pi * $animProgress * $completion)"
        else
            "$value1String + ($value2String - $value1String) * ${trigFunc.value}(pi * $animProgress * $completion)"

        nestedEnvelopes = nestedEnvelopesList.toList()
        LoggerUtil.info("Created curve envelope: $envelopeExpression with ${nestedEnvelopes.size} nested envelopes")

    }


}