package gg.growly.reflekt.invocation

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * @author GrowlyX
 * @since 3/23/2022
 */
class ReflektInvocationHandler(
    val kClass: Class<*>
) : InvocationHandler
{
    override fun invoke(
        proxy: Any, method: Method, args: Array<out Any>
    ): Any
    {
        val
    }
}
