package com.github.gameoholic.partigon.partigonparticle.envelope

import com.github.gameoholic.partigon.partigonparticle.loop.Loop
import org.bukkit.util.Vector

class LineEnvelope<T>(
    propertyType: PropertyType,
    value1: T, //1
    value2: T, //5
    loop: Loop //dur=8
) : Envelope(
    propertyType,
    loop
) {

    init {
        if (value1 is Int && value2 is Int)
            envelopeExpression = "$value1 + t * ${(value2 - value1).toDouble() / (loop.oneDirectionLoopDuration - 1)}"
        else if (value1 is Double && value2 is Double)
            envelopeExpression = "$value1 + t * ${(value2 - value1) / (loop.oneDirectionLoopDuration - 1)}"
        else if (value1 is Vector && value2 is Vector)
            envelopeExpression =
                "$value1 + t * ${(value2.clone().add(value1.multiply(-1))).multiply(1 / (loop.oneDirectionLoopDuration - 1))}"
        else
            throw IllegalArgumentException("Unsupported value types.")

    }


}