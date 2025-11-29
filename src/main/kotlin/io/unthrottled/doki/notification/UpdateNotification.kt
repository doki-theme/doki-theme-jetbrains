package io.unthrottled.doki.notification

import com.intellij.ide.plugins.PluginManager
import com.intellij.ide.plugins.PluginManagerCore.getPlugin
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.impl.HTMLEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.impl.IdeBackgroundUtil
import com.intellij.ui.ColorUtil
import com.intellij.ui.JBColor
import com.intellij.util.ui.UIUtil
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.icon.DokiIcons
import io.unthrottled.doki.themes.Background
import io.unthrottled.doki.themes.DokiTheme
import io.unthrottled.doki.themes.ThemeManager
import io.unthrottled.doki.util.BalloonPosition
import io.unthrottled.doki.util.BalloonTools
import io.unthrottled.doki.util.Logging
import io.unthrottled.doki.util.logger
import io.unthrottled.doki.util.runSafely
import io.unthrottled.doki.util.toHexString
import org.intellij.lang.annotations.Language
import org.jetbrains.annotations.Nls
import java.awt.Color

@Suppress("LongMethod", "MaxLineLength", "UnexpectedIndentation")
@Language("HTML")
private fun buildUpdateMessage(
  currentTheme: DokiTheme,
  isNewUser: Boolean,
  versionNumber: String,
): String {
  val accentColor =
    JBColor.namedColor(
      DokiTheme.ACCENT_COLOR,
      UIUtil.getTextAreaForeground(),
    )
  val darkerAccentColor = ColorUtil.darker(accentColor, 1).toHexString()
  val accentHex = accentColor.toHexString()
  val strokeColor =
    JBColor.namedColor("Doki.Icon.Accent.Contrast.color", Color.WHITE)
      .toHexString()
  val infoForegroundHex = UIUtil.getContextHelpForeground().toHexString()
  val background =
    currentTheme.getBackground().orElse(
      Background("essex_dark.png", IdeBackgroundUtil.Anchor.MIDDLE_RIGHT, 1),
    )

  val greeting =
    if (isNewUser) {
      //language=html
      """
      <p>Looks like you are new, <strong>welcome!</strong> You now have a <em>lot</em> of themes now.<br />
          Feel free to browse <br /> <a
            href="https://doki-theme.unthrottled.io/themes">https://doki-theme.unthrottled.io/themes</a></p>
      """.trimIndent()
    } else {
      //language=html
      """
      <p><strong>Thank You</strong> for updating! I changed some things.<br />
          You can find more information here: <br /> <a
            href="https://doki-theme.unthrottled.io/products/jetbrains/updates/$versionNumber?themeId=${currentTheme.id}">
              https://doki-theme.unthrottled.io/products/jetbrains/updates/$versionNumber
      </a></p>
      """.trimIndent()
    }

  val anchor =
    getAnchor(
      background.position,
    )
  return """
    <html lang='en'>

    <head>
        <meta charset="utf-8"/>
        <style>
            @font-face {
                font-family: "Victor Mono";
                src: url("https://doki-theme.unthrottled.io/victor-mono/VictorMono-MediumItalic.woff") format("woff");
                font-weight: 300;
                font-style: italic;
            }

            @font-face {
                font-family: "Victor Mono";
                src: url("https://doki-theme.unthrottled.io/victor-mono/VictorMono-Medium.woff") format("woff");
                font-weight: 700;
                font-style: normal;
            }
    
    
            body {
                background-color: ${currentTheme.getColor("baseBackground").toHexString()};
                margin: 0;
                font-size: large;
                color: ${currentTheme.getColor("foregroundColor").toHexString()};
                box-sizing: border-box;
                font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto,
                Oxygen-Sans, Ubuntu, Cantarell, "Helvetica Neue", sans-serif;
                text-align: center;
            }
    
            *::-webkit-scrollbar-thumb {
                background-color: ${currentTheme.getColor("accentColorTransparent").toHexString()};
            }

            *::-webkit-scrollbar {
                width: 0.5em;
            }

            ::-moz-selection {
                background: ${currentTheme.getColor("selectionBackground").toHexString()};
                color: ${currentTheme.getColor("selectionForeground").toHexString()};
            }
    
    
    
            ::selection {
            background: ${currentTheme.getColor("selectionBackground").toHexString()};
            color: ${currentTheme.getColor("selectionForeground").toHexString()};
          }

          .center {
            text-align: center;
          }

          a {
            color: ${currentTheme.editorAccentColor.toHexString()};
            font-weight: bold;
          }

          h2 {
            margin: 16px 0;
            font-weight: 300;
            font-size: 22px;
          }

          h3 {
            margin: 4px 0;
            font-weight: bold;
            font-size: 14px;
            color: ${currentTheme.getColor("infoForeground").toHexString()}
          }

          .accented {
            color: ${currentTheme.editorAccentColor.toHexString()};
          }

          .info-foreground {
            color: $infoForegroundHex;
            text-align: center;
          }

          .header {
            color: ${currentTheme.editorAccentColor.toHexString()};
            text-align: center;
          }

          .logo-container {
            margin-top: 8px;
            text-align: center;
          }

          .display-image {
            max-height: 256px;
            text-align: center;
          }


          #main {
            position: fixed !important;
            width: 100%;
            height: 100%;
            z-index: -3;
          }

          main {
            padding: 2rem;
          }

          .wallpaper {
            position: fixed;
            width: 100%;
            height: 100%;
            z-index: 0;
            top: 0;
            left: 0;
            background: url('https://doki.assets.unthrottled.io/backgrounds/wallpapers/transparent/smol/${background.name}') $anchor;
            background-size: cover;
          }

          #backgroundImage {
            z-index: -2;
          }

          h1 {
            font-size: 3rem;
            font-weight: 300;
          }

          p {
            margin-bottom: 2rem;
          }

          h3 {
            padding-top: 1rem;
            margin-bottom: 2rem;
            font-weight: 500;
            font-size: 1.25rem;
            font-family: "Victor Mono", "Courier New", Courier, monospace;
            font-style: italic;
          }
        </style>
        <script>
          let previousListener;

          function drawBackground() {
            const backgroundCanvas = document.getElementById(
              "backgroundImage"
            );
            const ctx = backgroundCanvas.getContext("2d");
            if (!ctx) return;
            const mainCanvas = document.getElementById("main");
            if (!mainCanvas) {
              return;
            }

            const boundingRect = mainCanvas.getBoundingClientRect();
            const w = boundingRect.width;
            const h = boundingRect.height;

            backgroundCanvas.setAttribute('width', String(w));
            backgroundCanvas.setAttribute('height', String(h))


            ctx.clearRect(0, 0, w, h);
            ctx.beginPath();
            ctx.moveTo(0, h * 0.85);
            ctx.quadraticCurveTo(w / 1.85, h, w, h / 2);
            ctx.lineTo(w, h);
            ctx.lineTo(0, h);

            const color = '${currentTheme.getColor("headerColor").toHexString()}'
            ctx.fillStyle = color;
            ctx.strokeStyle = color;
            ctx.fill();
            ctx.closePath();
            ctx.stroke();
          }

          document.addEventListener("DOMContentLoaded", () => {
            drawBackground();
            const listener = () => {
              drawBackground();
            };
            previousListener = listener;
            window.addEventListener('resize', listener);
          });
        </script>
      </head>

      <body>
        <div id="main">
          <canvas id="backgroundImage" width="150" height="150"></canvas>
          <div class="wallpaper"></div>
        </div>
        <main>
          <h1>Doki Theme</h1>
          <svg style="width: 100%; height: 100%; max-height: 145px;" version="1.1" viewBox="0 0 54.275 59.281"
            xmlns="http://www.w3.org/2000/svg">
            <path
              d="m52.577 14.952c1.9095 3.3073 1.9095 26.069 0 29.375-1.9095 3.3069-21.623 14.688-25.441 14.688-3.8189 0-23.532-11.381-25.441-14.688-1.9095-3.3073-1.9095-26.069 0-29.375 1.9095-3.3073 21.623-14.688 25.441-14.688 3.8189 0 23.532 11.381 25.441 14.688z"
              fill="$accentHex" stroke-width=".1248" style="paint-order:stroke fill markers" />
            <path
              d="m45.854 9.5987c0.03873 0.10755 0.07804 0.21489 0.1142 0.3235 0.73834 0.6891-6.4579 32.416-45.09 26.5-0.17914-0.54128-0.34649-1.074-0.5085-1.603 0.18835 4.4957 0.62782 8.3007 1.3245 9.5074 1.909 3.307 21.623 14.688 25.441 14.688 3.8182 0 23.532-11.381 25.441-14.688 1.9095-3.3059 1.9095-26.068 0-29.376-0.6433-1.1144-3.3263-3.1501-6.7235-5.3535z"
              fill="$darkerAccentColor" stroke-width=".1248" style="paint-order:stroke fill markers" />
            <path
              d="m27.363 21.738c0.86641-0.47597 3.7877-2.4016 8.056-3.3763 9.8408-2.2472 13.623 8.603 7.8207 16.785-5.1841 5.953-10.565 8.6876-15.877 12.21-5.3114-3.5223-10.692-6.257-15.877-12.21-5.8028-8.1814-2.02-19.031 7.8207-16.785 4.2683 0.97466 7.1895 2.9003 8.056 3.3763"
              fill="$accentHex" stroke="$strokeColor" stroke-dasharray="10.83493501, 5.41746751, 2.70873375, 5.41746751"
              stroke-linecap="round" stroke-miterlimit="6" stroke-width="2.7087" style="paint-order:stroke fill markers" />
            <path transform="matrix(.26458 0 0 .26458 -.00010697 0)"
              d="m161.03 73.684c-15.8 27.92-48.546 62.403-110.91 66.361 17.508 17.666 35.507 27.145 53.301 38.945 20.077-13.313 40.414-23.648 60.008-46.148 15.276-21.541 12.97-47.97-2.4024-59.158z"
              fill="$darkerAccentColor" style="paint-order:stroke fill markers" />
          </svg>
          <h3>A large collection of themes built with love and care</h3>

          <p>Unfortunately, I was unable to load your update.<br />
            So you are stuck with me, the offline fallback.</p>
          $greeting  
        </main>
      </body>

      </html>
    """.trimIndent()
}

fun getAnchor(position: IdeBackgroundUtil.Anchor): String {
  return when (position) {
    IdeBackgroundUtil.Anchor.MIDDLE_LEFT -> "left"
    IdeBackgroundUtil.Anchor.MIDDLE_RIGHT -> "right"
    else -> "center"
  }
}

object UpdateNotification : Logging {
  private val notificationGroup by lazy {
    NotificationGroupManager.getInstance()
      .getNotificationGroup("Doki Theme Updates")
  }

  private val defaultListener = NotificationListener.UrlOpeningListener(false)

  fun showDokiNotification(
    @Nls(capitalization = Nls.Capitalization.Sentence) title: String = "",
    @Nls(capitalization = Nls.Capitalization.Sentence) content: String,
    project: Project? = null,
    listener: NotificationListener = defaultListener,
    actions: List<AnAction> = emptyList(),
  ) {
    val notification = buildNotification(content, title, listener, actions)
    notification.notify(project)
  }

  private fun buildNotification(
    content: String,
    title: String,
    listener: NotificationListener,
    actions: List<AnAction>,
  ): Notification {
    val notification =
      notificationGroup.createNotification(
        content,
        NotificationType.INFORMATION,
      )
        .setTitle(title)
        .setListener(listener)
        .setIcon(DokiIcons.General.PLUGIN_LOGO)
    actions.forEach {
      notification.addAction(it)
    }
    notification.isImportant = true
    return notification
  }

  @Suppress("LongParameterList") // cuz I said so
  fun showStickyDokiNotification(
    @Nls(capitalization = Nls.Capitalization.Sentence) title: String = "",
    @Nls(capitalization = Nls.Capitalization.Sentence) content: String,
    project: Project,
    listener: NotificationListener = defaultListener,
    actions: List<AnAction> = emptyList(),
    balloonPosition: BalloonPosition,
  ) {
    val notification = buildNotification(content, title, listener, actions)
    BalloonTools.showStickyNotification(
      project,
      notification,
      balloonPosition,
    ) {
    }
  }

  fun showNotificationAcrossProjects(
    @Nls(capitalization = Nls.Capitalization.Sentence) title: String = "",
    @Nls(capitalization = Nls.Capitalization.Sentence) content: String,
    listener: NotificationListener = defaultListener,
    actions: List<(Project?) -> AnAction> = emptyList(),
  ) {
    ProjectManager.getInstance().openProjects.forEach { project ->
      showDokiNotification(title, content, project, listener, actions.map { it(project) })
    }
  }

  fun sendMessage(
    title: String,
    message: String,
    project: Project? = null,
  ) {
    showDokiNotification(
      title,
      message,
      project = project,
      listener = defaultListener,
    )
  }

  fun display(
    project: Project,
    newVersion: String,
    isNewUser: Boolean,
  ) {
    val title = getPluginUpdateTitle()
    val currentTheme = ThemeManager.instance.currentTheme.orElse(ThemeManager.instance.defaultTheme)
    val content = buildUpdateMessage(currentTheme, isNewUser, newVersion)
    val url = buildUrl(isNewUser, newVersion, currentTheme)
    ApplicationManager.getApplication().invokeLater {
      runSafely({
        HTMLEditorProvider.openEditor(
          project,
          title,
          url,
          content,
        )
      }) {
        logger().warn("Unable to show update notification for raisins.", it)
      }
    }
  }

  private fun buildUrl(
    isNewUser: Boolean,
    newVersion: String,
    currentTheme: DokiTheme,
  ): String {
    val urlParameters =
      if (isNewUser) {
        ""
      } else {
        "/products/jetbrains/updates/$newVersion"
      }
    val url = "https://doki-theme.unthrottled.io$urlParameters?themeId=${currentTheme.id}"
    return url
  }

  fun getPluginUpdateTitle(): String {
    val pluginName =
      getPlugin(
        PluginManager.getPluginByClass(
          UpdateNotification::class.java,
        )?.pluginId,
      )?.name
    return "$pluginName Update"
  }

  fun reconstructUrlAndContent(dokiTheme: DokiTheme): Pair<String, String> {
    val versionNumber = ThemeConfig.instance.version.substringAfter("v")
    val newUrl = buildUrl(false, versionNumber, dokiTheme)
    val content =
      buildUpdateMessage(
        dokiTheme,
        false,
        versionNumber,
      )
    return newUrl to content
  }
}
