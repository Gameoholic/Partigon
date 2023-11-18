package xyz.gameoholic.partigon.particle

/**
 * Represents a particle animation with multiple Partigon particles.
 * Starting, stopping or resuming it will do it to all the singular particles that it contains.
 */
class MultiParticle(private val particles: List<SingularParticle>) : PartigonParticle {

    private constructor(
        builder: Builder
    ) :
        this(
            builder.particles
        )

    companion object {
        inline fun multiParticle(
            block: Builder.() -> Unit
        ) = Builder().apply(block).build()

        inline fun multiParticleBuilder(
            block: Builder.() -> Unit
        ) = Builder().apply(block)
    }

    class Builder {
        /**
         * The list of singular particles that are part of this MultiParticle.
         */
        var particles: List<SingularParticle> = listOf()

        /**
         * Adds this particle to the MultiParticle instance.
         */
        fun SingularParticle.add() {
            particles += this
        }

        fun build() = MultiParticle(this)
    }

    override fun start() {
        particles.forEach {
            it.start()
        }
    }

    override fun stop() {
        particles.forEach {
            it.stop()
        }
    }

    override fun resume() {
        particles.forEach {
            it.resume()
        }
    }
}