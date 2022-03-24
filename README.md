# reflektion
Minimalistic class proxy creator for Kotlin.
 - Reflektion allows you to create an implementation of an interface you provide containing proxy methods.
   - When these methods are executed, the method in your original class will be called with the arguments you supplied.
 - Reflektion is **NOT** meant to be used in any production enviornment. Since we do not cache reflections, it's not as efficient as it could be.

## Possible use cases:
 - Create proxy methods for internal `net.minecraft.server` classes.

## Usage:
```kotlin
fun test()
{
    val original = OriginalClass()

    val proxied = Reflekt
        .map<ProxiedOriginalClass>(original)

    proxied.sendMessageProxied("test!")
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
    // A value for the original method 
    // name is not required.
    @Mapping("sendMessage")
    fun sendMessageProxied(message: String)
}
```
