package xyz.gameoholic.partigon.particle.envelope

import xyz.gameoholic.partigon.particle.loop.ContinueLoop
import xyz.gameoholic.partigon.particle.loop.Loop
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
    private val value: Number
) :
    BasicEnvelope(
        propertyType,
        "",
        ContinueLoop(0),
        1.0,
        listOf()
    ) {

    constructor(value: Number) : this(Envelope.PropertyType.NONE, value)

    override val envelopeExpression: String = value.toString()
    override val nestedEnvelopes: List<Envelope> = listOf()
    init {
        LoggerUtil.debug("Created constant envelope: $envelopeExpression")
    }
    override fun copyWithPropertyType(propertyType: Envelope.PropertyType): ConstantEnvelope {
        return ConstantEnvelope(propertyType, value)
    }

}