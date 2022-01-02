package io.unthrottled.doki.stickers

import java.awt.Dimension

object DimensionCappingService {

  fun getCappingStyle(
    stickerDimensions: Dimension,
    maxDimension: Dimension,
  ): Dimension {
    val maxHeight = maxDimension.height
    val maxWidth = maxDimension.width
    val shouldSetMaxHeight = maxHeight > 0
    val shouldSetMaxWidth = maxWidth > 0
    val comparisonMaxHeight = if (shouldSetMaxHeight) maxHeight else Int.MAX_VALUE
    val comparisonMaxWidth = if (shouldSetMaxWidth) maxWidth else Int.MAX_VALUE
    val stickerHeight = stickerDimensions.height
    val stickerWidth = stickerDimensions.width
    val stickerHeightGreaterThanCap = comparisonMaxHeight < stickerHeight
    val stickerWidthGreaterThanCap = comparisonMaxWidth < stickerWidth
    val needsToCap = stickerHeightGreaterThanCap || stickerWidthGreaterThanCap
    val canCap = (shouldSetMaxHeight || shouldSetMaxWidth)
    return if (needsToCap && canCap) {
      val (width, height) =
        when {
          shouldSetMaxHeight &&
            comparisonMaxHeight <= comparisonMaxWidth &&
            stickerHeightGreaterThanCap ->
            (stickerWidth / stickerHeight.toDouble()) * maxHeight to maxHeight
          shouldSetMaxWidth &&
            comparisonMaxWidth <= comparisonMaxHeight &&
            stickerWidthGreaterThanCap ->
            maxWidth to (stickerHeight / stickerWidth.toDouble()) * maxWidth
          else -> stickerWidth to stickerHeight
        }
      Dimension(width.toInt(), height.toInt())
    } else {
      stickerDimensions
    }
  }
}
