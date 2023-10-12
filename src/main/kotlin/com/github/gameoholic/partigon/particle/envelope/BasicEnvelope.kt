package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.Loop
import com.github.gameoholic.partigon.util.*
import com.github.gameoholic.partigon.util.rotation.RotationUtil
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.IllegalArgumentException
import java.lang.RuntimeException


/**
 * Basic envelope that animates a property of a particle animation with an expression.
 * It is preferred not to use this, and use other envelopes that extend this class instead.
 *
 * @param propertyType The property for the envelope to affect.
 * @param envelopeExpression The mathematical expression of the envelope. frame_index is the variable that corresponds to the tick.
 * @param loop The loop to be used with the envelope.
 * @param completion How much of the animation to animate. Must be positive. 1.0 for its entirety.
 * @param nestedEnvelopes The nested envelopes.
 */
open class BasicEnvelope(
    override val propertyType: Envelope.PropertyType,
    override val envelopeExpression: String,
    override val loop: Loop,
    override val completion: Double,
    override val nestedEnvelopes: List<Envelope>
) : Envelope {

    override var envelopeGroup: EnvelopeGroup? = null
        set(value) {
            if (field != null)
                throw RuntimeException("Cannot change envelope's group once it's been assigned to one.")
            field = value
        }

    override fun getValueAt(frameIndex: Int, rawValue: Boolean): Double {
        /**
         * We don't use the actual frame index with the envelope,
         * as the loop might modify it for different purposes.
         * Therefore, we use the LOOPED frame index, which may
         * differ from the original frame index.
         */
        val loopedFrameIndex = loop.applyLoop(frameIndex)

        var updatedEnvelopeExpression = envelopeExpression
        //If there are nested envelopes, apply them recursively
        for (i in nestedEnvelopes.indices) {
            val nestedEnvelopeValue = nestedEnvelopes[i].getValueAt(frameIndex)
            updatedEnvelopeExpression = updatedEnvelopeExpression
                .replace("@ENV_$i@", nestedEnvelopeValue.toString())
        }
        // Apply transformations to the envelope, when belonging to a group. This is needed
        // for shapes like circles, where in order to rotate it, all 3 values are needed
        // to rotate it properly.
        if (!rawValue)
            envelopeGroup?.let {
                if (it.rotationOptions.isEmpty()) return@let

                var newPosition = RotationUtil.applyRotationsForPoint(
                    Triple(
                    it.envelopeX.getValueAt(loopedFrameIndex, rawValue = true) ?: 0.0,
                    it.envelopeY.getValueAt(loopedFrameIndex, rawValue = true) ?: 0.0,
                    it.envelopeZ.getValueAt(loopedFrameIndex, rawValue = true) ?: 0.0
                ), it.rotationOptions, frameIndex)

                return when (propertyType) {
                    Envelope.PropertyType.POS_X -> newPosition.x
                    Envelope.PropertyType.POS_Y -> newPosition.y
                    Envelope.PropertyType.POS_Z -> newPosition.z
                    Envelope.PropertyType.OFFSET_X -> newPosition.x
                    Envelope.PropertyType.OFFSET_Y -> newPosition.y
                    Envelope.PropertyType.OFFSET_Z -> newPosition.z
                    else -> throw IllegalArgumentException("Non-position/offset envelope cannot be inside of an envelope group.")
                }
            }

        return ExpressionBuilder(updatedEnvelopeExpression)
            .variables("frame_index")
            .build()
            .setVariable("frame_index", loopedFrameIndex.toDouble()).evaluate()
    }

}