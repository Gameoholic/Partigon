package xyz.gameoholic.partigon.util

import xyz.gameoholic.partigon.particle.envelope.ConstantEnvelope
import xyz.gameoholic.partigon.particle.envelope.Envelope

typealias EnvelopeTriple = Triple<Envelope, Envelope, Envelope>
typealias EnvelopePair = Pair<Envelope, Envelope>
typealias DoubleTriple = Triple<Double, Double, Double>
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