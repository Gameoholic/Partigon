package com.github.gameoholic.partigon.commands


import com.github.gameoholic.partigon.Partigon
import com.github.gameoholic.partigon.particle.PartigonParticleImpl
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
        PartigonParticleImpl(sender.location,
            Particle.END_ROD,
            listOf(
                LineEnvelope(
                    Envelope.PropertyType.POS_Y,
                    0.0,
                    2.0,
                    DisableLoop(8),
                    false)
            ),
            1,
            Vector(0.0, 0.0, 0.0)
        ).start()
        return true
    }



}