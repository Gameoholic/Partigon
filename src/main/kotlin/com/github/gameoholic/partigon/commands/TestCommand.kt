package com.github.gameoholic.partigon.commands


import com.github.gameoholic.partigon.particle.PartigonParticle
import com.github.gameoholic.partigon.particle.PartigonParticle.Companion.partigonParticle
import com.github.gameoholic.partigon.particle.envelope.Envelope
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


    var prevParticle: PartigonParticle? = null

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        //todo now: envelope group recieve offset too, same for circle envelope wrappers., clone/mirror particles
        prevParticle?.stop()

        prevParticle = partigonParticle(Location(Bukkit.getWorld("world"), 0.0, 100.0, 0.0), Particle.FLAME) {
            envelopes = listOf(
                *circleEnvelopeGroup(
                    EnvelopePair((-3.0).envelope, 0.0.envelope),
                    EnvelopePair(0.0.envelope, (3.0).envelope),
                    CircleEnvelopeWrapper.CircleDirection.RIGHT,
                    RepeatLoop(100),
                ).getEnvelopes().toTypedArray(),

                circleEnvelope(Envelope.PropertyType.OFFSET_X, (3.0).envelope, 0.0.envelope,
                    CircleEnvelopeWrapper.CircleDirection.RIGHT, CircleEnvelopeWrapper.VectorComponent.X, RepeatLoop(100)),

                circleEnvelope(Envelope.PropertyType.OFFSET_Z, 0.0.envelope, (-3.0).envelope,
                    CircleEnvelopeWrapper.CircleDirection.RIGHT, CircleEnvelopeWrapper.VectorComponent.Z, RepeatLoop(100))
            )
            extra = 0.05
            count = 0
            animationInterval = 1
            animationFrameAmount = 1
        }
        prevParticle?.start()


        return true
    }


}