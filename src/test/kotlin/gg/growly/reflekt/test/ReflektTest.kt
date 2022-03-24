package gg.growly.reflekt.test

import gg.growly.reflekt.Reflekt
import gg.growly.reflekt.mapping.Mapping
import org.junit.jupiter.api.Test

/**
 * @author GrowlyX
 * @since 3/23/2022
 */
object ReflektTest
{
    @Test
    fun test()
    {
        val original = OriginalClass()

        val proxied = Reflekt
            .map<ProxiedOriginalClass>(original)

        proxied.sendMessageProxied("hors!/?")
    }

    class OriginalClass
    {
        fun sendMessage(message: String)
        {
            println(message)
        }
    }

    interface ProxiedOriginalClass
    {
        @Mapping("sendMessage")
        fun sendMessageProxied(message: String)
    }
}
