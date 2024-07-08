package io.unthrottled.doki.hax.svg;

import com.intellij.openapi.util.IconLoader
import com.intellij.ui.svg.*
import org.w3c.dom.Element
import java.awt.*
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream
import java.net.URL
import kotlin.math.max

private val iconMaxSize: Float by lazy {
  var maxSize = Integer.MAX_VALUE.toFloat()
  if (!GraphicsEnvironment.isHeadless()) {
    val device = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
    val bounds = device.defaultConfiguration.bounds
    val tx = device.defaultConfiguration.defaultTransform
    maxSize = max(bounds.width * tx.scaleX, bounds.height * tx.scaleY).toInt().toFloat()
  }
  maxSize
}

/**
 * Plugins should use [ImageLoader.loadFromResource].
 */
object SVGLoader {
  const val ICON_DEFAULT_SIZE: Int = 16

  @Throws(IOException::class)
  @JvmStatic
  fun load(url: URL, scale: Float): Image {
    return loadSvg(path = url.path, stream = url.openStream(), scale = scale, colorPatcherProvider = null)
  }

  @Throws(IOException::class)
  @JvmStatic
  fun load(stream: InputStream, scale: Float): Image {
    return loadSvg(path = null, stream = stream, scale = scale, colorPatcherProvider = null)
  }

  @Throws(IOException::class)
  fun load(url: URL?, stream: InputStream, scale: Float): BufferedImage {
    return loadSvg(path = url?.path, stream = stream, scale = scale, colorPatcherProvider = null)
  }

  @JvmStatic
  var colorPatcherProvider: SvgElementColorPatcherProvider? = null
    set(colorPatcher) {
      field = colorPatcher
      IconLoader.clearCache()
    }

  @Deprecated("Please use SvgAttributePatcher")
  @Suppress("unused")
  interface SvgElementColorPatcher {
    fun patchColors(svg: Element) {
    }

    /**
     * @return hash code of the current SVG color patcher or null to disable rendered SVG images caching
     */
    fun digest(): ByteArray?
  }

}

