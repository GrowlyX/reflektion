package gg.growly.reflekt.mapping

/**
 * Used to specify what internal
 * method the proxy method should be mapped to.
 *
 * If no value, or in our case,
 * the method name, is specified,
 * we will use the proxy method's name.
 *
 * @author GrowlyX
 * @since 3/23/2022
 */
@Target(
    AnnotationTarget.FUNCTION
)
annotation class Mapping(
    val value: String = ""
)
