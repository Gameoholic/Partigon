package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.RepeatLoop
import com.github.gameoholic.partigon.util.LoggerUtil


/**
 * An envelope used for holding a constant Numeric value.
 *
 * @param propertyType The property for the envelope to affect.
 * @param value The value.
 */
class ConstantEnvelope(
    propertyType: Envelope.PropertyType,
    value: Number
) :
    BasicEnvelope(
        propertyType, //todo: make loop lengths nullable so I don't have to do this stupid thing here
        "",
        RepeatLoop(Int.MAX_VALUE), // It doesn't matter what value we provide here as long as it's >0. I chose a high number to avoid logger spam when loop resets
        1.0,
        listOf()
    ) {

    override val envelopeExpression: String
    override val nestedEnvelopes: List<Envelope> = listOf()
    init {
        envelopeExpression = value.toString()

        LoggerUtil.debug("Created constant envelope: $envelopeExpression")
    }

}