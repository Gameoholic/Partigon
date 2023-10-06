package com.github.gameoholic.partigon.commands


import com.github.gameoholic.partigon.Utils
import com.github.gameoholic.partigon.particle.PartigonParticle
import com.github.gameoholic.partigon.particle.PartigonParticle.Companion.partigonParticle
import com.github.gameoholic.partigon.particle.envelope.BasicEnvelope
import com.github.gameoholic.partigon.particle.envelope.Envelope
import com.github.gameoholic.partigon.particle.envelope.TrigonometricEnvelope
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper.positionCircleEnvelopes
import com.github.gameoholic.partigon.particle.loop.RepeatLoop
import org.apache.commons.math3.geometry.Vector
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealVector
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import kotlin.math.cos
import kotlin.math.sin


object TestCommand : CommandExecutor {

    var degree = 0.0

    var part: PartigonParticle? = null

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (!args.isNullOrEmpty()) {
            degree = args[0]!!.toDouble()
            return true
        }


        partigonParticle(Location(Bukkit.getWorld("world"), 0.0, 100.0, 0.0), Particle.END_ROD) {
            envelopes = listOf(
                TrigonometricEnvelope(Envelope.PropertyType.POS_X, -3.0, 3.0, TrigonometricEnvelope.TrigFunc.COS, RepeatLoop(40), completion = 2.0),
                TrigonometricEnvelope(Envelope.PropertyType.POS_Z, -3.0, 3.0, TrigonometricEnvelope.TrigFunc.SIN, RepeatLoop(40), completion = 2.0),
                TrigonometricEnvelope(Envelope.PropertyType.POS_Y, -3.0, 3.0, TrigonometricEnvelope.TrigFunc.COS, RepeatLoop(40), completion = 2.0),
                )
            extra = 0.0
            animationInterval = 10
            animationFrameAmount = 40
        }.start()


        return true

        //Heart
//        PartigonParticle(
//            sender.location,
//            Particle.HEART,
//            listOf(
//                TrigonometricEnvelope(
//                    Envelope.PropertyType.POS_X,
//                    -2.0,
//                    LineEnvelope(Envelope.PropertyType.POS_X, 0, 2, ReverseLoop(40)),
//                    TrigonometricEnvelope.TrigFunc.SIN,
//                    RepeatLoop(80),
//                    2.0,
//                    1.0,
//                    false
//                ),
//                TrigonometricEnvelope(
//                    Envelope.PropertyType.POS_Z,
//                    LineEnvelope(Envelope.PropertyType.POS_X, 0, -2, ReverseLoop(40)),
//                    2.0,
//                    TrigonometricEnvelope.TrigFunc.COS,
//                    RepeatLoop(80),
//                    2.0,
//                    1.0,
//                    false
//                )
//            ),
//            1,
//            Vector(0.0, 0.0, 0.0),
//            1,
//            1,
//            0.0
//        ).start()


        return true
    }


}