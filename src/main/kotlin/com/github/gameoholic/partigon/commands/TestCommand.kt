package com.github.gameoholic.partigon.commands


import com.github.gameoholic.partigon.particle.PartigonParticle.Companion.partigonParticle
import com.github.gameoholic.partigon.particle.envelope.TrigonometricEnvelope
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
                TrigonometricEnvelope( //(0,0) -> (4,4)
                    Envelope.PropertyType.POS_X,
                    0.0,
                    4.0,
                    TrigonometricEnvelope.TrigFunc.COS,
                    RepeatLoop(110),
                    ),
                TrigonometricEnvelope(
                    Envelope.PropertyType.POS_Z,
                    0.0,
                    4.0,
                    TrigonometricEnvelope.TrigFunc.SIN,
                    RepeatLoop(110),
                )
            )
            extra = 0.0
            animationInterval = 1
            animationFrameAmount = 1
        }.start()

        //todo: create CircleEnvelope, with enum denoting whether use sin or cos. add new trig funcs. change doc a bit to reflect changes.
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