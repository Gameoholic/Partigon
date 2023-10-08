package com.github.gameoholic.partigon.particle.envelope

import com.github.gameoholic.partigon.particle.loop.Loop


/**
 * Represents an envelope for animating properties over time.
 */
interface Envelope {
    enum class PropertyType { POS_X, POS_Y, POS_Z, COUNT, OFFSET_X, OFFSET_Y, OFFSET_Z, NONE }

    /**
     * The property type the envelope controls.
     */
    val propertyType: PropertyType

    /**
     * The loop to use with the envelope.
     */
    val loop: Loop

    /**
     * The envelope math expression, with parameter frame_index.
     */
    val envelopeExpression: String
    /**
     * Whether the envelope will not affect the particle animation anymore.
     */
    var disabled: Boolean

    var envelopeGroup: EnvelopeGroup?

    /**
     * How much of the animation is animated.
     * When used with circle envelopes, for example, if you set it to 0.5,
     * only half of the circle would be animated.
     * The value should be between 0 and 1.
     */
    val completion: Double

    /**
     * Retrieves the envelope's value for a certain frame t. Applies loop.
     *
     * @param frameIndex The frame index.
     * @param rawValue Whether to return the value before any external transformations have been applied.
     * @return The value of the envelope at a frame t, or null if envelope is disabled. If rawValue set to true, returns the value before any external transformations have been applied.
     */
    fun getValueAt(frameIndex: Int, rawValue: Boolean = false): Double?

    /**
     * Nested envelopes are property-less envelopes that produce a value used for the parent envelope(s).
     * They are applied recursively.
     */
    val nestedEnvelopes: List<Envelope>


}