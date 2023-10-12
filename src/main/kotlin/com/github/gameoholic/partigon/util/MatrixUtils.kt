package com.github.gameoholic.partigon.util
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import kotlin.math.cos
import kotlin.math.sin
//todo: figure out extensions and this rotationtype enum down below
object MatrixUtils {

    /**
     * The type of the rotation to use. It will then rotate around the axis
     * of the component.
     */
    enum class RotationType(val rotationMatrix: (Double) -> (RealMatrix)) {
        /**
         * Will rotate around the X axis.
         */
        X_AXIS({ angle ->
            val rotationMatrixData = arrayOf(
                doubleArrayOf(1.0, 0.0, 0.0),
                doubleArrayOf(0.0, cos(angle), -sin(angle)),
                doubleArrayOf(0.0, sin(angle), cos(angle))
            )
            MatrixUtils.createRealMatrix(rotationMatrixData)
        }),
        /**
         * Will rotate around the Y axis.
         */
        Y_AXIS({ angle ->
            val rotationMatrixData = arrayOf(
                doubleArrayOf(cos(angle), 0.0, sin(angle)),
                doubleArrayOf(0.0, 1.0, 0.0), //row 2
                doubleArrayOf(-sin(angle), 0.0, cos(angle))
            )
            MatrixUtils.createRealMatrix(rotationMatrixData)
        }),
        /**
         * Will rotate around the Z axis.
         */
        Z_AXIS({ angle ->
            val rotationMatrixData = arrayOf(
                doubleArrayOf(cos(angle), -sin(angle), 0.0),
                doubleArrayOf(sin(angle), cos(angle), 0.0),
                doubleArrayOf(0.0, 0.0, 1.0)
            )
            MatrixUtils.createRealMatrix(rotationMatrixData)
        })

    }
    /**
     * The options for the rotation.
     * @param rotationPoint The point of reference for the rotation. It will rotate around it.
     * @param angle The angle of the rotation, in degrees.
     * @param rotationType The type of rotation.
     */
    @Serializable
    data class RotationOptions(
        val rotationPoint: DoubleTriple,
        val angle: Double,
        val rotationType: RotationType
    )


    /**
     * Applies a rotation to a point, given a point and rotation options.
     * @param point The point to rotate.
     * @param options The rotation options.
     * @return The new point with the rotation applied.
     */
    fun applyRotationForPoint(
        point: DoubleTriple,
        options: RotationOptions
    ): DoubleTriple {
        val angleRadians = Math.toRadians(options.angle)

        val pointMatrix = MatrixUtils.createRealMatrix(
            arrayOf(
                doubleArrayOf(point.x),
                doubleArrayOf(point.y),
                doubleArrayOf(point.z)
            )
        )

        val rotationPointMatrix = MatrixUtils.createRealMatrix(
            arrayOf(
                doubleArrayOf(options.rotationPoint.x),
                doubleArrayOf(options.rotationPoint.y),
                doubleArrayOf(options.rotationPoint.z)
            )
        )

        /**
         * Imagine moving both the point and origin point the same distance so that the rotation point is at the origin.
         * We apply the transformation to get only the point to that position.
         */
        val transformedPointMatrix =
            pointMatrix.add(rotationPointMatrix.scalarMultiply(-1.0))

        val rotationMatrix = options.rotationType.rotationMatrix.invoke(angleRadians)

        /**
         * We multiply the matrices to rotate the point.
         * Keep in mind it's still transformed to be origin relative to
         * the rotation point.
         */
        val rotatedPointMatrix = rotationMatrix.multiply(transformedPointMatrix)

        // We transform the point back to where it's supposed to be, "cancelling" the first transformation
        val newPointMatrix = rotatedPointMatrix.add(rotationPointMatrix)

        val newX = newPointMatrix.data[0][0]
        val newY = newPointMatrix.data[1][0]
        val newZ = newPointMatrix.data[2][0]

        return DoubleTriple(newX, newY, newZ)
    }

    /**
     * Applies multiple rotations to a point, given a point and a list of rotation options.
     * @param point The point to rotate.
     * @param options The rotation options list.
     * @return The new point with all the rotations applied.
     */
    fun applyRotationsForPoint(
        point: DoubleTriple,
        options: List<RotationOptions>
    ): DoubleTriple {
        var newPoint = point
        options.forEach {
            newPoint = applyRotationForPoint(newPoint, it)
        }
        return newPoint
    }

}