package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.Loop
import net.objecthunter.exp4j.ExpressionBuilder

class CurveEnvelope<T>(
    override val propertyType: Envelope.PropertyType,
    value1: T,
    value2: T,
    semiCircles: Double,
    mathOperation: String, //todo: this shouldn't be a string
    width: Double,
    override val loop: Loop,
    override val isAbsolute: Boolean): Envelope
{

    override var disabled = false
    final override val envelopeExpression: String
    init {
        val framePercentage = "frame_index / ${(loop.envelopeDuration - 1)}" //The animation progress, from 0 to 1
        envelopeExpression = if (value1 is Int && value2 is Int) {
            "$value1 + ${(value2 - value1)} * $mathOperation(pi * $framePercentage * $semiCircles) * $width"
        }
        else if (value1 is Double && value2 is Double)
            "$value1 + ${(value2 - value1)} * $mathOperation(pi * $framePercentage * $semiCircles) * $width"
        else
            throw IllegalArgumentException("Unsupported value types.")

    }



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

        return ExpressionBuilder(envelopeExpression)
            .variables("frame_index")
            .build()
            .setVariable("frame_index", loopedFrameIndex.toDouble()).evaluate()
    }


}