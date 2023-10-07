package com.github.gameoholic.partigon.util

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import java.util.Vector
import kotlin.math.cos
import kotlin.math.sin

object MatrixUtils {
    enum class RotationType(val rotationMatrix: (Double) -> (RealMatrix)) {
        X({ angle ->
            val rotationMatrixData = arrayOf(
                doubleArrayOf(1.0, 0.0, 0.0),
                doubleArrayOf(0.0, cos(angle), -sin(angle)),
                doubleArrayOf(0.0, sin(angle), cos(angle))
            )
            MatrixUtils.createRealMatrix(rotationMatrixData)
        }),
        Y({ angle ->
            val rotationMatrixData = arrayOf(
                doubleArrayOf(cos(angle), 0.0, sin(angle)),
                doubleArrayOf(0.0, 1.0, 0.0), //row 2
                doubleArrayOf(-sin(angle), 0.0, cos(angle))
            )
            MatrixUtils.createRealMatrix(rotationMatrixData)
        }),
        Z({ angle ->
            val rotationMatrixData = arrayOf(
                doubleArrayOf(cos(angle), -sin(angle), 0.0),
                doubleArrayOf(sin(angle), cos(angle), 0.0),
                doubleArrayOf(0.0, 0.0, 1.0)
            )
            MatrixUtils.createRealMatrix(rotationMatrixData)
        })

    }

    data class RotationMatrixOptions(val rotPoint: Vector3D, val angle: Double, val rotationType: RotationType)

    fun applyRotationAroundPoint( //todo: replace vector3d with partigon vector.
        point: Vector3D,
        options: RotationMatrixOptions
    ): Vector3D {
        println("ANGLE IS ${options.angle}")
        val angleRadians = Math.toRadians(options.angle)

        val pointMatrix = MatrixUtils.createRealMatrix(
            arrayOf(
                doubleArrayOf(point.x),
                doubleArrayOf(point.y),
                doubleArrayOf(point.z)
            )
        )
        println("Received point ${point.x}, ${point.y}, ${point.z}")

        val rotationPointMatrix = MatrixUtils.createRealMatrix(
            arrayOf(
                doubleArrayOf(options.rotPoint.x),
                doubleArrayOf(options.rotPoint.y),
                doubleArrayOf(options.rotPoint.z)
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
        println("Transforming point by ${options.angle} on ${options.rotationType}, new point is ${newX}, $newY, $newZ")

        return Vector3D(newX, newY, newZ)
    }


}