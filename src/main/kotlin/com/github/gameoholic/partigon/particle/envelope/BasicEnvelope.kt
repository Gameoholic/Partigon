package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.Loop
import com.github.gameoholic.partigon.util.MatrixUtils
import com.github.gameoholic.partigon.util.Utils
import net.objecthunter.exp4j.ExpressionBuilder
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import java.lang.IllegalArgumentException
import java.lang.RuntimeException


/**
 * Basic envelope that animates a property of a particle animation with an expression.
 * @param propertyType The property for the envelope to affect.
 * @param envelopeExpression The mathematical expression of the envelope. frame_index is the variable that corresponds to the tick.
 * @param loop The loop to be used with the envelope.
 */
open class BasicEnvelope(
    override val propertyType: Envelope.PropertyType,
    override val loop: Loop,
    override val completion: Double,
    override val envelopeExpression: String = "",
    override val nestedEnvelopes: List<Envelope> = listOf()
) : Envelope {

    override var disabled = false
    override var envelopeGroup: EnvelopeGroup? = null
        set(value) {
            if (field != null)
                throw RuntimeException("Cannot change envelope's group once it's been assigned to one.")
            field = value
        }

    override fun getValueAt(frameIndex: Int, rawValue: Boolean): Double? {
        if (disabled)
            return null

        /**
         * We don't use the actual frame index with the envelope,
         * as the loop might modify it for different purposes.
         * Therefore, we use the LOOPED frame index, which may
         * differ from the original frame index.
         */
        val loopedFrameIndex = loop.applyLoop(frameIndex)
        if (loopedFrameIndex == null) { //If envelope should be disabled because of loop condition
            disabled = true
            return null
        }

        var updatedEnvelopeExpression = envelopeExpression
        //If there are nested envelopes, apply them recursively
        for (i in nestedEnvelopes.indices) {
            val nestedEnvelopeValue = nestedEnvelopes[i].getValueAt(frameIndex)
            if (nestedEnvelopeValue == null) { //If envelope should be disabled because of nested envelope's loop condition
                disabled = true
                return null
            }
            updatedEnvelopeExpression = updatedEnvelopeExpression
                .replace("@ENV_$i@", nestedEnvelopeValue.toString())
        }

        // Apply transformations to the envelope, when belonging to a group. This is needed
        // for shapes like circles, where in order to rotate it, all 3 values are needed
        // to rotate it properly.
        if (!rawValue)
            envelopeGroup?.let {
                if (it.rotationMatrixOptions == null) return@let


                // get positions from other envelopes
                var newPosition = Utils.Vector(
                    it.envelopeX.getValueAt(loopedFrameIndex, rawValue = true) ?: 0.0,
                    it.envelopeY.getValueAt(loopedFrameIndex, rawValue = true) ?: 0.0,
                    it.envelopeZ.getValueAt(loopedFrameIndex, rawValue = true) ?: 0.0
                )
                // apply all rotation matrices
                it.rotationMatrixOptions.forEach {
                    rotationOptions ->
                    newPosition = MatrixUtils.applyRotationAroundPoint(
                        Utils.Vector(
                            newPosition.x,
                            newPosition.y,
                            newPosition.z
                        ),
                        rotationOptions
                    )
                }

                return when (propertyType) {
                    Envelope.PropertyType.POS_X -> newPosition.x
                    Envelope.PropertyType.POS_Y -> newPosition.y
                    Envelope.PropertyType.POS_Z -> newPosition.z
                    else -> throw IllegalArgumentException("Non-position envelope cannot be inside of an envelope group.")
                }
            }
        //todo: apply individual transformations

        return ExpressionBuilder(updatedEnvelopeExpression)
            .variables("frame_index")
            .build()
            .setVariable("frame_index", loopedFrameIndex.toDouble()).evaluate()
    }

}