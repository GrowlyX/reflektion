package gg.growly.reflekt

import gg.growly.reflekt.mapping.Mapping
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * Handles the relation between
 * proxy & internal methods.
 *
 * This also invokes the internal
 * method when the implementation's
 * proxy method is called.
 *
 * We will exclude any methods which
 * are not annotated with [Mapping] as
 * they do are not "proxy" methods.
 *
 * @author GrowlyX
 * @since 3/23/2022
 */
class ReflektInvocation(
    private val internalObject: Any
) : InvocationHandler
{
    @Throws(Throwable::class)
    override fun invoke(
        proxy: Any, method: Method, args: Array<out Any>
    ): Any?
    {
        val mapping = method
            .getAnnotation(Mapping::class.java)
            ?: throw IllegalArgumentException(
                "Method ${method.name} is not annotated with @Mapping."
            )

        // The name of the internal method
        // we're trying to target.
        val mappedMethod = mapping.value
            .ifEmpty { method.name }

        val parameterTypes = method
            .parameters.map { it.type }
            .toTypedArray()

        val javaClass = internalObject.javaClass

        if (!mapping.field)
        {
            val internal = javaClass
                .getMethod(
                    mappedMethod, *parameterTypes
                )
                ?: throw IllegalArgumentException(
                    "No internal method with the name $mappedMethod was found in ${javaClass.simpleName}."
                )

            return internal
                .invoke(internalObject, *args)
        } else
        {
            val internal = javaClass
                .getField(mappedMethod)
                ?: throw IllegalArgumentException(
                    "No internal method with the name $mappedMethod was found in ${javaClass.simpleName}."
                )

            return internal
                .get(internalObject)
        }
    }
}