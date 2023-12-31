package xyz.gameoholic.partigon.particle.envelope

import org.bstats.charts.AdvancedPie
import org.bstats.charts.SingleLineChart
import xyz.gameoholic.partigon.PartigonPlugin
import xyz.gameoholic.partigon.util.inject
import xyz.gameoholic.partigon.util.rotation.RotationOptions
import xyz.gameoholic.partigon.util.rotation.RotationUtil
import java.lang.RuntimeException

/**
 * Used for grouping multiple position/offset envelopes together.
 * Sets the group of the envelopes provided to this one on init.
 * @param envelopeX The envelope used for the X component.
 * @param envelopeY The envelope used for the Y component.
 * @param envelopeZ The envelope used for the Z component.
 * @param rotationOptions List of rotation options to be used on the components.
 *
 * @throws RuntimeException If one of the envelopes already has a group assigned.
 * @throws IllegalArgumentException If the group doesn't support one of the envelopes' properties.
 */
class EnvelopeGroup(
    val envelopeX: Envelope, //todo: provide default values here, for offset AND position.
    val envelopeY: Envelope,
    val envelopeZ: Envelope,
    var rotationOptions: List<RotationOptions> = listOf() // Needs to be var, so SingularParticle can add additional rotations on top
) {
    private val plugin: PartigonPlugin by inject()

    enum class EnvelopeGroupType { POSITION, OFFSET }

    init {
        if (envelopeX.envelopeGroup != null || envelopeY.envelopeGroup != null || envelopeZ.envelopeGroup != null)
            throw RuntimeException("Envelopes may only have one envelope group assigned to them.")
        if (envelopeX.propertyType != Envelope.PropertyType.POS_X && envelopeX.propertyType != Envelope.PropertyType.OFFSET_X &&
            envelopeY.propertyType != Envelope.PropertyType.POS_Y && envelopeY.propertyType != Envelope.PropertyType.OFFSET_Y &&
            envelopeZ.propertyType != Envelope.PropertyType.POS_Z && envelopeZ.propertyType != Envelope.PropertyType.OFFSET_Z)
            throw IllegalArgumentException("One of the envelopes' properties is invalid for group.")
        envelopeX.envelopeGroup = this
        envelopeY.envelopeGroup = this
        envelopeZ.envelopeGroup = this

        plugin.metrics.addCustomChart(SingleLineChart("envelopeGroupsCreated") { 1 }) // bstats
    }
    /**
     * Returns a list of all envelopes.
     */
    fun getEnvelopes(): List<Envelope> {
        return listOf(envelopeX, envelopeY, envelopeZ)
    }
}