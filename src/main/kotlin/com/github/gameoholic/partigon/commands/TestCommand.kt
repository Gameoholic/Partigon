package com.github.gameoholic.partigon.commands


import com.github.gameoholic.partigon.particleanimation.LineParticleAnimation
import com.github.gameoholic.partigon.particleanimation.LineParticleLoopEndType
import com.github.gameoholic.partigon.particleanimation.LineParticleSettings
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.util.Vector

object TestCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
//        var a = GenericParticleAnimation(
//            Bukkit.getWorlds()[0],
//            Vector(0.5, 100.0, 0.5),
//            "(10-(t/12.56)) * cos(t/20)",
//            "0",
//            "(10-(t/12.56)) * sin(t/20)",
//            Particle.END_ROD,
//            "1",
//            extraExpression = "0.1",
//            stopCondition = { t: Int -> t / 20.0 > 2 * Math.PI }
//        ).start()

        val line = LineParticleAnimation(
            LineParticleSettings(
                Bukkit.getWorlds()[0],
                Vector(0.5, 100.0, 0.5),
                Vector(10.5, 100.0, 0.5),
                1000,
                40,
                LineParticleLoopEndType.BOUNCE,
                Particle.FLAME,
                )
        )

        line.start()




        //LoopSettings(loopDuration: 40, loopEndType: REVERSE)




        return true
    }


    //TODO: make all particles have a way to aniamte their property like loop and stuff?
    //basic / parameter particles (that require additional data)
    //premade shape presets
    //"copy" particles (that are the same exact thing but then the expressions are applied to it
}