package gg.growly.reflekt

import gg.growly.reflekt.mapping.Field
import gg.growly.reflekt.mapping.Mapping
import gg.growly.reflekt.mapping.ReflektReturnValue
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * Handles the invocation of
 * internal methods when a
 * proxy method is called.
 *
 * We will exclude any methods which
 * are not annotated with [Mapping] as
 * they are not "proxy" methods.
 *
 * Reflections for mappings will be
 * cached on initialization.
 *
 * @author GrowlyX
 * @since 3/23/2022
 */
class ReflektInvocation(
    private val internalObject: Any, clazz: Class<*>
) : InvocationHandler
{
    private val methodMappings =
        mutableMapOf<Method, Method>()

    private val fieldMappings =
        mutableMapOf<Method, java.lang.reflect.Field>()

    init
    {
        for (method in clazz.methods)
        {
            val mapping = method
                .getAnnotation(Mapping::class.java)
                ?: continue

            // The name of the internal method
            // we're trying to target.
            val mappedMethod = mapping.value
                .ifEmpty { method.name }

            val parameterTypes = method
                .parameters.map { it.type }
                .toTypedArray()

            val javaClass = internalObject.javaClass

            if (!method.isAnnotationPresent(Field::class.java))
            {
                val internal = javaClass
                    .getMethod(
                        mappedMethod, *parameterTypes
                    )
                    ?: throw IllegalArgumentException(
                        "No internal method with the name $mappedMethod was found in ${javaClass.simpleName}."
                    )

                this.methodMappings[method] = internal
            } else
            {
                val internal = javaClass
                    .getField(mappedMethod)
                    ?: throw IllegalArgumentException(
                        "No internal field with the name $mappedMethod was found in ${javaClass.simpleName}."
                    )

                this.fieldMappings[method] = internal
            }
        }
    }

    @Throws(Throwable::class)
    override fun invoke(
        proxy: Any, method: Method, args: Array<out Any>
    ): Any?
    {
        var value: Any?

        if (!method.isAnnotationPresent(Field::class.java))
        {
            val internal = methodMappings[method]
                ?: throw IllegalArgumentException(
                    "No internal method was found for ${
                        method.name
                    } in ${javaClass.simpleName}."
                )

            value = internal
                .invoke(internalObject, *args)
        } else
        {
            val internal = fieldMappings[method]
                ?: throw IllegalArgumentException(
                    "No internal method was found for ${
                        method.name
                    } in ${javaClass.simpleName}."
                )

            val accessibility = internal
                .isAccessible

            internal.isAccessible = true

            value = internal
                .get(internalObject)

            internal.isAccessible =
                accessibility
        }

        val reflektReturnValue = method
            .isAnnotationPresent(ReflektReturnValue::class.java)

        if (reflektReturnValue && value != null)
        {
            // TODO: 3/30/2022 cache reflekt mappings
            //  for auto-generated values
            value = Reflekt.map(value::class, value)
        }

        return value
    }
}
