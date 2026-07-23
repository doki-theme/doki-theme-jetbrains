package io.unthrottled.doki.icon

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object DokiThemeIcons {
  @JvmField
  val CONFIG: Icon = IconLoader.getIcon("/icons/doki/pen.svg", DokiThemeIcons::class.java)
  @JvmField
  val SHAME: Icon = IconLoader.getIcon("/icons/emojis/1f648.svg", DokiThemeIcons::class.java)
  @JvmField
  val MAGIC: Icon = IconLoader.getIcon("/icons/doki/magic.svg", DokiThemeIcons::class.java)
  @JvmField
  val DIFF: Icon = IconLoader.getIcon("/icons/actions/diff.svg", DokiThemeIcons::class.java)
  @JvmField
  val DOKI_LOGO: Icon = IconLoader.getIcon("/icons/doki/Doki-Doki-Logo.svg", DokiThemeIcons::class.java)
  @JvmField
  val WALLPAPER: Icon = IconLoader.getIcon("/icons/doki/image.svg", DokiThemeIcons::class.java)
  @JvmField
  val GEAR_PLAIN: Icon = AllIcons.General.GearPlain
  @JvmField
  val FAVORITES: Icon = AllIcons.Nodes.Favorite
}