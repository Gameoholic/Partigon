package xyz.gameoholic.partigon.particle.envelope

import org.bstats.charts.AdvancedPie
import xyz.gameoholic.partigon.PartigonPlugin
import xyz.gameoholic.partigon.particle.loop.ContinueLoop
import xyz.gameoholic.partigon.particle.loop.Loop
import xyz.gameoholic.partigon.particle.loop.RepeatLoop
import xyz.gameoholic.partigon.util.LoggerUtil
import xyz.gameoholic.partigon.util.inject


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
    private val plugin: PartigonPlugin by inject()

    constructor(value: Number) : this(Envelope.PropertyType.NONE, value)

    override val envelopeExpression: String = value.toString()
    override val nestedEnvelopes: List<Envelope> = listOf()
    init {
        LoggerUtil.debug("Created constant envelope: $envelopeExpression")

        plugin.metrics.addCustomChart(AdvancedPie("envelopesCreated") { mapOf("Constant" to 1) }) // bstats
    }
    override fun copyWithPropertyType(propertyType: Envelope.PropertyType): ConstantEnvelope {
        return ConstantEnvelope(propertyType, value)
    }

}