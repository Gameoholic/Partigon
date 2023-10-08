package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.util.MatrixUtils

class EnvelopeGroup(
    val envelopeX: Envelope,
    val envelopeY: Envelope,
    val envelopeZ: Envelope,
    val rotationMatrixOptions: List<MatrixUtils.RotationMatrixOptions>
) {
    //todo: support for <3 amount of envelopes.

    fun getEnvelopes(): List<Envelope> {
        envelopeX.envelopeGroup = this
        envelopeY.envelopeGroup = this
        envelopeZ.envelopeGroup = this
        return listOf(envelopeX, envelopeY, envelopeZ)
    }
}