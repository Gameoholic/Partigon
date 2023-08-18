package com.github.gameoholic.partigon.particleanimation.envelope

import com.github.gameoholic.partigon.particleanimation.loop.Loop
import net.objecthunter.exp4j.ExpressionBuilder

enum class PropertyType { X_EXPRESSION, Y_EXPRESSION, Z_EXPRESSION }


abstract class Envelope(
    val propertyType: PropertyType,
    val envelopeExpression: String,
    val loop: Loop
) {

    /**
     * Returns the envelope's value for a certain frame t.
     * @param t The frame index. If loop is not set to null, the loop index will be calculated and used instead of t.
     */
    fun getValueAt(t: Int): Any {
        var index = t
        //if loop and check end, return for t of loop end reverse
        index = t % loop.duration

        return ExpressionBuilder(envelopeExpression)
            .variables("t")
            .build()
            .setVariable("t", index.toDouble()).evaluate()
    }
}