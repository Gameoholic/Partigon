package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.util.MatrixUtils

class EnvelopeGroup(val rotationMatrixOptions: MatrixUtils.RotationMatrixOptions?) {
    lateinit var envelopeX: Envelope
    lateinit var envelopeY: Envelope
    lateinit var envelopeZ: Envelope

}