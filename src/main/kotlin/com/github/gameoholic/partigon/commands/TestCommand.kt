package com.github.gameoholic.partigon.commands


import com.github.gameoholic.partigon.particle.PartigonParticle
import com.github.gameoholic.partigon.particle.PartigonParticle.Companion.partigonParticle
import com.github.gameoholic.partigon.particle.envelope.*
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper.circleEnvelope
import com.github.gameoholic.partigon.particle.envelope.wrapper.CircleEnvelopeWrapper.circleEnvelopeGroup
import com.github.gameoholic.partigon.particle.loop.RepeatLoop
import com.github.gameoholic.partigon.util.MatrixUtils
import com.github.gameoholic.partigon.util.Utils
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
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
    var degree2 = 0.0

    var prevParticle: PartigonParticle? = null

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        if (!args.isNullOrEmpty()) {
            degree = args[0]!!.toDouble()
        }
        if (!args.isNullOrEmpty() && args.size > 1) {
            degree2 = args[1]!!.toDouble()
        }

        prevParticle?.stop()
        prevParticle = partigonParticle(Location(Bukkit.getWorld("world"), 0.0, 100.0, 0.0), Particle.END_ROD) {
            envelopes = listOf(

                LineEnvelope(Envelope.PropertyType.POS_X,
                    ConstantEnvelope(Envelope.PropertyType.NONE, "0.0"),
                    ConstantEnvelope(Envelope.PropertyType.NONE, "2.0"),
                    RepeatLoop(120)
                ),
                *circleEnvelopeGroup(
                    Utils.Pair(0.0, 0.0),
                    Utils.Pair(1.0, 1.0),
                    CircleEnvelopeWrapper.CircleDirection.RIGHT,
                    listOf(
                        MatrixUtils.RotationMatrixOptions(
                            Utils.Vector(0.0, 0.0, 0.0),
                            degree,
                            MatrixUtils.RotationType.Z
                        ),
                        MatrixUtils.RotationMatrixOptions(
                            Utils.Vector(0.0, 0.0, 0.0),
                            degree2,
                            MatrixUtils.RotationType.X
                        )
                    ),
                    RepeatLoop(40)
                ).getEnvelopes().toTypedArray()
            )
            extra = 0.0
            animationInterval = 20
            animationFrameAmount = 40
        }
        prevParticle?.start()

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