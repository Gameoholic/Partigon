package xyz.gameoholic.partigon.particle.envelope

import xyz.gameoholic.partigon.particle.loop.ContinueLoop
import xyz.gameoholic.partigon.particle.loop.RepeatLoop
import xyz.gameoholic.partigon.util.LoggerUtil


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
        propertyType,
        "",
        ContinueLoop(0),
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