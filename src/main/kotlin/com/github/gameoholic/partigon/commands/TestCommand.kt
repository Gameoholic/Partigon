package com.github.gameoholic.partigon.commands


import com.github.gameoholic.partigon.particle.PartigonParticle.Companion.partigonParticleBuilder
import com.github.gameoholic.partigon.particle.envelope.Envelope
import com.github.gameoholic.partigon.particle.envelope.EnvelopeGroup
import com.github.gameoholic.partigon.particle.envelope.LineEnvelope
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper.circleEnvelopeGroup
import com.github.gameoholic.partigon.particle.envelope.wrapper.CurveEnvelopeWrapper
import com.github.gameoholic.partigon.particle.envelope.wrapper.CurveEnvelopeWrapper.curveEnvelope
import com.github.gameoholic.partigon.particle.loop.BounceLoop
import com.github.gameoholic.partigon.particle.loop.RepeatLoop
import com.github.gameoholic.partigon.util.DoubleTriple
import com.github.gameoholic.partigon.util.EnvelopePair
import com.github.gameoholic.partigon.util.EnvelopeTriple
import com.github.gameoholic.partigon.util.rotation.RotationUtil
import com.github.gameoholic.partigon.util.Utils.envelope
import com.github.gameoholic.partigon.util.rotation.RotationOptions
import com.github.gameoholic.partigon.util.rotation.RotationType
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender


object TestCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        //Define basic animation
        fun builder() =
            partigonParticleBuilder(Location(Bukkit.getWorld("world"), 0.0, 100.0, 0.0), Particle.END_ROD) {
                envelopes = listOf(
                    *circleEnvelopeGroup(
                        EnvelopeGroup.EnvelopeGroupType.POSITION,
                        EnvelopePair((-3.0).envelope, 0.0.envelope),
                        EnvelopePair(0.0.envelope, (3.0).envelope),
                        CircleEnvelopeWrapper.CircleDirection.RIGHT,
                        RepeatLoop(100),
                    ).getEnvelopes().toTypedArray(),

                    *circleEnvelopeGroup(
                        EnvelopeGroup.EnvelopeGroupType.OFFSET,
                        EnvelopePair((3.0).envelope, 0.0.envelope),
                        EnvelopePair(0.0.envelope, (-3.0).envelope),
                        CircleEnvelopeWrapper.CircleDirection.RIGHT,
                        RepeatLoop(100),
                    ).getEnvelopes().toTypedArray(),
                )
                positionX = curveEnvelope(
                    Envelope.PropertyType.NONE,
                    0.0.envelope, 4.0.envelope,
                    CurveEnvelopeWrapper.CurveOrientation.BELOW,
                    BounceLoop(200),
                )
                //todo: see if we cna get rid of PropertyType
                extra = LineEnvelope(Envelope.PropertyType.NONE, 0.025.envelope, 0.075.envelope, BounceLoop(400))
                count = 0.envelope
            }

        val particle1 = builder().build().start()
        //Mirror all particles:
        val particle2 = builder().apply {
            this.rotationOptions = listOf(
                RotationOptions(EnvelopeTriple(0.0.envelope, 0.0.envelope, 0.0.envelope), 180.0.envelope, RotationType.X_AXIS),
            )
        }.build().start()
        val particle3 = builder().apply {
            this.rotationOptions = listOf(
                RotationOptions(EnvelopeTriple(0.0.envelope, 0.0.envelope, 0.0.envelope), 180.0.envelope, RotationType.Z_AXIS)
            )
        }.build().start()
        val particle4 = builder().apply {
            this.rotationOptions = listOf(
                RotationOptions(EnvelopeTriple(0.0.envelope, 0.0.envelope, 0.0.envelope), 180.0.envelope, RotationType.X_AXIS),
                RotationOptions(EnvelopeTriple(0.0.envelope, 0.0.envelope, 0.0.envelope), 180.0.envelope, RotationType.Z_AXIS)
            )
        }.build().start()
        val particle5 = builder().apply {
            this.rotationOptions = listOf(
                RotationOptions(EnvelopeTriple(0.0.envelope, 0.0.envelope, 0.0.envelope), 270.0.envelope, RotationType.Y_AXIS),
                RotationOptions(EnvelopeTriple(0.0.envelope, 0.0.envelope, 0.0.envelope), 180.0.envelope, RotationType.Z_AXIS)
            )
        }.build().start()
        val particle6 = builder().apply {
            this.rotationOptions = listOf(
                RotationOptions(EnvelopeTriple(0.0.envelope, 0.0.envelope, 0.0.envelope), 270.0.envelope, RotationType.Y_AXIS),
                RotationOptions(EnvelopeTriple(0.0.envelope, 0.0.envelope, 0.0.envelope), 180.0.envelope, RotationType.X_AXIS)
            )
        }.build().start()










        return true
    }


}