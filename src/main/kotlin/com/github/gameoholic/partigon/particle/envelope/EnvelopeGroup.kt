package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.util.MatrixUtils
import java.lang.RuntimeException

/**
 * Used for grouping multiple position/offset envelopes together.
 * Sets the group of the envelopes provided to this one on init.
 * @param envelopeX The envelope used for the X component.
 * @param envelopeY The envelope used for the Y component.
 * @param envelopeZ The envelope used for the Z component.
 * @param rotationOptions List of rotation options to be used on the components.
 *
 * @throws RuntimeException If one of the envelopes already has a group assigned.
 * @throws IllegalArgumentException If the group doesn't support one of the envelopes' properties.
 */
class EnvelopeGroup(
    val envelopeX: Envelope, //todo: provide default values here, for offset AND position.
    val envelopeY: Envelope,
    val envelopeZ: Envelope,
    val rotationOptions: List<MatrixUtils.RotationOptions> = listOf()
) {
    enum class EnvelopeGroupType { POSITION, OFFSET }

    init {
        if (envelopeX.envelopeGroup != null || envelopeY.envelopeGroup != null || envelopeZ.envelopeGroup != null)
            throw RuntimeException("Envelopes may only have one envelope group assigned to them.")
        if (envelopeX.propertyType != Envelope.PropertyType.POS_X && envelopeX.propertyType != Envelope.PropertyType.OFFSET_X &&
            envelopeY.propertyType != Envelope.PropertyType.POS_Y && envelopeY.propertyType != Envelope.PropertyType.OFFSET_Y &&
            envelopeZ.propertyType != Envelope.PropertyType.POS_Z && envelopeZ.propertyType != Envelope.PropertyType.OFFSET_Z)
            throw IllegalArgumentException("One of the envelopes' properties is invalid for group.")
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