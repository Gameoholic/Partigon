package com.github.gameoholic.partigon.particleanimation.envelope

import com.github.gameoholic.partigon.particleanimation.loop.Loop
import net.objecthunter.exp4j.ExpressionBuilder

class LineEnvelope<T>(
    propertyType: PropertyType,
    value1: T, //1
    value2: T, //5
    loop: Loop //dur=8
) : Envelope(propertyType,
    "",
    loop) {

    init {
        if (value1 is Int && value2 is Int)
            envelopeExpression = "$value1 + t * ${(value2 - value1) / loop.halfDuration}"
    }



    override fun getValueAt(t: Int): Double {
        var index = t
        if (loop != null)
            index = 5 % loop.duration

        return ExpressionBuilder(envelopeExpression)
            .variables("t")
            .build()
            .setVariable("t", index.toDouble()).evaluate()
    }


}