package io.unthrottled.doki.util

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.util.ui.Animator
import com.intellij.util.ui.GraphicsUtil
import com.intellij.util.ui.ImageUtil
import com.intellij.util.ui.UIUtil
import java.awt.AlphaComposite
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.Window
import java.awt.image.BufferedImage
import javax.swing.JComponent
import javax.swing.JLayeredPane
import javax.swing.RootPaneContainer
import kotlin.math.cos

class LAFAnimator {
  private var myAlpha = 1f
  private val myMap: MutableMap<JLayeredPane, JComponent> = LinkedHashMap()
  private val myAnimator: Animator

  companion object {
    fun showSnapshot(): LAFAnimator = LAFAnimator()
  }

  init {
    Window.getWindows()
      .filter { it is RootPaneContainer }
      .filter { it.isShowing }
      .map { it as RootPaneContainer }
      .forEach { rootPaneContainer ->
        val window = rootPaneContainer as Window
        val bounds = window.bounds
        val layeredPane = rootPaneContainer.layeredPane
        val image: BufferedImage =
          ImageUtil.createImage(
            window.graphicsConfiguration,
            bounds.width,
            bounds.height,
            BufferedImage.TYPE_INT_ARGB,
          )
        val imageGraphics = image.graphics
        GraphicsUtil.setupAntialiasing(imageGraphics)
        rootPaneContainer.rootPane.paint(imageGraphics)
        val imageLayer =
          object : JComponent() {
            override fun updateUI() {}

            override fun paint(g: Graphics) {
              val newG = g.create() as Graphics2D
              newG.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, myAlpha)
              UIUtil.drawImage(
                newG,
                image,
                0,
                0,
                this,
              )
            }

            override fun getBounds(): Rectangle = layeredPane.bounds
          }

        imageLayer.size = layeredPane.size
        layeredPane.add(imageLayer, JLayeredPane.DRAG_LAYER)
        myMap[layeredPane] = imageLayer
      }

    myAnimator =
      object :
        Animator(
          "ChangeLAF",
          60,
          800,
          false,
        ),
        Disposable {
        override fun paintNow(
          frame: Int,
          totalFrames: Int,
          cycle: Int,
        ) {
          myAlpha =
            1 - (1 - cos(Math.PI * frame / totalFrames.toFloat())).toFloat() / 2
          doPaint()
        }

        override fun resume() {
          doPaint()
          super.resume()
        }

        override fun dispose() {
          try {
            super.dispose()
            for ((layeredPane, value) in myMap) {
              layeredPane.remove(value)
              layeredPane.revalidate()
              layeredPane.repaint()
            }
          } finally {
            myMap.clear()
          }
        }

        override fun paintCycleEnd() {
          if (!isDisposed) {
            Disposer.dispose(this)
          }
          super.paintCycleEnd()
        }
      }
  }

  fun hideSnapshotWithAnimation() {
    myAnimator.resume()
  }

  private fun doPaint() {
    for ((key, value) in myMap) {
      if (key.isShowing) {
        value.revalidate()
        value.repaint()
      }
    }
  }
}

fun performWithAnimation(
  animate: Boolean,
  actionToPerform: () -> Unit,
) {
  val animator = if (animate) LAFAnimator.showSnapshot() else null
  actionToPerform()
  animator?.hideSnapshotWithAnimation()
}
