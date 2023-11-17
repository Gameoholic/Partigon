package xyz.gameoholic.partigon.util


//Ty lucyydotp for this file

object DependencyInjection {
    @PublishedApi
    internal val boundObjects: MutableMap<Class<*>, Any> = mutableMapOf()
}

/** Lazily injects an instance of type [T], throwing [IllegalStateException] if nothing is bound when accessed. */
inline fun <reified T> inject(): Delegate<T> = Delegate {
    val bound = DependencyInjection.boundObjects[T::class.java] ?: error("No bound instance for ${T::class.java.simpleName}")
    return@Delegate bound as T
}

/** Lazily injects an instance of type [T], or null if nothing is bound when accessed. */
inline fun <reified T> injectOrNull(): Delegate<T?> = Delegate {
    DependencyInjection.boundObjects[T::class.java]?.let { it as T }
}

/**
 * Binds an instance to type [T].
 * @throws IllegalStateException if [T] already has a bound instance
 */
inline fun <reified T : Any> T.bind(): T = also {
    check(
        DependencyInjection.boundObjects.putIfAbsent(
            T::class.java,
            this
        ) == null
    ) { "Attempt to bind already bound type ${T::class.java.simpleName}" }
}

