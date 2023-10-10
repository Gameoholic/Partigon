package com.github.gameoholic.partigon.particle.envelope.wrapper

import com.github.gameoholic.partigon.particle.envelope.*
import com.github.gameoholic.partigon.particle.loop.Loop
import com.github.gameoholic.partigon.util.*
import java.lang.IllegalArgumentException

object CurveEnvelopeWrapper {

    /**
     * Represents the interpolation type of the curve relative to a line connecting 2 points
     * in 3D space.
     * This enum provides a way to specify the direction/orientation of a curve relative
     * to a line from point 1 to point 2.
     *
     * Visualize a line between point 1 and point 2. You're at point 1 and are facing point 2.
     * Setting the orientation to RIGHT will make the curve start at point 1, and
     * go around the line from the right, and finish at point 2.
     */
    enum class CurveOrientation {
        /**
         * 2D curve to the right of the line.
         */
        RIGHT,

        /**
         * 2D curve to the left of the line.
         */
        LEFT,

        /**
         * 3D curve from the right side of the line, below it.
         */
        RIGHT_DOWN,

        /**
         * 3D curve from the right side of the line, above it.
         */
        RIGHT_UP,

        /**
         * 3D curve from the left side of the line, below it.
         */
        LEFT_DOWN,

        /**
         * 3D curve from the left side of the line, above it.
         */
        LEFT_UP
    }

    /**
     * Represents the component (X_AXIS,Y_AXIS,Z_AXIS) of a vector.
     * This is used in curve envelopes with curve orientations to automatically
     * determine the trigonometric function to use.
     */
    enum class VectorComponent { X, Y, Z }

    /**
     * Trigonometric envelope wrapper that when applied on multiple properties,
     * creates a curve between 2 points.
     *
     * @param propertyType The property for the envelope to affect.
     * @param value1 The first value to interpolate.
     * @param value2 The second value to interpolate.
     * @param curveOrientation The orientation of the curve.
     * @param vectorComponent The vector component to be used for this curve property.
     * @param loop The loop to be used with the envelope.
     * @param completion How much of the curve will be animated. If set to 1.0, the entire curve would be drawn. If set to 2.0, it'll draw a half-ellipse. If set to 4.0, it'll draw an ellipse.
     *
     * @throws IllegalArgumentException If an invalid combination of curve orientation & vector component was provided.
     * @return The trigonometric envelope to be used on this property to create the curve.
     */
    fun curveEnvelope(
        propertyType: Envelope.PropertyType,
        value1: Envelope,
        value2: Envelope,
        curveOrientation: CurveOrientation,
        vectorComponent: VectorComponent,
        loop: Loop,
        completion: Double = 1.0,
    ): TrigonometricEnvelope {
        val trigFunc =
            if ((curveOrientation == CurveOrientation.LEFT || curveOrientation == CurveOrientation.LEFT_UP || curveOrientation == CurveOrientation.LEFT_DOWN) && vectorComponent == VectorComponent.X)
                TrigonometricEnvelope.TrigFunc.SIN
            else if ((curveOrientation == CurveOrientation.LEFT || curveOrientation == CurveOrientation.LEFT_UP || curveOrientation == CurveOrientation.LEFT_DOWN) && vectorComponent == VectorComponent.Z)
                TrigonometricEnvelope.TrigFunc.COS
            else if ((curveOrientation == CurveOrientation.RIGHT || curveOrientation == CurveOrientation.RIGHT_UP || curveOrientation == CurveOrientation.RIGHT_DOWN) && vectorComponent == VectorComponent.X)
                TrigonometricEnvelope.TrigFunc.COS
            else if ((curveOrientation == CurveOrientation.RIGHT || curveOrientation == CurveOrientation.RIGHT_UP || curveOrientation == CurveOrientation.RIGHT_DOWN) && vectorComponent == VectorComponent.Z)
                TrigonometricEnvelope.TrigFunc.SIN
            else if ((curveOrientation == CurveOrientation.RIGHT_DOWN || curveOrientation == CurveOrientation.LEFT_DOWN) && vectorComponent == VectorComponent.Y)
                TrigonometricEnvelope.TrigFunc.SIN
            else if ((curveOrientation == CurveOrientation.RIGHT_UP || curveOrientation == CurveOrientation.LEFT_UP) && vectorComponent == VectorComponent.Y)
                TrigonometricEnvelope.TrigFunc.COS
            else
                throw IllegalArgumentException("Invalid combination of curve orientation & vector component")

        return TrigonometricEnvelope(
            propertyType,
            value1,
            value2,
            trigFunc,
            loop,
            completion,
        )
    }

    /**
     * Trigonometric envelope wrapper that when applied on multiple properties,
     * creates a curve between 2 points/offsets.
     * This automatically determines the trigonometric function to use based
     * on the curve orientation and the property type.
     * This method may only be used with the follwing vector property
     * types: POS_X, POS_Y, POS_Z, OFFSET_X, OFFSET_Y, OFFSET_Z, and is preferred
     * if you are dealing with position/offset envelopes.
     *
     * @param propertyType The property for the envelope to affect.
     * @param value1 The first value to interpolate.
     * @param value2 The second value to interpolate.
     * @param curveOrientation The orientation of the curve.
     * @param loop The loop to be used with the envelope.
     * @param completion How much of the curve will be animated. If set to 1.0, the entire curve would be drawn. If set to 2.0, it'll draw a half-ellipse. If set to 4.0, it'll draw an ellipse.
     *
     * @throws IllegalArgumentException If the method doesn't support the property type provided.
     */
    fun curveEnvelope(
        propertyType: Envelope.PropertyType,
        value1: Envelope,
        value2: Envelope,
        curveOrientation: CurveOrientation,
        loop: Loop,
        completion: Double = 1.0,
    ): TrigonometricEnvelope {
        val vectorComponent =
            when (propertyType) {
                Envelope.PropertyType.POS_X -> VectorComponent.X
                Envelope.PropertyType.POS_Y -> VectorComponent.Y
                Envelope.PropertyType.POS_Z -> VectorComponent.Z
                Envelope.PropertyType.OFFSET_X -> VectorComponent.X
                Envelope.PropertyType.OFFSET_Y -> VectorComponent.Y
                Envelope.PropertyType.OFFSET_Z -> VectorComponent.Z
                else -> throw IllegalArgumentException("This method doesn't support this property type, see method docs for more info.")
            }

        return curveEnvelope(
            propertyType,
            value1,
            value2,
            curveOrientation,
            vectorComponent,
            loop,
            completion,
        )
    }


    /**
     * Envelope wrapper that creates a curve between 2 points/offsets
     * in 3D space, with rotations.
     *
     * @param envelopeGroupType The type of property (offset/position)
     * @param position1 The first position to interpolate (x,y,z).
     * @param position2 The second position to interpolate (x,y,z).
     * @param curveOrientation The orientation of the curve.
     * @param rotationOptions The list of the rotations to apply to the curve.
     * @param loop The loop to be used with the envelope.
     * @param completion How much of the curve will be animated. If set to 1.0, the entire curve would be drawn. If set to 2.0, it'll draw a half-ellipse. If set to 4.0, it'll draw an ellipse. Must be positive.
     *
     * @return The envelope group used to create the curve.
     */
    fun curveEnvelopeGroup(
        envelopeGroupType: EnvelopeGroup.EnvelopeGroupType,
        position1: EnvelopeTriple,
        position2: EnvelopeTriple,
        curveOrientation: CurveOrientation,
        rotationOptions: List<MatrixUtils.RotationOptions>,
        loop: Loop,
        completion: Double = 1.0,
    ): EnvelopeGroup = EnvelopeGroup(
        curveEnvelope(
            if (envelopeGroupType == EnvelopeGroup.EnvelopeGroupType.POSITION)
                Envelope.PropertyType.POS_X
            else
                Envelope.PropertyType.OFFSET_X,
            position1.x,
            position2.x,
            curveOrientation,
            loop,
            completion,
        ),
        curveEnvelope(
            if (envelopeGroupType == EnvelopeGroup.EnvelopeGroupType.POSITION)
                Envelope.PropertyType.POS_Y
            else
                Envelope.PropertyType.OFFSET_Y,
            position1.y,
            position2.y,
            curveOrientation,
            loop,
            completion,
        ),
        curveEnvelope(
            if (envelopeGroupType == EnvelopeGroup.EnvelopeGroupType.POSITION)
                Envelope.PropertyType.POS_Z
            else
                Envelope.PropertyType.OFFSET_Z,
            position1.z,
            position2.z,
            curveOrientation,
            loop,
            completion,
        ),
        rotationOptions
    )

}