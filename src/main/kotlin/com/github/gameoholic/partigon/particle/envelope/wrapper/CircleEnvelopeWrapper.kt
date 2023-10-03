package com.github.gameoholic.partigon.particle.envelope.wrapper

import com.github.gameoholic.partigon.particle.envelope.Envelope
import com.github.gameoholic.partigon.particle.envelope.TrigonometricEnvelope
import com.github.gameoholic.partigon.particle.loop.Loop
import java.lang.IllegalArgumentException

object CircleEnvelopeWrapper {
//todo: T can't be dtwo differents?

    /**
     * Represents the orientation of the circle relative to a line connecting 2 points
     * in 2D/3D space.
     * This enum provides a way to specify the direction/orientation of a circle relative
     * to a line from point 1 to point 2.
     *
     * Visualize a line between point 1 and point 2. You're at point 1 and are facing point 2.
     * Setting the orientation to RIGHT will make the circle start at point 1, and
     * go around the line from the right, and finish at point 2.
     */
    enum class CircleOrientation {
        /**
         * 2D Circle to the right of the line.
         */
        RIGHT,
        /**
         * 2D Circle to the left of the line.
         */
        LEFT,
        /**
         * 3D Circle from the right side of the line, below it.
         */
        RIGHT_DOWN,
        /**
         * 3D Circle from the right side of the line, above it.
         */
        RIGHT_UP,
        /**
         * 3D Circle from the left side of the line, below it.
         */
        LEFT_DOWN,
        /**
         * 3D Circle from the left side of the line, above it.
         */
        LEFT_UP
    }

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