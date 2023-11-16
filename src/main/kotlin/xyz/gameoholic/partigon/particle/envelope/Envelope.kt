package xyz.gameoholic.partigon.particle.envelope

import xyz.gameoholic.partigon.particle.loop.Loop
import xyz.gameoholic.partigon.util.EnvelopeTriple


/**
 * Represents an envelope for animating properties over time.
 */
interface Envelope {
    enum class PropertyType { POS_X, POS_Y, POS_Z, COUNT, OFFSET_X, OFFSET_Y, OFFSET_Z, EXTRA, NONE }

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
     * The envelope group the envelope belongs to. Null if doesn't belong to one.
     */
    var envelopeGroup: EnvelopeGroup?

    /**
     * How much of the animation is animated.
     * The value should be positive. Set to 1.0 for the full animation.
     */
    val completion: Double

    /**
     * Retrieves the envelope's value for a certain frame t. Applies loop.
     *
     * @param frameIndex The frame index.
     * @param rawValue Whether to return the value before any external transformations have been applied.
     * @return The value of the envelope at a frame t. If rawValue set to true, returns the value before any external transformations have been applied.
     */
    fun getValueAt(frameIndex: Int, rawValue: Boolean = false): Double

    /**
     * Nested envelopes are property-less envelopes that produce a value used for the parent envelope(s).
     * They are applied recursively.
     */
    val nestedEnvelopes: List<Envelope>

    /**
     * Returns a new instance of this class, with a different envelope property.
     */
    fun copyWithPropertyType(propertyType: PropertyType): Envelope


}