package xyz.gameoholic.partigon.particle.envelope

import org.bstats.charts.AdvancedPie
import org.bstats.charts.SingleLineChart
import xyz.gameoholic.partigon.PartigonPlugin
import xyz.gameoholic.partigon.particle.loop.Loop
import xyz.gameoholic.partigon.util.EnvelopeTriple
import xyz.gameoholic.partigon.util.LoggerUtil
import xyz.gameoholic.partigon.util.inject

//TODO: fix extra frames bug
/**
 * An envelope used for creating curves between two points and circles using trigonometric functions.
 *
 * @param propertyType The property for the envelope to affect.
 * @param value1 The first value to interpolate.
 * @param value2 The second value to interpolate.
 * @param completion How much of the animation will be animated. If set to 1.0, half a wave length. If set to 2.0, one wavelength, etc.
 * @param trigFunc The trigonometric function to use to animate the curve.
 * @param loop The loop to be used with the envelope.
 */
open class TrigonometricEnvelope(
    override val propertyType: Envelope.PropertyType,
    private val value1: Envelope,
    private val value2: Envelope,
    private val trigFunc: TrigFunc,
    override val loop: Loop,
    override val completion: Double = 1.0,
) : BasicEnvelope(propertyType, "", loop, completion, listOf()) {

    private val plugin: PartigonPlugin by inject()

    override val envelopeExpression: String
    override val nestedEnvelopes: List<Envelope>

    enum class TrigFunc(val value: String) { SIN("sin"), COS("cos"), TAN("tan"), COT("cot"), COSEC("cosec"), SEC("sec") }

    /**
     * An envelope used for creating curves between two points and circles using trigonometric functions.
     * This secondary constructor provides the property type as NONE, and should only be used for
     * constructor-parameter envelopes (positionY = ...) or for nested Envelopes.
     *
     * @param value1 The first value to interpolate.
     * @param value2 The second value to interpolate.
     * @param trigFunc The trigonometric function to use to animate the curve.
     * @param loop The loop to be used with the envelope.
     * @param completion How much of the animation will be animated. If set to 1.0, half a wave length. If set to 2.0, one wavelength, etc.
     */
    constructor(
        value1: Envelope,
        value2: Envelope,
        trigFunc: TrigFunc,
        loop: Loop,
        completion: Double = 1.0) : this(Envelope.PropertyType.NONE, value1, value2, trigFunc, loop, completion)

    init {
        val animProgress = "frame_index / ${(loop.envelopeDuration - 1)}" //The animation progress, from 0.0 to 1.0

        val nestedEnvelopesList = mutableListOf<Envelope>()
        // Since we don't know the actual nested envelope value initialization-time,
        // we give it a placeholder (@ENV_X@) and replace it with the nested envelope's
        // value every tick.

        val value1String = "@ENV_0@"
        nestedEnvelopesList.add(value1)

        val value2String = "@ENV_1@"
        nestedEnvelopesList.add(value2)

        // Cos starts at 1 and heads down until pi radians. Because we interpolate the value from down, to up, we must switch the values of the two values.
        envelopeExpression = if (trigFunc == TrigFunc.COS)
            "$value2String + ($value1String - $value2String) * ${trigFunc.value}(pi * $animProgress * $completion)"
        else
            "$value1String + ($value2String - $value1String) * ${trigFunc.value}(pi * $animProgress * $completion)"

        nestedEnvelopes = nestedEnvelopesList.toList()

        plugin.metrics.addCustomChart(AdvancedPie("envelopesCreated") { mapOf("Trigonometric" to 1) }) // bstats
        LoggerUtil.info("Created curve envelope: $envelopeExpression with ${nestedEnvelopes.size} nested envelopes")
    }

    override fun copyWithPropertyType(propertyType: Envelope.PropertyType): TrigonometricEnvelope {
        return TrigonometricEnvelope(propertyType, value1, value2, trigFunc, loop, completion)
    }


}