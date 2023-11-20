# Partigon

A Minecraft particle animation library designed to make your life easier.

Create complex particle animations with just a few lines of code!

To get started, go to the [documentation](https://partigon.gameoholic.xyz/).


## Using Partigon
_It is recommended to use Kotlin with Partigon, as it was made specifically for usage with Kotlin._

To use this library, add the `Partigon.jar` file (found in [Releases](https://github.com/Gameoholic/Partigon/releases)) and `Kotlin.jar` (found [here](https://github.com/Gameoholic/PaperKotlin/releases)) to your plugins folder. Partigon requires the Kotlin stdlib to work. 

Then, add Partigon as a [dependency or soft dependency](https://docs.papermc.io/paper/dev/plugin-yml#dependencies) like any other plugin:

```yml
depend: [ Partigon ]
```

To add Partigon as a Maven dependency:

```xml
<repository>
  <id>gameoholic-repo</id>
  <url>https://repo.gameoholic.xyz/releases</url>
</repository>

<dependency>
  <groupId>xyz.gameoholic</groupId>
  <artifactId>partigon</artifactId>
  <version>version</version>
  <scope>provided</scope>
</dependency>
```

To add Partigon as a Gradle (Kotlin) dependency:

```gradle
repositories {
    maven {
        url = uri("https://repo.gameoholic.xyz/releases")
    }
}
dependencies {
    compileOnly("xyz.gameoholic:partigon:1.1.1")
}
```
