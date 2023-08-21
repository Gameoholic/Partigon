package com.github.gameoholic.partigon.partigonparticle.loop

import kotlin.math.ceil


/**
 * Determine what happens to the particle animation when it reaches halfway point of the loop.
 *
 * @property CONTINUE Will continue running the animation, disregarding the loop.
 * @property REPEAT Will restart the animation where it was on frame t=0
 * @property REVERSE Will reverse the animation, playing it the same way but from point 2 to point 1.
 * @property BOUNCE Similar to REVERSE, but will not repeat frames upon finishing the loop.
 * @property DISABLE Disables the envelope.
 */
enum class LoopType { CONTINUE, REPEAT, REVERSE, BOUNCE, DISABLE}

class Loop(val type: LoopType, val duration: Int) {

    val oneDirectionLoopDuration: Int
    init {
        if (type == LoopType.REPEAT)
            oneDirectionLoopDuration = duration
        else if (type == LoopType.BOUNCE)
            oneDirectionLoopDuration = ceil(duration / 2.0).toInt() + 1
        else
            oneDirectionLoopDuration = ceil(duration / 2.0).toInt()

    }

}