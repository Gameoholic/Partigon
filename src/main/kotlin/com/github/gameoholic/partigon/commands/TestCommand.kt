package com.github.gameoholic.partigon.commands


import com.github.gameoholic.partigon.Partigon
import com.github.gameoholic.partigon.particle.PartigonParticleImpl
import com.github.gameoholic.partigon.particle.envelope.CurveEnvelope
import com.github.gameoholic.partigon.particle.envelope.Envelope
import com.github.gameoholic.partigon.particle.envelope.LineEnvelope
import com.github.gameoholic.partigon.particle.loop.*

import net.minecraft.core.particles.ParticleType
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

object TestCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return true
        //todo: add foo type to property type for nested



        PartigonParticleImpl(sender.location,
            Particle.FLAME,
            listOf(
                CurveEnvelope(
                    Envelope.PropertyType.POS_X,
                    -2.0,
                    CurveEnvelope(
                        Envelope.PropertyType.POS_X,
                        -2.0,
                        0.0,
                        CurveEnvelope.TrigFunc.SIN,
                        RepeatLoop(80),
                        2.0,
                        1.0,
                        false),
                    CurveEnvelope.TrigFunc.SIN,
                    RepeatLoop(40),
                    0.5,
                    1.0,
                    false),
                CurveEnvelope(
                    Envelope.PropertyType.POS_Z,
                    CurveEnvelope(
                        Envelope.PropertyType.POS_Z,
                        0.0,
                        2.0,
                        CurveEnvelope.TrigFunc.COS,
                        RepeatLoop(80),
                        2.0,
                        1.0,
                        false),
                    2.0,
                    CurveEnvelope.TrigFunc.COS,
                    RepeatLoop(40),
                    0.5,
                    1.0,
                    false),
                CurveEnvelope(
                    Envelope.PropertyType.POS_Y,
                    2.0,
                    0.0,
                    CurveEnvelope.TrigFunc.SIN,
                    RepeatLoop(40),
                    0.5,
                    1.0,
                    false)
            ),
            1,
            Vector(0.0, 0.0, 0.0)
        ).start()



        //Heart
//        PartigonParticleImpl(sender.location,
//            Particle.FLAME,
//            listOf(
//                CurveEnvelope(
//                    Envelope.PropertyType.POS_X,
//                    -2.0,
//                    LineEnvelope(Envelope.PropertyType.POS_X, 0, 2, ReverseLoop(40)),
//                    CurveEnvelope.TrigFunc.SIN,
//                    RepeatLoop(80),
//                    2.0,
//                    1.0,
//                    false),
//                CurveEnvelope(
//                    Envelope.PropertyType.POS_Z,
//                    LineEnvelope(Envelope.PropertyType.POS_X, 0, -2, ReverseLoop(40)),
//                    2.0,
//                    CurveEnvelope.TrigFunc.COS,
//                    RepeatLoop(80),
//                    2.0,
//                    1.0,
//                    false)
//            ),
//            1,
//            Vector(0.0, 0.0, 0.0)
//        ).start()

        return true
    }



}