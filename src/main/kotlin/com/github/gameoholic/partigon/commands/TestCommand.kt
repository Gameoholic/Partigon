package com.github.gameoholic.partigon.commands


import com.github.gameoholic.partigon.Partigon
import com.github.gameoholic.partigon.particle.PartigonParticle.Companion.partigonParticle
import com.github.gameoholic.partigon.particle.PartigonParticle.Companion.partigonParticleBuilder
import com.github.gameoholic.partigon.particle.envelope.ConstantEnvelope
import com.github.gameoholic.partigon.particle.envelope.Envelope
import com.github.gameoholic.partigon.particle.envelope.EnvelopeGroup
import com.github.gameoholic.partigon.particle.envelope.TrigonometricEnvelope
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper.circleEnvelopeGroup
import com.github.gameoholic.partigon.particle.envelope.wrapper.CurveEnvelopeWrapper
import com.github.gameoholic.partigon.particle.envelope.wrapper.CurveEnvelopeWrapper.curveEnvelope
import com.github.gameoholic.partigon.particle.loop.BounceLoop
import com.github.gameoholic.partigon.particle.loop.RepeatLoop
import com.github.gameoholic.partigon.util.EnvelopePair
import com.github.gameoholic.partigon.util.EnvelopeTriple
import com.github.gameoholic.partigon.util.Utils.envelope
import com.github.gameoholic.partigon.util.rotation.RotationOptions
import com.github.gameoholic.partigon.util.rotation.RotationType
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.scheduler.BukkitRunnable


object TestCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val framesPerTick = 10
        val circlePoint1 = TrigonometricEnvelope(
            Envelope.PropertyType.NONE,
            3.0.envelope,
            0.0.envelope,
            TrigonometricEnvelope.TrigFunc.COS,
            RepeatLoop(50 * framesPerTick)
        )
        val circlePoint2 = TrigonometricEnvelope(
            Envelope.PropertyType.NONE,
            (-3.0).envelope,
            0.0.envelope,
            TrigonometricEnvelope.TrigFunc.COS,
            RepeatLoop(50 * framesPerTick)
        )
        val particle = partigonParticle(Location(Bukkit.getWorld("world"), 0.0, 100.0, 0.0), Particle.END_ROD) {
            envelopes = listOf(
                *circleEnvelopeGroup(
                    EnvelopeGroup.EnvelopeGroupType.POSITION,
                    EnvelopePair(circlePoint2, 0.0.envelope),
                    EnvelopePair(0.0.envelope, circlePoint1),
                    CircleEnvelopeWrapper.CircleDirection.RIGHT,
                    RepeatLoop(framesPerTick),
                ).getEnvelopes().toTypedArray(),

                *circleEnvelopeGroup(
                    EnvelopeGroup.EnvelopeGroupType.OFFSET,
                    EnvelopePair(circlePoint2, 0.0.envelope),
                    EnvelopePair(0.0.envelope, circlePoint1),
                    CircleEnvelopeWrapper.CircleDirection.RIGHT,
                    RepeatLoop(10 * framesPerTick),
                ).getEnvelopes().toTypedArray(),
            )
            animationFrameAmount = framesPerTick
            animationInterval = 1
            count = 0.envelope
            extra = 1.0.envelope
        }
        particle.start()


        return true




    }


}