package io.acari.DDLC.themes

import com.chrisrm.ideaddlc.MTConfig
import com.chrisrm.ideaddlc.MTThemeManager
import com.chrisrm.ideaddlc.laf.MTDarkLaf
import com.chrisrm.ideaddlc.laf.MTLightLaf
import com.chrisrm.ideaddlc.themes.models.MTSerializedTheme
import com.chrisrm.ideaddlc.themes.models.MTThemeable
import com.chrisrm.ideaddlc.utils.PropertiesParser
import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.laf.IntelliJLookAndFeelInfo
import com.intellij.ide.ui.laf.LafManagerImpl
import com.intellij.ide.ui.laf.darcula.DarculaLookAndFeelInfo
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.JBColor
import io.acari.DDLC.LegacySupportUtility
import io.acari.DDLC.legacy.Runner
import java.awt.Color
import java.io.Serializable
import java.util.stream.Stream
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.plaf.ColorUIResource

abstract class DokiDokiTheme(val ddlcThemeId: String,
                             val isDarkTheme: Boolean,
                             val colorScheme: String,
                             val clubMemberIcon: String,
                             val clubMemberName: String
): Serializable, MTThemeable, MTSerializedTheme {

  init {

  }

  override fun getThemeId() = this.ddlcThemeId
  override fun isDark() = this.isDarkTheme
  override fun getThemeColorScheme() = this.colorScheme
  override fun getThemeName() = this.clubMemberName
  override fun getThemeIcon() = this.clubMemberIcon

  override fun activate() {
    try {
      if (isDark) {
        LegacySupportUtility.invokeVoidMethodSafely(
            LafManagerImpl::class.java,
            "getTestInstance",
            Runner{ LafManagerImpl.getTestInstance().setCurrentLookAndFeel(DarculaLookAndFeelInfo()) },
            Runner{
              LafManager.getInstance().setCurrentLookAndFeel(DarculaLookAndFeelInfo())
              UIManager.setLookAndFeel(MTDarkLaf(this))
            }
        )

      } else {
        LegacySupportUtility.invokeVoidMethodSafely(
            LafManagerImpl::class.java,
            "getTestInstance",
            Runner{ LafManagerImpl.getTestInstance().setCurrentLookAndFeel(IntelliJLookAndFeelInfo()) },
            Runner{
              LafManager.getInstance().setCurrentLookAndFeel(IntelliJLookAndFeelInfo())
              UIManager.setLookAndFeel(MTLightLaf(this))
            }
        )

      }
      JBColor.setDark(isDark)
      IconLoader.setUseDarkIcons(isDark)
      buildResources(getBackgroundResources(), contrastifyBackground(backgroundColorString))
      buildResources(getButtonBackgroundResources(), getButtonBackgroundColor())
      buildResources(getButtonForegroundResources(), getButtonForegroundColor())
      buildResources(getForegroundResources(), foregroundColorString)
      buildResources(getMenuItemForegroundResources(), getMenuItemForegroundColor())
      buildResources(getTextResources(), contrastifyForeground(textColorString))
      buildResources(getSelectionBackgroundResources(), selectionBackgroundColorString)
      buildResources(getSelectionForegroundResources(), selectionForegroundColorString)

      buildResources(getInactiveResources(), getInactiveColorString())
      buildResources(getSecondaryBackgroundResources(), secondaryBackgroundColorString)
      buildResources(getSecondaryForegroundResources(), getSecondaryForegroundColorString())
      buildResources(getDisabledResources(), disabledColorString)
      buildResources(getContrastResources(), contrastifyBackground(contrastColorString))
      buildResources(getTableSelectedResources(), tableSelectedColorString)
      buildResources(getSecondBorderResources(), secondBorderColorString)
      buildResources(getHighlightResources(), highlightColorString)

      buildResources(getMenuItemSelectionBackgroundResources(), getMenuBarSelectionBackgroundColorString())
      buildResources(getMenuItemSelectionForegroundResources(), getMenuBarSelectionForegroundColorString())

      buildResources(getTreeSelectionBackgroundResources(), getTreeSelectionBackgroundColorString())
      buildResources(getTreeSelectionForegroundResources(), getTreeSelectionForegroundColorString())
      buildResources(getNotificationsResources(), notificationsColorString)
      buildNotificationsColors()

      // Apply theme accent color if said so
      val instance = MTConfig.getInstance()
      if (instance.isOverrideAccentColor) {
        instance.accentColor = accentColor
        MTThemeManager.applyAccents(true)
      }

      if (isDark) {
        UIManager.setLookAndFeel(MTDarkLaf(this))
      } else {
        UIManager.setLookAndFeel(MTLightLaf(this))
      }
      JBColor.setDark(isDark)
      IconLoader.setUseDarkIcons(isDark)
    } catch (e: UnsupportedLookAndFeelException) {
      e.printStackTrace()
    }

  }

  override fun isCustom(): Boolean = false

  override fun getBackgroundColor(): Color = ColorUIResource(backgroundColorResource)

  override fun getContrastColor(): Color = ColorUIResource(contrastColorResource)

  override fun getForegroundColor(): Color = foregroundColorResource

  override fun getSelectionBackgroundColor(): Color = secondaryBackgroundColorResource

  override fun getSelectionForegroundColor(): Color = selectionForegroundColorResource

  override fun getExcludedColor(): Color = excludedColorResource

  override fun getPrimaryColor(): Color = textColorResource

  fun getTreeSelectionBackgroundResources(): Stream<String> {
    return Stream.of("Tree.selectionBackground")
  }

  fun getTreeSelectionForegroundResources(): Stream<String> {
    return Stream.of("Tree.selectionForeground")
  }



  /**
   * Get the hex code for the notifications color
   */
  abstract override fun getNotificationsColorString(): String

  private fun buildNotificationsColors() {
    val errorColor = JBColor(ColorUIResource(0xef5350), ColorUIResource(0xb71c1c))
    UIManager.put("Notification.ToolWindowError.background", errorColor)
    UIManager.put("Notification.ToolWindowError.borderColor", errorColor)

    val warnColor = JBColor(ColorUIResource(0xFFD54F), ColorUIResource(0x5D4037))
    UIManager.put("Notification.ToolWindowWarning.background", warnColor)
    UIManager.put("Notification.ToolWindowWarning.borderColor", warnColor)

    val infoColor = JBColor(ColorUIResource(0x66BB6A), ColorUIResource(0x1B5E20))
    UIManager.put("Notification.ToolWindowInfo.borderColor", infoColor)
    UIManager.put("Notification.ToolWindowInfo.background", infoColor)
  }

  private fun buildResources(resources: Stream<String>, color: String) {
    val o1 = PropertiesParser.parseColor(color)
    resources.forEach { resource -> UIManager.getDefaults()[resource] = o1 }
  }

}