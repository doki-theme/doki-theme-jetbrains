package io.unthrottled.doki.stickers

import java.awt.Dimension

object DimensionCappingService {

  fun getCappingStyle(
    stickerDimensions: Dimension,
    maxDimension: Dimension,
  ): Dimension {
    val maxHeight = maxDimension.height
    val maxWidth = maxDimension.width
    val setMaxHeight = maxHeight > 0
    val setMaxWidth = maxWidth > 0
    val stickerHeight = stickerDimensions.height
    val stickerWidth = stickerDimensions.width
    val heightIsGreaterThanOriginal = maxHeight < stickerHeight
    val widthIsGreaterThanOriginal = maxWidth < stickerWidth
    val needsToCap = heightIsGreaterThanOriginal || widthIsGreaterThanOriginal
    val canCap = (setMaxHeight || setMaxWidth)
    return if (needsToCap && canCap) {
      val heightIsGreater = stickerHeight > stickerWidth
      val capHeightIsGreater = (setMaxWidth && setMaxHeight && maxHeight <= maxWidth) ||
        (setMaxHeight && !setMaxWidth)
      val (width, height) =
        when {
          heightIsGreaterThanOriginal &&
            heightIsGreater &&
            capHeightIsGreater ->
            (stickerWidth / stickerHeight.toDouble()) * maxHeight to maxHeight
          widthIsGreaterThanOriginal && setMaxWidth ->
            maxWidth to (stickerHeight / stickerWidth.toDouble()) * maxWidth
          else -> stickerWidth to stickerHeight
        }
      Dimension(width.toInt(), height.toInt())
    } else {
      stickerDimensions
    }
  }
}
