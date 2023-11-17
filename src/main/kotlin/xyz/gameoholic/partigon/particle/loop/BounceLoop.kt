package xyz.gameoholic.partigon.particle.loop

/**
 * When loop reaches end, reverses the animation similarly to RepeatLoop,
 * but will skip the first & last frames (point 1 and 2) upon reversing.
 * This results in a smooth back and forth animation that does not stay
 * on the points for an extra frame, as opposed to ReverseLoop.
 *
 * NOTE: The last frame will not be animated. The loop will not end
 * on point1, but one frame before it.
 *
 * @param duration The duration of the loop, both directions included. Must be even.
 *
 * @throws IllegalArgumentException If loop duration was not above 0.
 */ //todo: investigate if still works with odd. if not throw exception.
class BounceLoop(override val duration: Int) : Loop {
    override val envelopeDuration = duration / 2 + 1
    init {
        if (duration <= 0)
            throw IllegalArgumentException("Bounce loop duration must be above 0.")
    }
    override fun applyLoop(frameIndex: Int): Int {
        // For loop index 0,1,2,3 half loop index will be 0,1,2,0
        val loopIndex = frameIndex % duration
        val halfLoopIndex = loopIndex % (duration / 2 + 1) //We add +1 so it's not actually half
        // If animation needs to be reversed, skip point 2 and reverse:
        if (loopIndex > duration / 2)
            return duration / 2 - 1 - halfLoopIndex
        // Point 1 on reverse will not be animated
        return halfLoopIndex
    }

}