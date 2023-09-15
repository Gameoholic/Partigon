package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.Loop

interface Envelope {
    enum class PropertyType { POS_X, POS_Y, POS_Z, COUNT, OFFSET_X, OFFSET_Y, OFFSET_Z }

    val propertyType: PropertyType
    val loop: Loop
    val envelopeExpression: String
    /**
     * Whether the envelope will not affect the particle animation anymore.
     */
    var disabled: Boolean

    /**
     * Returns the envelope's value for a certain frame t. Applies loop.
     * @param t The frame index.
     * @return The value of the envelope at a frame t, or null if envelope is disabled.
     */
    fun getValueAt(t: Int): Double?

}