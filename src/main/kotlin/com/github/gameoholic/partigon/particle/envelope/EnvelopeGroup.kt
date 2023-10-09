package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.util.MatrixUtils
import java.lang.RuntimeException

/**
 * Used for grouping multiple position envelopes together.
 * Sets the group of the envelopes provided to this one on init.
 * @param envelopeX The envelope used for the X position.
 * @param envelopeY The envelope used for the Y position.
 * @param envelopeZ The envelope used for the Z position.
 * @param rotationMatrixOptions List of rotation options to be used on the positions.
 *
 * @throws RuntimeException If one of the envelopes already has a group assigned.
 */
class EnvelopeGroup(
    val envelopeX: Envelope = ConstantEnvelope(Envelope.PropertyType.POS_X, 0.0),
    val envelopeY: Envelope = ConstantEnvelope(Envelope.PropertyType.POS_Y, 0.0),
    val envelopeZ: Envelope = ConstantEnvelope(Envelope.PropertyType.POS_Z, 0.0),
    val rotationMatrixOptions: List<MatrixUtils.RotationMatrixOptions> = listOf()
) {
    init {
        if (envelopeX.envelopeGroup != null || envelopeY.envelopeGroup != null || envelopeZ.envelopeGroup != null)
            throw RuntimeException("Envelopes may only have one envelope group assigned to them.")
        envelopeX.envelopeGroup = this
        envelopeY.envelopeGroup = this
        envelopeZ.envelopeGroup = this
    }
    /**
     * Returns a list of all envelopes.
     */
    fun getEnvelopes(): List<Envelope> {
        return listOf(envelopeX, envelopeY, envelopeZ)
    }
}