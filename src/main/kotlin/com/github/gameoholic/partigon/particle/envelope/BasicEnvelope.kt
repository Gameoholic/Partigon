package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.commands.TestCommand
import com.github.gameoholic.partigon.particle.loop.Loop
import net.objecthunter.exp4j.ExpressionBuilder
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import org.apache.commons.math3.linear.MatrixUtils
import kotlin.math.cos
import kotlin.math.sin


/**
 * Basic envelope that animates a property of a particle animation with an expression.
 * @param propertyType The property for the envelope to affect.
 * @param envelopeExpression The mathematical expression of the envelope. frame_index is the variable that corresponds to the tick.
 * @param loop The loop to be used with the envelope.
 * @param isAbsolute Whether the values are absolute, or relative to the original particle's values.
 */
open class BasicEnvelope(
    override val propertyType: Envelope.PropertyType,
    override val loop: Loop,
    override val isAbsolute: Boolean,
    override val completion: Double,
    override val envelopeExpression: String = "",
    override val nestedEnvelopes: List<Envelope> = listOf()
) : Envelope {

    override var disabled = false

    override fun getValueAt(frameIndex: Int): Double? {
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

        val valuePreRotation = ExpressionBuilder(updatedEnvelopeExpression)
            .variables("frame_index")
            .build()
            .setVariable("frame_index", loopedFrameIndex.toDouble()).evaluate()

//        if (propertyType == Envelope.PropertyType.POS_X) {
//            return com.github.gameoholic.partigon.util.MatrixUtils.applyRotationAroundPoint(
//                Vector3D(valuePreRotation, 0.0, 0.0),
//                Vector3D(0.0, 0.0, 0.0),
//                TestCommand.degree,
//                com.github.gameoholic.partigon.util.MatrixUtils.RotationType.Y
//            ).x
//        }
//        else if (propertyType == Envelope.PropertyType.POS_Y) {
//            return com.github.gameoholic.partigon.util.MatrixUtils.applyRotationAroundPoint(
//                Vector3D(0.0, valuePreRotation, 0.0),
//                Vector3D(0.0, 0.0, 0.0),
//                TestCommand.degree,
//                com.github.gameoholic.partigon.util.MatrixUtils.RotationType.Y
//            ).y
//        }
//        else if (propertyType == Envelope.PropertyType.POS_Z) {
//            return com.github.gameoholic.partigon.util.MatrixUtils.applyRotationAroundPoint(
//                Vector3D(0.0, 0.0, valuePreRotation),
//                Vector3D(0.0, 0.0, 0.0),
//                TestCommand.degree,
//                com.github.gameoholic.partigon.util.MatrixUtils.RotationType.Y
//            ).z
//        }

        return ExpressionBuilder(updatedEnvelopeExpression)
            .variables("frame_index")
            .build()
            .setVariable("frame_index", loopedFrameIndex.toDouble()).evaluate()
    }

}