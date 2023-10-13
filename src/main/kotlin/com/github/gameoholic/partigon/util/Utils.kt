package com.github.gameoholic.partigon.util

import com.github.gameoholic.partigon.particle.envelope.ConstantEnvelope
import com.github.gameoholic.partigon.particle.envelope.Envelope

typealias EnvelopeTriple = Triple<Envelope, Envelope, Envelope>
typealias DoubleTriple = Triple<Double, Double, Double>
typealias EnvelopePair = Pair<Envelope, Envelope>
val <T> Triple<T,*,*>.x
    get(): T = first
val <T> Triple<*,T,*>.y
    get(): T = second
val <T> Triple<*,*,T>.z
    get(): T = third



object Utils {
    val Number.envelope: ConstantEnvelope
        get() = ConstantEnvelope(Envelope.PropertyType.NONE, this)
}