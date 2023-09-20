package com.github.gameoholic.partigon.particle.loop

/**
 * When loop reaches end, will reverse the animation similarly to RepeatLoop,
 * but will skip the first & last frames (point 1 and 2) upon reversing.
 * This results in a smooth back and forth animation
 * that does not 'lag' on the points.
 *
 * Loop duration MUST be even.
 * NOTE: The last frame will not be animated. The loop will not end
 * on point1, but one frame before it.
 */
class BounceLoop(override val duration: Int) : Loop {
    override val envelopeDuration = duration / 2 + 1
    override fun applyLoop(frameIndex: Int): Int {
        //For loop index 0,1,2,3 half loop index will be 0,1,2,0
        val loopIndex = frameIndex % duration
        val halfLoopIndex = loopIndex % (duration / 2 + 1) //We add +1 so it's not actually half
        //If animation needs to be reversed, skip point 2 and reverse:
        if (loopIndex > duration / 2)
            return duration / 2 - 1 - halfLoopIndex
        //Point 1 on reverse will not be animated
        return halfLoopIndex
    }

}