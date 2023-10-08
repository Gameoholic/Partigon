package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.util.MatrixUtils

class EnvelopeGroup(
    val envelopeX: Envelope = StaticEnvelope(Envelope.PropertyType.POS_X, 0.0),
    val envelopeY: Envelope = StaticEnvelope(Envelope.PropertyType.POS_Y, 0.0),
    val envelopeZ: Envelope = StaticEnvelope(Envelope.PropertyType.POS_Z, 0.0),
    val rotationMatrixOptions: List<MatrixUtils.RotationMatrixOptions>
) {

    fun getEnvelopes(): List<Envelope> {
        envelopeX.envelopeGroup = this
        envelopeY.envelopeGroup = this
        envelopeZ.envelopeGroup = this
        return listOf(envelopeX, envelopeY, envelopeZ)
    }
}