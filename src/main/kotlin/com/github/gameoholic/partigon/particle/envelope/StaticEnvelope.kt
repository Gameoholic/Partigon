package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.RepeatLoop
import com.github.gameoholic.partigon.util.LoggerUtil

class StaticEnvelope(
    propertyType: Envelope.PropertyType,
    value: Any
) :
    BasicEnvelope(
        propertyType,
        RepeatLoop(Int.MAX_VALUE), // It doesn't matter what value we provide here as long as it's >0. I chose a high number to avoid logger spam when loop resets
        1.0,
        "",
        listOf()
    ) {

    override val envelopeExpression: String
    override val nestedEnvelopes: List<Envelope>
    init {
        if ((value !is Int && value !is Double && value !is Envelope))
            throw IllegalArgumentException("Unsupported value types.")

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