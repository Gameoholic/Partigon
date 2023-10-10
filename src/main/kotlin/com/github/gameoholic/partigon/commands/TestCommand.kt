package com.github.gameoholic.partigon.commands


import com.github.gameoholic.partigon.particle.PartigonParticle
import com.github.gameoholic.partigon.particle.PartigonParticle.Companion.partigonParticle
import com.github.gameoholic.partigon.particle.envelope.Envelope
import com.github.gameoholic.partigon.particle.envelope.EnvelopeGroup
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper.circleEnvelope
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper.circleEnvelopeGroup
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
import org.bukkit.util.Vector


object TestCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        //todo now: clone/mirror particles

        val particle = partigonParticle(Location(Bukkit.getWorld("world"), 0.0, 100.0, 0.0), Particle.FLAME) {
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
            extra = 0.05
            count = 0
        }
        particle.start()


        partigonParticle(particle) {
            envelopes = listOf(
                *circleEnvelopeGroup(
                    EnvelopeGroup.EnvelopeGroupType.POSITION,
                    EnvelopePair((-3.0).envelope, 0.0.envelope),
                    EnvelopePair(0.0.envelope, (3.0).envelope),
                    CircleEnvelopeWrapper.CircleDirection.RIGHT,
                    RepeatLoop(100),
                    rotationOptions = listOf(
                        MatrixUtils.RotationOptions(DoubleTriple(0.0, 0.0, 0.0), 180.0, MatrixUtils.RotationType.X_AXIS)
                    )
                ).getEnvelopes().toTypedArray(),

                *circleEnvelopeGroup(
                    EnvelopeGroup.EnvelopeGroupType.OFFSET,
                    EnvelopePair((3.0).envelope, 0.0.envelope),
                    EnvelopePair(0.0.envelope, (-3.0).envelope),
                    CircleEnvelopeWrapper.CircleDirection.RIGHT,
                    RepeatLoop(100),
                    rotationOptions = listOf(
                        MatrixUtils.RotationOptions(DoubleTriple(0.0, 0.0, 0.0), 180.0, MatrixUtils.RotationType.X_AXIS)
                    )
                ).getEnvelopes().toTypedArray()
            )
        }.start()



        return true
    }


}