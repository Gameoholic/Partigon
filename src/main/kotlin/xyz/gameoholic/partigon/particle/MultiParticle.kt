package xyz.gameoholic.partigon.particle

class MultiParticle(private val particles: List<SingularParticle>): PartigonParticle {

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