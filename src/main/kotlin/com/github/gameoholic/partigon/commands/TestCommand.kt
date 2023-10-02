package com.github.gameoholic.partigon.commands


import com.github.gameoholic.partigon.particle.PartigonParticle.Companion.partigonParticle
import com.github.gameoholic.partigon.particle.envelope.CircleEnvelope
import com.github.gameoholic.partigon.particle.envelope.Envelope
import com.github.gameoholic.partigon.particle.loop.*

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
                CircleEnvelope(
                    Envelope.PropertyType.POS_X,
                    0.0,
                    4.0,
                    CircleEnvelope.TrigFunc.SIN,
                    RepeatLoop(110),
                    semiCircles = 1.0,
                    width = 0.5
                    ),
                CircleEnvelope(
                    Envelope.PropertyType.POS_Z,
                    0.0,
                    2.0,
                    CircleEnvelope.TrigFunc.COS,
                    RepeatLoop(110),
                    semiCircles = 1.0 //todo: width is fucking useless bro. rename to circle. fix this entire shit.
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
//                CircleEnvelope(
//                    Envelope.PropertyType.POS_X,
//                    -2.0,
//                    LineEnvelope(Envelope.PropertyType.POS_X, 0, 2, ReverseLoop(40)),
//                    CircleEnvelope.TrigFunc.SIN,
//                    RepeatLoop(80),
//                    2.0,
//                    1.0,
//                    false
//                ),
//                CircleEnvelope(
//                    Envelope.PropertyType.POS_Z,
//                    LineEnvelope(Envelope.PropertyType.POS_X, 0, -2, ReverseLoop(40)),
//                    2.0,
//                    CircleEnvelope.TrigFunc.COS,
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