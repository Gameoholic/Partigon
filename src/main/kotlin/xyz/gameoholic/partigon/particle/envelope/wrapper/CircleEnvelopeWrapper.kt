package xyz.gameoholic.partigon.particle.envelope.wrapper

import xyz.gameoholic.partigon.particle.envelope.*
import xyz.gameoholic.partigon.particle.loop.Loop
import xyz.gameoholic.partigon.util.*
import xyz.gameoholic.partigon.util.rotation.RotationOptions
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
     * Represents the component (X, Z) of a vector.
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
     * creates a circle between 2 points/offsets in the XZ plane.
     *
     * This automatically determines the trigonometric function to use based
     * on the circle orientation and the property type.
     * This method may only be used with the following vector property
     * types: POS_X, POS_Z, OFFSET_X, OFFSET_Z, and is preferred
     * if you are dealing with position/offset envelopes.
     *
     * @param propertyType The property for the envelope to affect.
     * @param value1 The first value to interpolate.
     * @param value2 The second value to interpolate.
     * @param circleDirection The orientation/direction of the circle.
     * @param loop The loop to be used with the envelope.
     * @param completion How much of the circle will be animated. If set to 1.0, an entire circle would be drawn. If set to 0.5, only half of it, etc.
     *
     * @throws IllegalArgumentException If the method doesn't support the property type provided.
     * @return The trigonometric envelope to be used on this property to create the circle.
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
                Envelope.PropertyType.OFFSET_X -> VectorComponent.X
                Envelope.PropertyType.OFFSET_Z -> VectorComponent.Z
                else -> throw IllegalArgumentException("This method doesn't support this property type, see method docs for more info.")
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
     * Envelope wrapper that when applied on multiple properties,
     * creates a circle centered around a point/offset in the XZ plane.
     *
     * This automatically determines the trigonometric function to use based
     * on the property type.
     * This method may only be used with the following vector property
     * types: POS_X, POS_Z, OFFSET_X, OFFSET_Z, and is preferred
     * if you are dealing with position/offset envelopes.
     *
     * @param propertyType The property for the envelope to affect.
     * @param center The center of the circle.
     * @param radius The radius of the circle.
     * @param loop The loop to be used with the envelope.
     * @param completion How much of the circle will be animated. If set to 1.0, an entire circle would be drawn. If set to 0.5, only half of it, etc.
     *
     * @throws IllegalArgumentException If the method doesn't support the property type provided.
     * @return The trigonometric envelope to be used on this property to create the circle.
     */
    fun circleEnvelope(
        propertyType: Envelope.PropertyType,
        center: EnvelopePair,
        radius: Envelope,
        loop: Loop,
        completion: Double = 1.0
    ): TrigonometricEnvelope {
        val vectorComponent =
            when (propertyType) {
                Envelope.PropertyType.POS_X -> VectorComponent.X
                Envelope.PropertyType.POS_Z -> VectorComponent.Z
                Envelope.PropertyType.OFFSET_X -> VectorComponent.X
                Envelope.PropertyType.OFFSET_Z -> VectorComponent.Z
                else -> throw IllegalArgumentException("This method doesn't support this property type, see method docs for more info.")
            }
        return circleEnvelope(propertyType, vectorComponent, center, radius, loop, completion)
    }
    /**
     * Envelope wrapper that when applied on multiple properties,
     * creates a circle centered around a point/offset in the XZ plane.
     *
     * This automatically determines the trigonometric function to use based
     * on the circle orientation and the vector component.
     * This method supports any property type.
     *
     * @param propertyType The property for the envelope to affect.
     * @param center The center of the circle.
     * @param radius The radius of the circle.
     * @param loop The loop to be used with the envelope.
     * @param completion How much of the circle will be animated. If set to 1.0, an entire circle would be drawn. If set to 0.5, only half of it, etc.
     *
     * @throws IllegalArgumentException If the method doesn't support the property type provided.
     * @return The trigonometric envelope to be used on this property to create the circle.
     */
    fun circleEnvelope(
        propertyType: Envelope.PropertyType,
        vectorComponent: VectorComponent,
        center: EnvelopePair,
        radius: Envelope,
        loop: Loop,
        completion: Double = 1.0,
    ): TrigonometricEnvelope {

        var value1: Envelope
        var value2: Envelope
        if (vectorComponent == VectorComponent.X) {
            value1 = BasicEnvelope("@ENV_0@ + @ENV_1@", loop, completion, listOf(center.first, radius))
            value2 = center.first
        }
        else {
            value1 = BasicEnvelope("@ENV_0@", loop, completion, listOf(center.second))
            value2 = BasicEnvelope("@ENV_0@ + @ENV_1@", loop, completion, listOf(center.second, radius))
        }

        return circleEnvelope(
            propertyType,
            value1,
            value2,
            CircleDirection.RIGHT,
            vectorComponent,
            loop,
            completion,
        )
    }


    /**
     * Envelope wrapper that creates a circle between 2 points/offsets
     * in the XZ plane, with rotations.
     *
     * @param envelopeGroupType The type of property (offset/position)
     * @param position1 The first position to interpolate (X, Z).
     * @param position2 The second position to interpolate (X, Z).
     * @param circleDirection The direction of the circle.
     * @param loop The loop to be used with the envelope.
     * @param rotationOptions The list of rotations to apply to the circle.
     * @param completion How much of the circle will be animated. If set to 1.0, an entire circle would be drawn. If set to 0.5, only half of it, etc.
     *
     * @return The envelope group used to create the circle.
     */
    fun circleEnvelopeGroup(
        envelopeGroupType: EnvelopeGroup.EnvelopeGroupType,
        position1: EnvelopePair,
        position2: EnvelopePair,
        circleDirection: CircleDirection,
        loop: Loop,
        rotationOptions: List<RotationOptions> = listOf(),
        completion: Double = 1.0,
    ): EnvelopeGroup = EnvelopeGroup(
        circleEnvelope(
            if (envelopeGroupType == EnvelopeGroup.EnvelopeGroupType.POSITION)
                Envelope.PropertyType.POS_X
            else
                Envelope.PropertyType.OFFSET_X,
            position1.first,
            position2.first,
            circleDirection,
            loop,
            completion,
        ),
        ConstantEnvelope(
            if (envelopeGroupType == EnvelopeGroup.EnvelopeGroupType.POSITION)
                Envelope.PropertyType.POS_Y
            else
                Envelope.PropertyType.OFFSET_Y, 0.0
        ),
        circleEnvelope(
            if (envelopeGroupType == EnvelopeGroup.EnvelopeGroupType.POSITION)
                Envelope.PropertyType.POS_Z
            else
                Envelope.PropertyType.OFFSET_Z,
            position1.second,
            position2.second,
            circleDirection,
            loop,
            completion,
        ),
        rotationOptions
    )

    /**
     * Envelope wrapper that creates a circle around a center point
     * in the XZ plane, with rotations.
     *
     * @param envelopeGroupType The type of property (offset/position)
     * @param center The center of the circle.
     * @param radius The radius of the circle.
     * @param loop The loop to be used with the envelope.
     * @param rotationOptions The list of rotations to apply to the circle.
     * @param completion How much of the circle will be animated. If set to 1.0, an entire circle would be drawn. If set to 0.5, only half of it, etc.
     *
     * @return The envelope group used to create the circle.
     */
    fun circleEnvelopeGroup(
        envelopeGroupType: EnvelopeGroup.EnvelopeGroupType,
        center: EnvelopePair,
        radius: Envelope,
        loop: Loop,
        rotationOptions: List<RotationOptions> = listOf(),
        completion: Double = 1.0,
    ): EnvelopeGroup = EnvelopeGroup(
        circleEnvelope(
            if (envelopeGroupType == EnvelopeGroup.EnvelopeGroupType.POSITION)
                Envelope.PropertyType.POS_X
            else
                Envelope.PropertyType.OFFSET_X,
            center,
            radius,
            loop,
            completion,
        ),
        ConstantEnvelope(
            if (envelopeGroupType == EnvelopeGroup.EnvelopeGroupType.POSITION)
                Envelope.PropertyType.POS_Y
            else
                Envelope.PropertyType.OFFSET_Y, 0.0
        ),
        circleEnvelope(
            if (envelopeGroupType == EnvelopeGroup.EnvelopeGroupType.POSITION)
                Envelope.PropertyType.POS_Z
            else
                Envelope.PropertyType.OFFSET_Z,
            center,
            radius,
            loop,
            completion,
        ),
        rotationOptions
    )


}