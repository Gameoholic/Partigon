package xyz.gameoholic.partigon.util.rotation

import xyz.gameoholic.partigon.particle.envelope.Envelope
import xyz.gameoholic.partigon.util.EnvelopeTriple

/**
 * The options for the rotation.
 * @param rotationPoint The point of reference for the rotation. It will rotate around it.
 * @param angle The angle of the rotation, in degrees.
 * @param rotationType The type of rotation.
 */
data class RotationOptions(
    val rotationPoint: EnvelopeTriple,
    val angle: Envelope,
    val rotationType: RotationType
)
