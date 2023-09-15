package com.github.gameoholic.partigon.particle.loop

interface Loop {

    val duration: Int
    val envelopeDuration: Int
    fun applyLoop(frameIndex: Int): Int

}