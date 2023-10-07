package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.commands.TestCommand
import com.github.gameoholic.partigon.particle.loop.Loop
import net.objecthunter.exp4j.ExpressionBuilder
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import org.apache.commons.math3.linear.MatrixUtils
import org.bukkit.Bukkit
import kotlin.math.cos
import kotlin.math.sin


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

        val theta = TestCommand.degree //angle in deg
        val thetaRadians = Math.toRadians(theta)

//        val matrixData = arrayOf( //Rx(theta)
//            doubleArrayOf(1.0, 0.0, 0.0), //row 1
//            doubleArrayOf(0.0, cos(thetaRadians), -sin(thetaRadians)), //row 2
//            doubleArrayOf(0.0, sin(thetaRadians), cos(thetaRadians)) //row 3
//        )
//        val matrixData = arrayOf( //Ry(theta)
//            doubleArrayOf(cos(thetaRadians), 0.0, sin(thetaRadians)), //row 1
//            doubleArrayOf(0.0, 1.0, 0.0), //row 2
//            doubleArrayOf(-sin(thetaRadians), 0.0, cos(thetaRadians)) //row 3
//        )
        val matrixData = arrayOf( //Rz(theta)
            doubleArrayOf(cos(thetaRadians), -sin(thetaRadians), 0.0), //row 1
            doubleArrayOf(sin(thetaRadians), cos(thetaRadians), 0.0), //row 2
            doubleArrayOf(0.0, 0.0, 1.0) //row 3
        )
        val m = MatrixUtils.createRealMatrix(matrixData)


        val matrixData2 = arrayOf( //Points
            doubleArrayOf(sin(loopedFrameIndex.toDouble()/6)), //row 1
            doubleArrayOf(0.0), //row 2
            doubleArrayOf(cos(loopedFrameIndex.toDouble()/6)) //row 3
        )
        val m2 = MatrixUtils.createRealMatrix(matrixData2)

        val newM = m.multiply(m2)
        //todo: rotate around anchor point.


//test
        val valuePreRotation = ExpressionBuilder(updatedEnvelopeExpression)
            .variables("frame_index")
            .build()
            .setVariable("frame_index", loopedFrameIndex.toDouble()).evaluate()

        if (propertyType == Envelope.PropertyType.POS_X) {
            return com.github.gameoholic.partigon.util.MatrixUtils.applyRotationAroundPoint(
                Vector3D(sin(loopedFrameIndex.toDouble()/6), 0.0, 0.0),
                Vector3D(0.0, 0.0, 0.0),
                TestCommand.degree,
                com.github.gameoholic.partigon.util.MatrixUtils.RotationType.X
            ).x
        }
        else if (propertyType == Envelope.PropertyType.POS_Y) {
            return com.github.gameoholic.partigon.util.MatrixUtils.applyRotationAroundPoint(
                Vector3D(0.0, sin(loopedFrameIndex.toDouble()/6), cos(loopedFrameIndex.toDouble()/6)),
                Vector3D(0.0, 0.0, 0.0),
                TestCommand.degree,
                com.github.gameoholic.partigon.util.MatrixUtils.RotationType.X
            ).y
        }
        else if (propertyType == Envelope.PropertyType.POS_Z) {
            return com.github.gameoholic.partigon.util.MatrixUtils.applyRotationAroundPoint(
                Vector3D(0.0, 0.0, cos(loopedFrameIndex.toDouble()/6)),
                Vector3D(0.0, 0.0, 0.0),
                TestCommand.degree,
                com.github.gameoholic.partigon.util.MatrixUtils.RotationType.X
            ).z
        }
        //test^


        if (envelopeExpression == "0")
            return newM.data[0][0]
        else if (envelopeExpression == "1")
            return newM.data[1][0]
        else if (envelopeExpression == "2")
            return newM.data[2][0]

        return ExpressionBuilder(updatedEnvelopeExpression)
            .variables("frame_index")
            .build()
            .setVariable("frame_index", loopedFrameIndex.toDouble()).evaluate()
    }

}