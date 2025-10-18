/**
 *
 * @author PumpkinXD
 * @description Gradle build configuration for packaging Minecraft resource packs.
 *              Modified from:
 *                 https://github.com/ILikePlayingGames/FancyWarpMenuResourcePacks/blob/c2b422d1fb7e4df4268815c8d1bf9b5f9de49629/build.gradle.kts
 *
 */

import groovy.lang.Closure

plugins {
    base
}

/**
 * taskName : folderName
 */
val buildPackTasks: Map<String, String> = mapOf(
    "buildZH" to "KanamiTheWitherZH",
    "buildEN" to "KanamiTheWitherEN"
)

/**
 * taskName : legacyFolderName : modernFolderName
 */
val buildLegacyPackTasks: List<Triple<String, String, String>> = listOf(
    Triple("buildLegacyZH", "KanamiTheWitherLegacyZH", "KanamiTheWitherZH"),
    Triple("buildLegacyEN", "KanamiTheWitherLegacyEN", "KanamiTheWitherEN")
)

tasks.build {
    dependsOn(tasks.matching { it.group.equals("build resource pack") })
}

for (taskEntry in buildPackTasks) {
    tasks.register<Zip>(taskEntry.key) {
        configure(getTexturePackConfiguration(taskEntry.value))
    }
}

for (taskEntry in buildLegacyPackTasks) {
    tasks.register<Zip>(taskEntry.first) {
        group = "build resource pack"
        archiveBaseName.set(taskEntry.second)
        destinationDirectory.set(layout.buildDirectory)
        from(layout.projectDirectory.dir("src/${taskEntry.second}"))
        from(layout.projectDirectory.dir("src/${taskEntry.third}/assets/strinova"))
        {
            into("assets/strinova")
        }
    }
}



fun getTexturePackConfiguration(texturePackName: String): Closure<Any?> {
    return closureOf<Zip> {
        group = "build resource pack"
        archiveBaseName.set(texturePackName)
        destinationDirectory.set(layout.buildDirectory)
        from(layout.projectDirectory.dir("src/${texturePackName}"))
        // filesMatching("pack.mcmeta") {
        //     expand("Version" to project.properties["Version"])
        // }
    }
}
