package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.RepeatLoop
import com.github.gameoholic.partigon.util.LoggerUtil


/**
 * An envelope used for holding a constant value.
 *
 * @param propertyType The property for the envelope to affect.
 * @param value The value.
 *
 * @throws IllegalArgumentException If value is neither a Number nor an Envelope.
 */
class ConstantEnvelope( //todo: remopve support for passing envelopes .non ested ones.
    propertyType: Envelope.PropertyType,
    value: Any
) :
    BasicEnvelope(
        propertyType, //todo: make loop lengths nullable so I don't have to do this stupid thing here
        "",
        RepeatLoop(Int.MAX_VALUE), // It doesn't matter what value we provide here as long as it's >0. I chose a high number to avoid logger spam when loop resets
        1.0,
        listOf()
    ) {

    override val envelopeExpression: String
    override val nestedEnvelopes: List<Envelope>
    init {
        if ((value !is Number && value !is Envelope))
            throw IllegalArgumentException("Unsupported value type $value")

        val nestedEnvelopesList = mutableListOf<Envelope>()
        var valueString = value.toString()
        if (value is Envelope) {
            /**
             * Since we don't know the actual nested envelope value initialization-time,
             * we give it a placeholder (@ENV_X@) and replace it with the nested envelope's
             * value every tick.
             */
            valueString = "@ENV_0@"
            nestedEnvelopesList.add(value)
        }

        envelopeExpression = "$valueString"
        nestedEnvelopes = nestedEnvelopesList.toList()

        LoggerUtil.debug("Created static envelope: $envelopeExpression with ${nestedEnvelopes.size} nested envelopes")

    }

}