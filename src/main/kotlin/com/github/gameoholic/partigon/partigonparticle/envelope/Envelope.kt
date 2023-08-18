package com.github.gameoholic.partigon.partigonparticle.envelope

import com.github.gameoholic.partigon.partigonparticle.loop.Loop
import com.github.gameoholic.partigon.partigonparticle.loop.LoopType
import net.objecthunter.exp4j.ExpressionBuilder

enum class PropertyType { POS_X, POS_Y, POS_Z, COUNT, OFFSET_X, OFFSET_Y, OFFSET_Z }


abstract class Envelope(
    val propertyType: PropertyType,
    val loop: Loop
) {

    lateinit var envelopeExpression: String
        protected set

    /**
     * Returns the envelope's value for a certain frame t.
     * @param t The frame index.
     */
    fun getValueAt(t: Int): Double {
        val loopT = t % loop.duration
        val directionT = loopT % loop.oneDirectionLoopDuration //PREVIOUS WAS  t % loop.oneDirectionLoopDuration
        var index = directionT //The index of the animation we want to get, from the expression //PREVIOUS WAS t % loop.oneDirectionLoopDuration

        when (loop.type) {
            LoopType.REVERSE -> {
                if (loopT >= loop.oneDirectionLoopDuration) //First direction finished, this frame should start reversing the direction and restart the cycle from the last frame back to the 0th frame
                    index = loop.oneDirectionLoopDuration - directionT - 1
            }
            LoopType.BOUNCE -> {
                if (loopT >= loop.oneDirectionLoopDuration) {
                        index = loop.oneDirectionLoopDuration - 2 - directionT
                }
            }
            else -> {}
        }

        return ExpressionBuilder(envelopeExpression)
            .variables("t")
            .build()
            .setVariable("t", index.toDouble()).evaluate()
    }
}