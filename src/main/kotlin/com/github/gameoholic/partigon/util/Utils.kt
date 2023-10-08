package com.github.gameoholic.partigon.util

import com.github.gameoholic.partigon.particle.envelope.ConstantEnvelope
import com.github.gameoholic.partigon.particle.envelope.Envelope

object Utils {

    data class Vector<T>(val x: T, val y: T, val z: T)
    data class Pair<T>(val x: T, val z: T)

    val <T> Triple<T,*,*>.x
        get(): T = first
    val <T> Triple<*,T,*>.y
        get(): T = second
    val <T> Triple<*,*,T>.z
        get(): T = third

    val Number.envelope: ConstantEnvelope
        get() = ConstantEnvelope(Envelope.PropertyType.NONE, "$this")
}