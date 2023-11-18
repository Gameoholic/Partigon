package xyz.gameoholic.partigon.particle

class MultiParticle(private val particles: List<SingularParticle>) : PartigonParticle {

    //todo: doc different partigon classes

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
        var particles: List<SingularParticle> = listOf()

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