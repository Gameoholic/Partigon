package com.github.gameoholic.partigon.particle.loop

/**
 * When loop reaches end, will reverse the animation, playing it the same way but from point 2 to point 1.
 * Loop duration is for both directions.
 */
class ReverseLoop(override val duration: Int): Loop {
    override val envelopeDuration = duration / 2
    override fun applyLoop(frameIndex: Int): Int {
        //For loop index 0,1,2,3,4,5 half loop index will be 0,1,2,0,1,2
        val loopIndex = frameIndex % duration
        val halfLoopIndex = loopIndex % (duration / 2)
        //If animation needs to be reversed:
        if (loopIndex >= duration / 2)
            return duration / 2 - 1 - halfLoopIndex
        return halfLoopIndex
    }

}