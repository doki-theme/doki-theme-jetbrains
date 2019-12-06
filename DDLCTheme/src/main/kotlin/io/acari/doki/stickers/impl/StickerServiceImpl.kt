package io.acari.doki.stickers.impl

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import com.intellij.util.io.isFile
import io.acari.doki.stickers.StickerLevel
import io.acari.doki.stickers.StickerService
import io.acari.doki.themes.DokiTheme
import io.acari.doki.util.toOptional
import org.apache.commons.io.IOUtils
import java.io.BufferedInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.security.MessageDigest
import java.util.*
import javax.xml.bind.DatatypeConverter

private const val CLUB_MEMBER_ON = "CLUB_MEMBER_ON"
const val DOKI_BACKGROUND_PROP: String = "io.acari.doki.background"
private val messageDigest: MessageDigest = MessageDigest.getInstance("MD5")
private const val RESOURCES_DIRECTORY =
    "https://raw.githubusercontent.com/cyclic-reference/ddlc-jetbrains-theme/master/assets"
const val DOKI_CHIBI_PROP: String = "io.acari.doki.stickers"

class StickerServiceImpl : StickerService {

    // todo: persist level
    private var chibiLevel = StickerLevel.ON

    override fun activateForTheme(dokiTheme: DokiTheme) {
        removeWeebShit()
        turnOnIfNecessary(dokiTheme)
    }

    override fun remove() {
        removeWeebShit()
    }

    private fun removeWeebShit() {
        PropertiesComponent.getInstance().unsetValue(DOKI_CHIBI_PROP)
        PropertiesComponent.getInstance().unsetValue(DOKI_BACKGROUND_PROP)
        repaintWindows()
    }

    private fun repaintWindows() = try {
        IdeBackgroundUtil.repaintAllWindows()
    } catch (e: Throwable) {
    }

    private fun weebShitOn(): Boolean =
        chibiLevel != StickerLevel.OFF

    private fun turnOnIfNecessary(dokiTheme: DokiTheme) {
        if (weebShitOn())
            turnOnWeebShit(dokiTheme)
    }

    private fun turnOnWeebShit(dokiTheme: DokiTheme) {
        // todo: adjust opacity if lit club to 80 else 100
        val chibiOpacity = 99
        getImagePath(dokiTheme)
            .ifPresent {
                setProperty(
                    it,
                    "$chibiOpacity",
                    IdeBackgroundUtil.Fill.PLAIN.name,
                    IdeBackgroundUtil.Anchor.BOTTOM_RIGHT.name,
                    DOKI_CHIBI_PROP
                )
            }

        // todo: background frame painting.
        getFrameBackground(dokiTheme)
            .ifPresent {
                setProperty(
                    it,
                    "$chibiOpacity",
                    IdeBackgroundUtil.Fill.SCALE.name,
                    IdeBackgroundUtil.Anchor.CENTER.name,
                    DOKI_BACKGROUND_PROP
                )

            }
        repaintWindows()
    }


    private fun getFrameBackground(dokiTheme: DokiTheme): Optional<String> {
        return dokiTheme.getChibi()
            .map { "$RESOURCES_DIRECTORY/themes/$it" }
    }

    private fun getImagePath(dokiTheme: DokiTheme): Optional<String> =
        dokiTheme.getChibiPath()
            .flatMap { fullChibiClasspath ->
                getLocalClubMemberParentDirectory()
                    .map { localParentDirectory ->
                        val weebStuff =
                            Paths.get(localParentDirectory, fullChibiClasspath)
                                .normalize()
                                .toAbsolutePath()
                        if (shouldCopyToDisk(weebStuff, fullChibiClasspath)) {
                            createDirectories(weebStuff)
                            copyAnimes(fullChibiClasspath, weebStuff)
                                .map { it.toOptional() }
                                .orElseGet { getClubMemberFallback(dokiTheme) }
                        } else {
                            weebStuff.toString().toOptional()
                        }
                    }
                    .orElseGet {
                        getClubMemberFallback(dokiTheme)
                    }
            }

    private fun shouldCopyToDisk(weebStuff: Path, theAnimesPath: String) =
        !(Files.exists(weebStuff) && checksumMatches(weebStuff, theAnimesPath))

    private fun checksumMatches(weebStuff: Path, theAnimesPath: String): Boolean =
        try {
            getAnimesInputStream(theAnimesPath)
                .map { IOUtils.toByteArray(it) }
                .map { computeCheckSum(it) }
                .map { computedCheckSum ->
                    val onDiskCheckSum = getOnDiskCheckSum(weebStuff)
                    computedCheckSum == onDiskCheckSum
                }.orElseGet { false }
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }

    private fun getOnDiskCheckSum(weebStuff: Path): String =
        computeCheckSum(Files.readAllBytes(weebStuff))

    private fun computeCheckSum(byteArray: ByteArray): String {
        messageDigest.update(byteArray)
        val digest = messageDigest.digest()
        return DatatypeConverter.printHexBinary(digest).toUpperCase()
    }

    private fun createDirectories(weebStuff: Path) {
        try {
            Files.createDirectories(weebStuff.parent)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getAnimesInputStream(theAnimesPath: String): Optional<BufferedInputStream> =
        try {
            this.javaClass
                .classLoader
                .getResourceAsStream(theAnimesPath).toOptional()
                .map { BufferedInputStream(it) }
        } catch (e: IOException) {
            Optional.empty()
        }

    private fun copyAnimes(theAnimesPath: String, weebStuff: Path): Optional<String> =
        try {
            getAnimesInputStream(theAnimesPath)
                .map { bufferedInputStream ->
                    bufferedInputStream.use { inputStream ->
                        Files.newOutputStream(
                            weebStuff,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING
                        ).use { bufferedWriter ->
                            IOUtils.copy(inputStream, bufferedWriter)
                        }
                    }
                    weebStuff.toString()
                }
        } catch (e: IOException) {
            Optional.empty()
        }


    // todo: internet stickers fallback (test that and stuff)
    private fun getClubMemberFallback(dokiTheme: DokiTheme): Optional<String> {
        return dokiTheme.getChibi()
            .map { "$RESOURCES_DIRECTORY/club_members/$it" }
    }


    private fun getLocalClubMemberParentDirectory(): Optional<String> =
        Optional.ofNullable(
            System.getProperties()["jb.vmOptionsFile"] as? String
                ?: System.getProperties()["idea.config.path"] as? String
        )
            .map { property -> property.split(",") }
            .filter { properties -> properties.isNotEmpty() }
            .map { paths -> paths[paths.size - 1] }
            .map { property ->
                val directory = Paths.get(property)
                if (directory.isFile()) {
                    directory.parent
                } else {
                    directory
                }.toAbsolutePath().toString()
            }


    private fun setProperty(
        imagePath: String,
        opacity: String,
        fill: String, anchor: String,
        propertyKey: String
    ) {
        //org.intellij.images.editor.actions.SetBackgroundImageDialog has all of the answers
        //as to why this looks this way
        val propertyValue = listOf(imagePath, opacity, fill, anchor)
            .reduceRight { a, b -> "$a,$b" }
        setPropertyValue(propertyKey, propertyValue)
    }

    private fun setPropertyValue(propertyKey: String, propertyValue: String) {
        PropertiesComponent.getInstance().unsetValue(propertyKey)
        PropertiesComponent.getInstance().setValue(propertyKey, propertyValue)
    }


}