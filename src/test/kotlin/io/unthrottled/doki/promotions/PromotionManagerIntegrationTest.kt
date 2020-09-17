package io.unthrottled.doki.promotions

import com.intellij.util.io.isFile
import io.mockk.Called
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import io.unthrottled.doki.assets.LocalStorageService
import io.unthrottled.doki.integrations.RestClient
import io.unthrottled.doki.test.tools.TestTools
import io.unthrottled.doki.test.tools.TestTools.setUpMocksForManager
import io.unthrottled.doki.test.tools.TestTools.tearDownMocksForPromotionManager
import io.unthrottled.doki.util.toOptional
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.nio.file.Files
import java.time.Instant

class PromotionManagerIntegrationTest {

  companion object {
    @JvmStatic
    @BeforeClass
    fun setUp() {
      setUpMocksForManager()
      mockkObject(MotivatorPromotionService)
      mockkObject(RestClient)
    }

    @JvmStatic
    @AfterClass
    fun tearDown() {
      tearDownMocksForPromotionManager()
      unmockkObject(MotivatorPromotionService)
      unmockkObject(RestClient)
    }
  }

  @Before
  fun cleanUp() {
    Files.walk(TestTools.getTestAssetPath())
      .filter { it.isFile() }
      .forEach { Files.deleteIfExists(it) }
  }

  @Test
  fun `should write new version`() {
    every { LocalStorageService.getGlobalAssetDirectory() } returns
      TestTools.getTestAssetPath().toString().toOptional()

    val beforePromotion = Instant.now()

    val promotionManager = PromotionManagerImpl()
    promotionManager.registerPromotion("Ryuko", true)

    val postLedger = LedgerMaster.readLedger()

    assertThat(postLedger.allowedToPromote).isTrue()
    assertThat(postLedger.user).isNotNull()
    assertThat(postLedger.seenPromotions.isEmpty()).isTrue()
    assertThat(postLedger.versionInstallDates.size).isEqualTo(1)
    assertThat(postLedger.versionInstallDates["Ryuko"]).isBetween(
      beforePromotion, Instant.now()
    )

    verify { MotivatorPromotionService wasNot Called }
  }

  @Test
  fun `should always write new version`() {
    every { LocalStorageService.getGlobalAssetDirectory() } returns
      TestTools.getTestAssetPath().toString().toOptional()

    val beforeRyuko = Instant.now()

    val promotionManager = PromotionManagerImpl()
    promotionManager.registerPromotion("Ryuko", true)

    val postRyukoLedger = LedgerMaster.readLedger()

    assertThat(postRyukoLedger.allowedToPromote).isTrue()
    assertThat(postRyukoLedger.user).isNotNull()
    assertThat(postRyukoLedger.seenPromotions.isEmpty()).isTrue()
    assertThat(postRyukoLedger.versionInstallDates.size).isEqualTo(1)
    assertThat(postRyukoLedger.versionInstallDates["Ryuko"]).isBetween(
      beforeRyuko, Instant.now()
    )

    val beforeRin = Instant.now()

    promotionManager.registerPromotion("Rin", true)

    val postRinLedger = LedgerMaster.readLedger()

    assertThat(postRinLedger.allowedToPromote).isTrue()
    assertThat(postRinLedger.user).isNotNull()
    assertThat(postRinLedger.seenPromotions.isEmpty()).isTrue()
    assertThat(postRinLedger.versionInstallDates.size).isEqualTo(2)
    assertThat(postRyukoLedger.versionInstallDates["Ryuko"]).isBetween(
      beforeRyuko, Instant.now()
    )
    assertThat(postRinLedger.versionInstallDates["Rin"]).isBetween(
      beforeRin, Instant.now()
    )

    verify { MotivatorPromotionService wasNot Called }
  }
}

