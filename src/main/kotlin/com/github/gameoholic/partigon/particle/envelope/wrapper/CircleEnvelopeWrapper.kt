package com.github.gameoholic.partigon.particle.envelope.wrapper

import com.github.gameoholic.partigon.particle.envelope.*
import com.github.gameoholic.partigon.particle.loop.Loop
import com.github.gameoholic.partigon.util.*
import java.lang.IllegalArgumentException

object CircleEnvelopeWrapper {

    /**
     * Represents the direction of the circle relative to a line connecting 2 points
     * on the XZ plane.
     * This enum provides a way to specify the direction of a circle relative
     * to a line from point 1 to point 2.
     *
     * Visualize a line between point 1 and point 2. You're at point 1 and are facing point 2.
     * Setting the orientation to RIGHT will make the circle start at point 1, and
     * go around the line from the right, and finish at point 2.
     */
    enum class CircleDirection {
        /**
         * 2D Circle to the right of the line.
         */
        RIGHT,

        /**
         * 2D Circle to the left of the line.
         */
        LEFT,
    }

    /**
     * Represents the component (X,Z) of a vector.
     * This is used in circle envelopes with circle directions to automatically
     * determine the trigonometric function to use.
     */
    enum class VectorComponent { X, Z }

    /**
     * Envelope wrapper that when applied on multiple properties,
     * creates a circle between 2 points on the XZ plane.
     *
     * @param propertyType The property for the envelope to affect.
     * @param value1 The first value to interpolate.
     * @param value2 The second value to interpolate.
     * @param circleDirection The direction of the circle.
     * @param vectorComponent The vector component to be used for this circle property.
     * @param loop The loop to be used with the envelope.
     * @param completion How much of the circle will be animated. If set to 1.0, an entire circle would be drawn. If set to 0.5, only half of it, etc.
     *
     * @throws IllegalArgumentException If an invalid combination of circle orientation & vector component was provided.
     * @return The trigonometric envelope to be used on this property to create the circle.
     */
    fun circleEnvelope(
        propertyType: Envelope.PropertyType,
        value1: Envelope,
        value2: Envelope,
        circleDirection: CircleDirection,
        vectorComponent: VectorComponent,
        loop: Loop,
        completion: Double = 1.0,
    ): TrigonometricEnvelope {
        val trigFunc =
            if (circleDirection == CircleDirection.LEFT && vectorComponent == VectorComponent.X)
                TrigonometricEnvelope.TrigFunc.SIN
            else if (circleDirection == CircleDirection.LEFT && vectorComponent == VectorComponent.Z)
                TrigonometricEnvelope.TrigFunc.COS
            else if (circleDirection == CircleDirection.RIGHT && vectorComponent == VectorComponent.X)
                TrigonometricEnvelope.TrigFunc.COS
            else if (circleDirection == CircleDirection.RIGHT && vectorComponent == VectorComponent.Z)
                TrigonometricEnvelope.TrigFunc.SIN
            else
                throw IllegalArgumentException("Invalid combination of circle direction & vector component")

        return TrigonometricEnvelope(
            propertyType,
            value1,
            value2,
            trigFunc,
            loop,
            completion * 2,
        )
    }

    /**
     * Envelope wrapper that when applied on multiple properties,
     * creates a circle between 2 points in the XZ plane.
     *
     * This automatically determines the trigonometric function to use based
     * on the circle orientation and the property type.
     * This method may only be used with vector property types (POS_X, POS_Y, POS_Z), and is preferred
     * if you are dealing with position envelopes.
     *
     * @param propertyType The property for the envelope to affect.
     * @param value1 The first value to interpolate.
     * @param value2 The second value to interpolate.
     * @param circleDirection The orientation/direction of the circle.
     * @param loop The loop to be used with the envelope.
     * @param completion How much of the circle will be animated. If set to 1.0, an entire circle would be drawn. If set to 0.5, only half of it, etc.
     *
     * @throws IllegalArgumentException If the method doesn't support the property type provided.
     * @return The trigonometric envelope to be used on this position property to create the circle.
     */
    fun circleEnvelope(
        propertyType: Envelope.PropertyType,
        value1: Envelope,
        value2: Envelope,
        circleDirection: CircleDirection,
        loop: Loop,
        completion: Double = 1.0,
    ): TrigonometricEnvelope {
        val vectorComponent =
            when (propertyType) {
                Envelope.PropertyType.POS_X -> VectorComponent.X
                Envelope.PropertyType.POS_Z -> VectorComponent.Z
                else -> throw IllegalArgumentException()
            }

        return circleEnvelope(
            propertyType,
            value1,
            value2,
            circleDirection,
            vectorComponent,
            loop,
            completion,
        )
    }


    /**
     * Envelope wrapper that creates a circle between 2 points
     * in the XZ plane, with rotations.
     *
     * @param position1 The first position to interpolate (x,z).
     * @param position2 The second position to interpolate (x,z).
     * @param circleDirection The direction of the circle.
     * @param rotationOptions The list of the rotations to apply to the circle.
     * @param loop The loop to be used with the envelope.
     * @param completion How much of the circle will be animated. If set to 1.0, an entire circle would be drawn. If set to 0.5, only half of it, etc.
     *
     * @return The envelope group used to create the circle.
     */
    fun circleEnvelopeGroup(
        position1: EnvelopePair,
        position2: EnvelopePair,
        circleDirection: CircleDirection,
        rotationOptions: List<MatrixUtils.RotationMatrixOptions>,
        loop: Loop,
        completion: Double = 1.0,
    ): EnvelopeGroup = EnvelopeGroup(
        circleEnvelope(
            Envelope.PropertyType.POS_X,
            position1.first,
            position2.first,
            circleDirection,
            loop,
            completion,
        ),
        ConstantEnvelope(Envelope.PropertyType.POS_Y, 0.0),
        circleEnvelope(
            Envelope.PropertyType.POS_Z,
            position1.second,
            position2.second,
            circleDirection,
            loop,
            completion,
        ),
        rotationOptions
    )


}