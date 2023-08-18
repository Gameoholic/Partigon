package com.github.gameoholic.partigon.particleanimation.loop


/**
 * Determine what happens to the particle animation when it reaches halfway point of the loop.
 *
 * @property CONTINUE Will continue running the animation in the same direction vector.
 * @property REPEAT Will restart the animation where it was on frame t=0
 * @property REVERSE Will reverse the animation, playing it the same way but from point 2 to point 1. WARNING: This will modify the centerPosition and switch it between point1 and point2 on every loop.
 * @property BOUNCE Similar to REVERSE, but will not repeat frames upon finishing the loop. WARNING: This will modify the centerPosition and switch it between point1 and point2 on every loop.
 * @property END Stops the particle animation.
 */
enum class LoopType { CONTINUE, REPEAT, REVERSE, BOUNCE, END}

class Loop(val type: LoopType, val duration: Int) {

    val halfDuration: Int = duration / 2

}