package com.github.gameoholic.partigon.particle.envelope.wrapper

import com.github.gameoholic.partigon.particle.envelope.Envelope
import com.github.gameoholic.partigon.particle.envelope.TrigonometricEnvelope
import com.github.gameoholic.partigon.particle.loop.Loop
import java.lang.IllegalArgumentException

object CircleEnvelopeWrapper {
//todo: T can't be dtwo differents?

    /**
     * The
     */
    enum class CircleOrientation { RIGHT, LEFT, RIGHT_DOWN, RIGHT_UP, LEFT_DOWN, LEFT_UP }
    enum class VectorComponent { X, Y, Z }

    fun <T> circleEnvelope(
        propertyType: Envelope.PropertyType,
        value1: T,
        value2: T,
        circleLayout: CircleOrientation,
        vectorComponent: VectorComponent,
        loop: Loop,
        completion: Double = 1.0,
        isAbsolute: Boolean = false
    ): TrigonometricEnvelope<T> {
        val trigFunc =
            if ((circleLayout == CircleOrientation.LEFT || circleLayout == CircleOrientation.LEFT_UP || circleLayout == CircleOrientation.LEFT_DOWN) && vectorComponent == VectorComponent.X)
                TrigonometricEnvelope.TrigFunc.SIN
            else if ((circleLayout == CircleOrientation.LEFT || circleLayout == CircleOrientation.LEFT_UP || circleLayout == CircleOrientation.LEFT_DOWN) && vectorComponent == VectorComponent.Z)
                TrigonometricEnvelope.TrigFunc.COS
            else if ((circleLayout == CircleOrientation.RIGHT || circleLayout == CircleOrientation.RIGHT_UP || circleLayout == CircleOrientation.RIGHT_DOWN) && vectorComponent == VectorComponent.X)
                TrigonometricEnvelope.TrigFunc.COS
            else if ((circleLayout == CircleOrientation.RIGHT || circleLayout == CircleOrientation.RIGHT_UP || circleLayout == CircleOrientation.RIGHT_DOWN) && vectorComponent == VectorComponent.Z)
                TrigonometricEnvelope.TrigFunc.SIN
            else if ((circleLayout == CircleOrientation.RIGHT_DOWN || circleLayout == CircleOrientation.LEFT_DOWN) && vectorComponent == VectorComponent.Y)
                TrigonometricEnvelope.TrigFunc.SIN
            else if ((circleLayout == CircleOrientation.RIGHT_UP || circleLayout == CircleOrientation.LEFT_UP) && vectorComponent == VectorComponent.Y)
                TrigonometricEnvelope.TrigFunc.COS
            else
                throw IllegalArgumentException()

        return TrigonometricEnvelope(
            propertyType,
            value1,
            value2,
            trigFunc,
            loop,
            completion * 4,
            isAbsolute
        )
    }

    fun <T> circleEnvelope(
        propertyType: Envelope.PropertyType,
        value1: T,
        value2: T,
        circleLayout: CircleOrientation,
        loop: Loop,
        completion: Double = 1.0,
        isAbsolute: Boolean = false
    ): TrigonometricEnvelope<T> {
        val vectorComponent =
            when (propertyType) {
                Envelope.PropertyType.POS_X -> VectorComponent.X
                Envelope.PropertyType.POS_Y -> VectorComponent.Y
                Envelope.PropertyType.POS_Z -> VectorComponent.Z
                else -> throw IllegalArgumentException()
            }

        return circleEnvelope(
            propertyType,
            value1,
            value2,
            circleLayout,
            vectorComponent,
            loop,
            completion,
            isAbsolute
        )
    }


}