package gg.growly.reflekt

import java.lang.reflect.Proxy
import kotlin.reflect.KClass

/**
 * @author GrowlyX
 * @since 3/23/2022
 */
object Reflekt
{
    /**
     * Inline function to wrap [map].
     */
    inline fun <reified T : Any> map(
        target: Any
    ): T
    {
        return map(T::class, target)
    }

    /**
     * Maps the original object to a proxy
     * interface implementation through our
     * [ReflektInvocation].
     */
    fun <T : Any> map(
        clazz: KClass<T>, target: Any
    ): T
    {
        val invocationHandler =
            ReflektInvocation(
                target, clazz.java
            )

        val proxy = Proxy.newProxyInstance(
            clazz.java.classLoader,
            arrayOf(clazz.java),
            invocationHandler
        )

        return proxy as T
    }
}
