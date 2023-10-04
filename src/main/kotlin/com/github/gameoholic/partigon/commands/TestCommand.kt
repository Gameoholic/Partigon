package com.github.gameoholic.partigon.commands


import com.github.gameoholic.partigon.Utils
import com.github.gameoholic.partigon.particle.PartigonParticle
import com.github.gameoholic.partigon.particle.PartigonParticle.Companion.partigonParticle
import com.github.gameoholic.partigon.particle.envelope.BasicEnvelope
import com.github.gameoholic.partigon.particle.envelope.Envelope
import com.github.gameoholic.partigon.particle.envelope.TrigonometricEnvelope
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper.positionCircleEnvelopes
import com.github.gameoholic.partigon.particle.loop.*

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.joml.Matrix2d
import org.joml.Matrix2dc
import org.joml.Matrix3d
import org.joml.Vector2d
import org.joml.Vector4f
import kotlin.math.cos
import kotlin.math.sin

object TestCommand : CommandExecutor {

    var part: PartigonParticle? = null

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        val theta = 30.0
        val thetaRadians = Math.toRadians(theta)

        org.bukkit.util.


        partigonParticle(Location(Bukkit.getWorld("world"), 0.0, 100.0, 0.0), Particle.END_ROD) {
            envelopes = listOf(
                BasicEnvelope(
                    Envelope.PropertyType.POS_X,
                    RepeatLoop(80),
                    false,
                    1.0,
                    "sin(frame_index)"
                ),
                BasicEnvelope(
                    Envelope.PropertyType.POS_Z,
                    RepeatLoop(80),
                    false,
                    1.0,
                    "cos(frame_index)"
                ),
                BasicEnvelope(
                    Envelope.PropertyType.POS_Y,
                    RepeatLoop(80),
                    false,
                    1.0,
                    "0"
                )

            )
            extra = 0.0
            animationInterval = 1
            animationFrameAmount = 1
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