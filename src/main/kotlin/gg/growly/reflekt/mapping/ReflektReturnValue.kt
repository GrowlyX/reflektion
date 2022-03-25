package gg.growly.reflekt.mapping

/**
 * Forces [gg.growly.reflekt.ReflektInvocation]
 * to "reflekt" the return value.
 *
 * @author GrowlyX
 * @since 3/25/2022
 */
@Target(
    AnnotationTarget.FUNCTION
)
annotation class ReflektReturnValue
