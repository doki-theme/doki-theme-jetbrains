package io.unthrottled.doki.hax

import io.unthrottled.doki.icon.ColorPatcher
import io.unthrottled.doki.service.PluginService
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.util.Logging
import io.unthrottled.doki.util.logger
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.runSafelyWithResult
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

object SvgLoaderHacker : Logging {
  /**
   * Enables the ability to have more than one color patcher.
   */
  fun setSVGColorPatcher(dokiTheme: DokiTheme) {
    if (PluginService.areIconsInstalled()) {
      return
    }

    ColorPatcher.setDokiTheme(dokiTheme)

    runSafely({
      setHackedPatcher()
    }) {
      logger().warn("Unable to set hacked patcher", it)
    }
  }

  private fun setHackedPatcher() {
    val patcherProxyHandler =
      object : InvocationHandler, Logging {
        val associatedMethods = ColorPatcher.javaClass.methods.associateBy { it.name }

        override fun invoke(
          proxy: Any?,
          method: Method?,
          arguments: Array<out Any>?,
        ): Any? {
          if (method == null) return null
          return runSafelyWithResult({
            val methodToInvoke = associatedMethods[method.name]
            val usableArguments = arguments ?: emptyArray()
            methodToInvoke?.invoke(
              ColorPatcher,
              *usableArguments,
            )
          }) {
            logger().warn("unable to invoke proxy handler method", it)
            null
          }
        }
      }
    val patcherProviderClass = Class.forName("com.intellij.util.SVGLoader\$SvgElementColorPatcherProvider")
    val proxiedSVGElementColorProvider =
      Proxy.newProxyInstance(
        patcherProviderClass.classLoader,
        arrayOf(patcherProviderClass),
        patcherProxyHandler,
      )
    val svgLoaderClass = Class.forName("com.intellij.util.SVGLoader")
    val setPatcher = svgLoaderClass.declaredMethods.firstOrNull { it.name == "setColorPatcherProvider" }
    setPatcher?.invoke(null, proxiedSVGElementColorProvider)

    val clazz = Class.forName("com.intellij.ui.svg.SvgKt")
    val setPatcherProvider = clazz.declaredMethods.firstOrNull { it.name == "setSelectionColorPatcherProvider" }
    setPatcherProvider?.invoke(null, proxiedSVGElementColorProvider)
  }
}
