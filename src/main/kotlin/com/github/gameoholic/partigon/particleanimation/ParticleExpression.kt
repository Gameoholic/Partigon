package com.github.gameoholic.partigon.particleanimation

class ParticleExpression(expression: String) {


    companion object {
        fun getExpression(expression: String): ParticleExpression {
            return ParticleExpression(expression)
        }
        fun getExpression(value: Int): ParticleExpression {
            return ParticleExpression(value.toString())
        }
    }
}