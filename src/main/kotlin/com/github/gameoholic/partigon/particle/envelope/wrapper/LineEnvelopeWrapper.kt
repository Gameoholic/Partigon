package com.github.gameoholic.partigon.particle.envelope.wrapper

import com.github.gameoholic.partigon.particle.envelope.Envelope
import com.github.gameoholic.partigon.particle.envelope.EnvelopeGroup
import com.github.gameoholic.partigon.particle.envelope.LineEnvelope
import com.github.gameoholic.partigon.particle.loop.Loop
import com.github.gameoholic.partigon.util.*

object LineEnvelopeWrapper {
    /**
     * Envelope wrapper that creates a straight line between 2 points/offsets.
     *
     * @param envelopeGroupType The type of property (offset/position)
     * @param position1 The first position to interpolate (x,y,z).
     * @param position2 The second position to interpolate (x,y,z).
     * @param rotationOptions The list of the rotations to apply to the curve.
     * @param loop The loop to be used with the envelope.
     * @param completion How much of the line will be animated. If set to 1.0, the entire line would be drawn. Must be positive.
     *
     * @return The envelope group used to create the line.
     */
    fun lineEnvelopeGroup(
        envelopeGroupType: EnvelopeGroup.EnvelopeGroupType,
        position1: EnvelopeTriple,
        position2: EnvelopeTriple,
        rotationOptions: List<MatrixUtils.RotationOptions>,
        loop: Loop,
        completion: Double = 1.0,
    ): EnvelopeGroup = EnvelopeGroup(
        LineEnvelope(
            if (envelopeGroupType == EnvelopeGroup.EnvelopeGroupType.POSITION)
                Envelope.PropertyType.POS_X
            else
                Envelope.PropertyType.OFFSET_X,
            position1.x,
            position2.x,
            loop,
            completion
        ),
        LineEnvelope(
            if (envelopeGroupType == EnvelopeGroup.EnvelopeGroupType.POSITION)
                Envelope.PropertyType.POS_Y
            else
                Envelope.PropertyType.OFFSET_Y,
            position1.y,
            position2.y,
            loop,
            completion
        ),
        LineEnvelope(
            if (envelopeGroupType == EnvelopeGroup.EnvelopeGroupType.POSITION)
                Envelope.PropertyType.POS_Z
            else
                Envelope.PropertyType.OFFSET_Z,
            position1.z,
            position2.z,
            loop,
            completion
        ),
        rotationOptions
    )
}