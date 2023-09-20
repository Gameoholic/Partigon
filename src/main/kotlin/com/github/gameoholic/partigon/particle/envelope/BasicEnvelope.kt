package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.Loop
import net.objecthunter.exp4j.ExpressionBuilder


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
    override val envelopeExpression: String,
    override val isAbsolute: Boolean
) : Envelope {
    override var disabled = false
    override val passedEnvelopes = mutableListOf<Envelope>()
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

        //todo: the below can return null, if this ahppens we disable.
        var newEnvelopeExpression = envelopeExpression
        if (passedEnvelopes.size > 0)
            newEnvelopeExpression = envelopeExpression.replace("@ENV0", passedEnvelopes[0].getValueAt(frameIndex).toString())
        return ExpressionBuilder(newEnvelopeExpression)
            .variables("frame_index")
            .build()
            .setVariable("frame_index", loopedFrameIndex.toDouble()).evaluate()
    }

}