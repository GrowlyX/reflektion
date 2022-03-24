package gg.growly.reflekt

import java.lang.reflect.Proxy

/**
 * @author GrowlyX
 * @since 3/23/2022
 */
object Reflekt
{
    /**
     * Maps the original object to a proxy
     * interface implementation through our
     * [ReflektInvocation].
     */
    inline fun <reified T : Any> map(
        target: Any
    ): T
    {
        val invocationHandler =
            ReflektInvocation(target)

        val proxy = Proxy.newProxyInstance(
            T::class.java.classLoader,
            arrayOf(T::class.java),
            invocationHandler
        )

        return proxy as T
    }
}
