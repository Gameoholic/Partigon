package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.Loop
import net.objecthunter.exp4j.ExpressionBuilder
import org.bukkit.util.Vector

class LineEnvelope<T>(
    override val propertyType: Envelope.PropertyType,
    value1: T,
    value2: T,
    override val loop: Loop): Envelope
{

    override var disabled = false
    final override val envelopeExpression: String
    init {
        envelopeExpression = if (value1 is Int && value2 is Int)
            "$value1 + t * ${(value2 - value1).toDouble() / (loop.envelopeDuration - 1)}"
        else if (value1 is Double && value2 is Double)
            "$value1 + t * ${(value2 - value1) / (loop.envelopeDuration - 1)}"
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