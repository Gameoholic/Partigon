package com.github.gameoholic.partigon.util.rotation

import com.github.gameoholic.partigon.util.DoubleTriple

/**
 * The options for the rotation.
 * @param rotationPoint The point of reference for the rotation. It will rotate around it.
 * @param angle The angle of the rotation, in degrees.
 * @param rotationType The type of rotation.
 */
data class RotationOptions(
    val rotationPoint: DoubleTriple,
    val angle: Double,
    val rotationType: RotationType
)
