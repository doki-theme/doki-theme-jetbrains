package io.unthrottled.doki.icon

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.laf.UIThemeLookAndFeelInfoImpl
import com.intellij.ui.ColorUtil
import com.intellij.ui.JBColor
import com.intellij.ui.JBColor.namedColor
import com.intellij.ui.svg.SvgAttributePatcher
import com.intellij.util.io.DigestUtil
import io.unthrottled.doki.hax.PatcherProvider
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.runSafelyWithResult
import io.unthrottled.doki.util.toHexString
import io.unthrottled.doki.util.toOptional
import java.awt.Color
import java.time.Duration

object NoOptPatcher : SvgAttributePatcher {
  override fun patchColors(attributes: MutableMap<String, String>) {
  }
}

val noOptPatcherProvider =
  object : PatcherProvider {
    val longArray = longArrayOf(0)

    override fun digest(): LongArray {
      return longArray
    }
  }

@Suppress("TooManyFunctions")
object ColorPatcher : PatcherProvider {
  private var otherColorPatcherProvider: PatcherProvider = noOptPatcherProvider
  private var uiColorPatcherProvider: PatcherProvider = noOptPatcherProvider
  private lateinit var dokiTheme: DokiTheme
  private val patcherProviderCache = HashSet<String>()

  fun setDokiTheme(dokiTheme: DokiTheme) {
    this.dokiTheme = dokiTheme
    calculateAndSetNewDigest()
    LafManager.getInstance()
      ?.currentUIThemeLookAndFeel.toOptional()
      .filter { it is UIThemeLookAndFeelInfoImpl }
      .map { it as UIThemeLookAndFeelInfoImpl }
      .ifPresent {
        this.uiColorPatcherProvider = (it.theme.colorPatcher ?: noOptPatcherProvider) as PatcherProvider
      }
    clearCaches()
  }

  fun setOtherPatcher(otherPatcher: PatcherProvider) {
    this.otherColorPatcherProvider = otherPatcher
    clearCaches()
  }

  private fun clearCaches() {
    this.patcherCache.invalidateAll()
    this.patcherProviderCache.clear()
  }

  private lateinit var myDigest: LongArray

  override fun digest(): LongArray {
    return if (this::myDigest.isInitialized) {
      myDigest
    } else {
      calculateAndSetNewDigest()
    }
  }

  private fun calculateAndSetNewDigest(): LongArray {
    myDigest = calculateDigest()
    return myDigest
  }

  private fun calculateDigest(): LongArray {
    val shaDigest = DigestUtil.sha512()
    if (ColorPatcher::dokiTheme.isInitialized) {
      shaDigest.update((dokiTheme.id + dokiTheme.version).toByteArray(Charsets.UTF_8))
    } else {
      shaDigest.update(this.toString().toByteArray(Charsets.UTF_8))
    }
    val digest = shaDigest.digest()
    val longArray = LongArray(digest.size)
    digest.forEachIndexed { index, byteMe -> longArray[index] = byteMe.toLong() }
    return longArray
  }

  private val patcherCache: Cache<String, SvgAttributePatcher> =
    CacheBuilder.newBuilder()
      .expireAfterWrite(Duration.ofMinutes(1))
      .build()

  override fun attributeForPath(path: String): SvgAttributePatcher? {
    val safeKey = path ?: "ayyLmao"
    return if (patcherProviderCache.add(safeKey)) {
      val hackedPatcher =
        buildHackedPatcher(
          listOf(otherColorPatcherProvider, uiColorPatcherProvider)
            .distinct()
            .mapNotNull { patcherProvider ->
              runSafelyWithResult({
                patcherProvider.attributeForPath(path)
              }) {
                null
              }
            },
          safeKey,
        )
      patcherProviderCache.remove(safeKey)
      hackedPatcher
    } else {
      NoOptPatcher
    }
  }

  private fun buildHackedPatcher(
    otherPatchers: List<SvgAttributePatcher>,
    patcherKey: String,
  ): SvgAttributePatcher {
    val cachedPatcher = patcherCache.getIfPresent(patcherKey)
    if (cachedPatcher !== null) {
      return cachedPatcher
    }

    val recursionResistantPatcher =
      object : SvgAttributePatcher {
        private val svgCache = HashSet<MutableMap<String, String>>()

        override fun patchColors(attributes: MutableMap<String, String>) {
          if (svgCache.add(attributes)) {
            try {
              runSafely({
                patchAttributes(attributes)
              })
            } finally {
              svgCache.remove(attributes)
            }
          }
          patchAttributes(attributes)
        }

        private fun patchAttributes(attributes: MutableMap<String, String>) {
          otherPatchers.forEach { otherPatcher ->
            runSafely({
              otherPatcher.patchColors(attributes)
            })
          }

          patchAccent(attributes["accentTint"], attributes) {
            it.toHexString()
          }
          patchAccent(attributes["accentTintDarker"], attributes) {
            ColorUtil.darker(it, 1).toHexString()
          }
          patchAccent(attributes["accentContrastTint"], attributes) {
            getIconAccentContrastColor().toHexString()
          }
          patchAccent(attributes["stopTint"], attributes) {
            getThemedStopColor()
          }
          patchAccent(attributes["editorAccentTint"], attributes) {
            ThemeManager.instance.currentTheme
              .map { it.editorAccentColor.toHexString() }
              .orElseGet { JBColor.CYAN.toHexString() }
          }

          val themedStartAttr = attributes["themedStart"]
          val themedStopAttr = attributes["themedStop"]
          val themedFillAttr = attributes["themedFill"]
          when {
            "true" == themedStartAttr -> {
              val themedStart = getThemedStartColor()
              attributes["stop-color"] = themedStart
              attributes["fill"] = themedStart
            }

            "true" == themedStopAttr -> {
              val themedStop = getThemedStopColor()
              attributes["stop-color"] = themedStop
              attributes["fill"] = themedStop
            }

            "true" == themedFillAttr -> {
              val themedStart = getThemedStartColor()
              attributes["fill"] = themedStart
              attributes["stroke"] = themedStart
            }
          }
        }
      }

    patcherCache.put(patcherKey, recursionResistantPatcher)

    return recursionResistantPatcher
  }

  private fun patchAccent(
    attribute: String?,
    attributes: MutableMap<String, String>,
    colorDecorator: (Color) -> String,
  ) {
    when (attribute) {
      "fill" -> attributes["fill"] = colorDecorator(getAccentColor())
      "stroke" -> attributes["stroke"] = colorDecorator(getAccentColor())
      "both", "partialFill" -> {
        val accentColor = colorDecorator(getAccentColor())
        attributes["stroke"] = accentColor
        attributes["stroke-opacity"] = if (attribute == "both") "1" else "0.25"
        attributes["fill"] = accentColor
      }
    }
  }

  private fun getAccentColor() = namedColor("Doki.Accent.color", JBColor.CYAN)

  private fun getIconAccentContrastColor() = namedColor("Doki.Icon.Accent.Contrast.color", JBColor.WHITE)

  private fun getThemedStartColor() = namedColor("Doki.startColor", JBColor.CYAN).toHexString()

  private fun getThemedStopColor() = namedColor("Doki.stopColor", JBColor.CYAN).toHexString()
}
