package com.github.gameoholic.partigon.commands


import com.github.gameoholic.partigon.particle.PartigonParticle.Companion.partigonParticle
import com.github.gameoholic.partigon.particle.PartigonParticle.Companion.partigonParticleBuilder
import com.github.gameoholic.partigon.particle.envelope.Envelope
import com.github.gameoholic.partigon.particle.envelope.EnvelopeGroup
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper.circleEnvelopeGroup
import com.github.gameoholic.partigon.particle.envelope.wrapper.CurveEnvelopeWrapper
import com.github.gameoholic.partigon.particle.envelope.wrapper.CurveEnvelopeWrapper.curveEnvelope
import com.github.gameoholic.partigon.particle.loop.BounceLoop
import com.github.gameoholic.partigon.particle.loop.RepeatLoop
import com.github.gameoholic.partigon.util.DoubleTriple
import com.github.gameoholic.partigon.util.EnvelopePair
import com.github.gameoholic.partigon.util.MatrixUtils
import com.github.gameoholic.partigon.util.Utils.envelope
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender


object TestCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        partigonParticle(Location(Bukkit.getWorld("world"), 0.0, 100.0, 0.0), Particle.END_ROD) {
            envelopes = listOf(
                curveEnvelope(
                    Envelope.PropertyType.POS_Z,
                    0.0.envelope, 4.0.envelope,
                    CurveEnvelopeWrapper.CurveOrientation.RIGHT,
                    RepeatLoop(200),
                    completion = 0.5,
                ),
                curveEnvelope(
                    Envelope.PropertyType.POS_X,
                    0.0.envelope, 4.0.envelope,
                    CurveEnvelopeWrapper.CurveOrientation.RIGHT,
                    RepeatLoop(200),
                    completion = 0.5,
                )
            )
            extra = 0.05
            count = 0
        }.start()


        return true
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

                    curveEnvelope(
                        Envelope.PropertyType.POS_Y,
                        0.0.envelope, 4.0.envelope,
                        CurveEnvelopeWrapper.CurveOrientation.RIGHT_ABOVE,
                        BounceLoop(200),
                    )
                )
                extra = 0.05
                count = 0
            }

        val particle1 = builder().build().start()
        //Mirror all particles:
        val particle2 = builder().apply {
            this.rotationOptions = listOf(
                MatrixUtils.RotationOptions(DoubleTriple(0.0, 0.0, 0.0), 180.0, MatrixUtils.RotationType.X_AXIS)
            )
        }.build().start()
        val particle3 = builder().apply {
            this.rotationOptions = listOf(
                MatrixUtils.RotationOptions(DoubleTriple(0.0, 0.0, 0.0), 180.0, MatrixUtils.RotationType.Z_AXIS)
            )
        }.build().start()
        val particle4 = builder().apply {
            this.rotationOptions = listOf(
                MatrixUtils.RotationOptions(DoubleTriple(0.0, 0.0, 0.0), 180.0, MatrixUtils.RotationType.X_AXIS),
                MatrixUtils.RotationOptions(DoubleTriple(0.0, 0.0, 0.0), 180.0, MatrixUtils.RotationType.Z_AXIS)
            )
        }.build().start()
        val particle5 = builder().apply {
            this.rotationOptions = listOf(
                MatrixUtils.RotationOptions(DoubleTriple(0.0, 0.0, 0.0), 270.0, MatrixUtils.RotationType.Y_AXIS),
                MatrixUtils.RotationOptions(DoubleTriple(0.0, 0.0, 0.0), 180.0, MatrixUtils.RotationType.Z_AXIS)
            )
        }.build().start()
        val particle6 = builder().apply {
            this.rotationOptions = listOf(
                MatrixUtils.RotationOptions(DoubleTriple(0.0, 0.0, 0.0), 270.0, MatrixUtils.RotationType.Y_AXIS),
                MatrixUtils.RotationOptions(DoubleTriple(0.0, 0.0, 0.0), 180.0, MatrixUtils.RotationType.X_AXIS)
            )
        }.build().start()










        return true
    }


}