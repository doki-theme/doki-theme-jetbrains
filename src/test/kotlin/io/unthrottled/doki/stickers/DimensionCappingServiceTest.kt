package io.unthrottled.doki.stickers

import org.assertj.core.api.Assertions
import org.junit.Test
import java.awt.Dimension

class DimensionCappingServiceTest {

  @Test
  fun getCappingStyleShouldNotCapIfBothNegativeOne() {
    val result = DimensionCappingService.getCappingStyle(
      Dimension(69, 420),
      Dimension(-1, -1),
    )

    Assertions.assertThat(result)
      .isEqualTo(Dimension(69, 420))
  }

  @Test
  fun getCappingStyleShouldMaintainAspectRationWhenScalingWidthHeightGreater() {
    val result = DimensionCappingService.getCappingStyle(
      Dimension(20, 40),
      Dimension(10, -1),
    )

    Assertions.assertThat(result)
      .isEqualTo(Dimension(10, 20))
  }

  @Test
  fun getCappingStyleShouldMaintainAspectRationWhenScalingHeightHeightGreater() {
    val result = DimensionCappingService.getCappingStyle(
      Dimension(20, 40),
      Dimension(-1, 10),
    )

    Assertions.assertThat(result)
      .isEqualTo(Dimension(5, 10))
  }

  @Test
  fun getCappingStyleShouldMaintainAspectRationWhenScalingWidthWidthGreater() {
    val result = DimensionCappingService.getCappingStyle(
      Dimension(40, 20),
      Dimension(10, -1),
    )

    Assertions.assertThat(result)
      .isEqualTo(Dimension(10, 5))
  }

  @Test
  fun getCappingStyleShouldMaintainAspectRationWhenScalingHeightWidthGreater() {
    val result = DimensionCappingService.getCappingStyle(
      Dimension(40, 20),
      Dimension(-1, 10),
    )

    Assertions.assertThat(result)
      .isEqualTo(Dimension(20, 10))
  }

  @Test
  fun getCappingStyleShouldMaintainAspectRatioAndRespectSmallestCapHeight() {
    val result = DimensionCappingService.getCappingStyle(
      Dimension(40, 20),
      Dimension(4, 10),
    )

    Assertions.assertThat(result)
      .isEqualTo(Dimension(4, 2))
  }

  @Test
  fun getCappingStyleShouldMaintainAspectRatioAndRespectSmallestCapWidth() {
    val result = DimensionCappingService.getCappingStyle(
      Dimension(40, 20),
      Dimension(10, 4),
    )

    Assertions.assertThat(result)
      .isEqualTo(Dimension(8, 4))
  }
}
