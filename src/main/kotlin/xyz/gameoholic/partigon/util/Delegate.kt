package xyz.gameoholic.partigon.util

fun interface Delegate<out T> {
    fun get(): T
    operator fun getValue(thisRef: Any?, property: Any): T = get()
}