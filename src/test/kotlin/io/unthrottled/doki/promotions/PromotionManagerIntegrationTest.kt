package io.unthrottled.doki.promotions

import com.intellij.util.io.isFile
import io.mockk.Called
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import io.unthrottled.doki.assets.AssetManager
import io.unthrottled.doki.assets.LocalStorageService
import io.unthrottled.doki.integrations.RestClient
import io.unthrottled.doki.service.PluginService
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
import java.time.Period
import java.util.Optional
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue

class PromotionManagerIntegrationTest {

  companion object {
    private const val testDirectory = "testOne"
    @JvmStatic
    @BeforeClass
    fun setUp() {
      setUpMocksForManager()
      mockkObject(MotivatorPromotionService)
      mockkObject(RestClient)
      mockkObject(PluginService)
      mockkObject(WeebService)
    }

    @JvmStatic
    @AfterClass
    fun tearDown() {
      tearDownMocksForPromotionManager()
      unmockkObject(MotivatorPromotionService)
      unmockkObject(RestClient)
      unmockkObject(PluginService)
      unmockkObject(WeebService)
    }
  }

  @Before
  fun cleanUp() {
    clearMocks(MotivatorPromotionService)
    Files.walk(TestTools.getTestAssetPath(testDirectory))
      .filter { it.isFile() }
      .forEach { Files.deleteIfExists(it) }
  }

  @Test
  fun `should write new version`() {
    every { LocalStorageService.getGlobalAssetDirectory() } returns
      TestTools.getTestAssetPath(testDirectory).toString().toOptional()
    every { PluginService.isMotivatorInstalled() } returns false
    every { WeebService.isWeebStuffOn() } returns true
    every { RestClient.performGet("${AssetManager.ASSETS_SOURCE}/misc/am-i-online.txt") } returns
      """         
        yes       
              
      """.toOptional()

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
      TestTools.getTestAssetPath(testDirectory).toString().toOptional()
    every { PluginService.isMotivatorInstalled() } returns false
    every { WeebService.isWeebStuffOn() } returns true
    every { RestClient.performGet("${AssetManager.ASSETS_SOURCE}/misc/am-i-online.txt") } returns
      """         
        yes       
              
      """.toOptional()

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

  @Test
  fun `should not do anything when install is less than a day old`() {
    every { LocalStorageService.getGlobalAssetDirectory() } returns
      TestTools.getTestAssetPath(testDirectory).toString().toOptional()
    every { PluginService.isMotivatorInstalled() } returns false
    every { WeebService.isWeebStuffOn() } returns true
    every { RestClient.performGet("${AssetManager.ASSETS_SOURCE}/misc/am-i-online.txt") } returns
      """         
        yes       
              
      """.toOptional()

    val currentLedger = PromotionLedger(
      UUID.randomUUID(),
      mutableMapOf("Ryuko" to Instant.now()),
      mutableMapOf(),
      true
    )
    LedgerMaster.persistLedger(currentLedger)

    val promotionManager = PromotionManagerImpl()
    promotionManager.registerPromotion("Ryuko", true)

    val postLedger = LedgerMaster.readLedger()

    assertThat(postLedger).isEqualTo(currentLedger)

    verify { MotivatorPromotionService wasNot Called }
  }

  @Test
  fun `should not do anything when motivator is installed`() {
    every { LocalStorageService.getGlobalAssetDirectory() } returns
      TestTools.getTestAssetPath(testDirectory).toString().toOptional()

    every { PluginService.isMotivatorInstalled() } returns true
    every { WeebService.isWeebStuffOn() } returns true
    every { RestClient.performGet("${AssetManager.ASSETS_SOURCE}/misc/am-i-online.txt") } returns
      """         
        yes       
              
      """.toOptional()

    val currentLedger = PromotionLedger(
      UUID.randomUUID(),
      mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
      mutableMapOf(),
      true
    )

    LedgerMaster.persistLedger(currentLedger)

    val promotionManager = PromotionManagerImpl()
    promotionManager.registerPromotion("Ryuko", true)

    val postLedger = LedgerMaster.readLedger()

    assertThat(postLedger).isEqualTo(currentLedger)

    verify { MotivatorPromotionService wasNot Called }
  }

  @Test
  fun `should not do anything when has been promoted before`() {
    every { LocalStorageService.getGlobalAssetDirectory() } returns
      TestTools.getTestAssetPath(testDirectory).toString().toOptional()
    every { PluginService.isMotivatorInstalled() } returns false
    every { WeebService.isWeebStuffOn() } returns true
    every { RestClient.performGet("${AssetManager.ASSETS_SOURCE}/misc/am-i-online.txt") } returns
      """         
        yes       
              
      """.toOptional()

    val currentLedger = PromotionLedger(
      UUID.randomUUID(),
      mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
      mutableMapOf(
        MOTIVATION_PROMOTION_ID to Promotion(
          MOTIVATION_PROMOTION_ID, Instant.now(), PromotionStatus.REJECTED
        )
      ),
      true
    )

    LedgerMaster.persistLedger(currentLedger)

    val promotionManager = PromotionManagerImpl()
    promotionManager.registerPromotion("Ryuko", true)

    val postLedger = LedgerMaster.readLedger()

    assertThat(postLedger).isEqualTo(currentLedger)

    verify { MotivatorPromotionService wasNot Called }
  }

  @Test
  fun `should not do anything when weeb stuff is not on`() {
    every { LocalStorageService.getGlobalAssetDirectory() } returns
      TestTools.getTestAssetPath(testDirectory).toString().toOptional()
    every { PluginService.isMotivatorInstalled() } returns false
    every { WeebService.isWeebStuffOn() } returns false
    every { RestClient.performGet("${AssetManager.ASSETS_SOURCE}/misc/am-i-online.txt") } returns
      """         
        yes       
              
      """.toOptional()

    val currentLedger = PromotionLedger(
      UUID.randomUUID(),
      mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
      mutableMapOf(),
      true
    )

    LedgerMaster.persistLedger(currentLedger)

    val promotionManager = PromotionManagerImpl()
    promotionManager.registerPromotion("Ryuko", true)

    val postLedger = LedgerMaster.readLedger()

    assertThat(postLedger).isEqualTo(currentLedger)

    verify { MotivatorPromotionService wasNot Called }
  }

  @Test
  fun `should not do anything when not online`() {
    every { LocalStorageService.getGlobalAssetDirectory() } returns
      TestTools.getTestAssetPath(testDirectory).toString().toOptional()
    every { PluginService.isMotivatorInstalled() } returns false
    every { WeebService.isWeebStuffOn() } returns true
    every { RestClient.performGet("${AssetManager.ASSETS_SOURCE}/misc/am-i-online.txt") } returns
      "no".toOptional() andThen Optional.empty()

    val currentLedger = PromotionLedger(
      UUID.randomUUID(),
      mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
      mutableMapOf(),
      true
    )

    LedgerMaster.persistLedger(currentLedger)

    val promotionManager = PromotionManagerImpl()
    promotionManager.registerPromotion("Ryuko", true)
    promotionManager.registerPromotion("Ryuko", true)

    val postLedger = LedgerMaster.readLedger()

    assertThat(postLedger).isEqualTo(currentLedger)

    verify { MotivatorPromotionService wasNot Called }
  }

  @Test
  fun `should not do anything when not owner of lock`() {
    every { LocalStorageService.getGlobalAssetDirectory() } returns
      TestTools.getTestAssetPath(testDirectory).toString().toOptional()
    every { PluginService.isMotivatorInstalled() } returns false
    every { WeebService.isWeebStuffOn() } returns true
    every { RestClient.performGet("${AssetManager.ASSETS_SOURCE}/misc/am-i-online.txt") } returns
      """         
        yes       
              
      """.toOptional()

    assertThat(LockMaster.acquireLock("Misato")).isTrue()

    val currentLedger = PromotionLedger(
      UUID.randomUUID(),
      mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
      mutableMapOf(),
      true
    )

    LedgerMaster.persistLedger(currentLedger)

    val promotionManager = PromotionManagerImpl()
    promotionManager.registerPromotion("Ryuko", true)

    val postLedger = LedgerMaster.readLedger()

    assertThat(postLedger).isEqualTo(currentLedger)

    verify { MotivatorPromotionService wasNot Called }
  }

  @Test
  fun `should promote when not locked`() {
    every { LocalStorageService.getGlobalAssetDirectory() } returns
      TestTools.getTestAssetPath(testDirectory).toString().toOptional()
    every { PluginService.isMotivatorInstalled() } returns false
    every { WeebService.isWeebStuffOn() } returns true
    every { RestClient.performGet("${AssetManager.ASSETS_SOURCE}/misc/am-i-online.txt") } returns
      """         
        yes       
              
      """.toOptional()

    val currentLedger = PromotionLedger(
      UUID.randomUUID(),
      mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
      mutableMapOf(),
      true
    )

    LedgerMaster.persistLedger(currentLedger)

    val beforePromotion = Instant.now()
    val promotionManager = PromotionManagerImpl()
    promotionManager.registerPromotion("Ryuko", true)

    val postLedger = LedgerMaster.readLedger()

    assertThat(postLedger).isEqualTo(currentLedger)

    validateLedgerCallback(currentLedger, beforePromotion)

    assertTrue { LockMaster.acquireLock("Syrena") }
  }

  @Test
  fun `should break old lock`() {
    every { LocalStorageService.getGlobalAssetDirectory() } returns
      TestTools.getTestAssetPath(testDirectory).toString().toOptional()
    every { PluginService.isMotivatorInstalled() } returns false
    every { WeebService.isWeebStuffOn() } returns true
    every { RestClient.performGet("${AssetManager.ASSETS_SOURCE}/misc/am-i-online.txt") } returns
      """         
        yes       
              
      """.toOptional()

    LockMaster.writeLock(Lock("Misato", Instant.now().minusMillis(
      TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS)
    )))

    val currentLedger = PromotionLedger(
      UUID.randomUUID(),
      mutableMapOf("Ryuko" to Instant.now().minus(Period.ofDays(3))),
      mutableMapOf(),
      true
    )

    LedgerMaster.persistLedger(currentLedger)

    val beforePromotion = Instant.now()
    val promotionManager = PromotionManagerImpl()
    promotionManager.registerPromotion("Ryuko", true)

    val postLedger = LedgerMaster.readLedger()

    assertThat(postLedger).isEqualTo(currentLedger)

    validateLedgerCallback(currentLedger, beforePromotion)

    assertTrue { LockMaster.acquireLock("Syrena") }
  }

  private fun validateLedgerCallback(
    currentLedger: PromotionLedger,
    beforePromotion: Instant?
  ) {
    val promotionSlot = slot<(PromotionResults) -> Unit>()
    verify { MotivatorPromotionService.runPromotion(capture(promotionSlot)) }

    val promotionCallback = promotionSlot.captured
    promotionCallback(PromotionResults(PromotionStatus.BLOCKED))

    val postBlockedTime = Instant.now()
    val postBlocked = LedgerMaster.readLedger()
    assertThat(postBlocked.user).isEqualTo(currentLedger.user)
    assertThat(postBlocked.versionInstallDates).isEqualTo(currentLedger.versionInstallDates)
    assertThat(postBlocked.allowedToPromote).isFalse()
    assertThat(postBlocked.seenPromotions[MOTIVATION_PROMOTION_ID]?.result).isEqualTo(PromotionStatus.BLOCKED)
    assertThat(postBlocked.seenPromotions[MOTIVATION_PROMOTION_ID]?.id).isEqualTo(MOTIVATION_PROMOTION_ID)
    assertThat(postBlocked.seenPromotions[MOTIVATION_PROMOTION_ID]?.datePromoted).isBetween(
      beforePromotion,
      postBlockedTime
    )

    promotionCallback(PromotionResults(PromotionStatus.REJECTED))

    val postRejectedTime = Instant.now()
    val postRejected = LedgerMaster.readLedger()
    assertThat(postRejected.user).isEqualTo(currentLedger.user)
    assertThat(postRejected.versionInstallDates).isEqualTo(currentLedger.versionInstallDates)
    assertThat(postRejected.allowedToPromote).isTrue()
    assertThat(postRejected.seenPromotions[MOTIVATION_PROMOTION_ID]?.result).isEqualTo(PromotionStatus.REJECTED)
    assertThat(postRejected.seenPromotions[MOTIVATION_PROMOTION_ID]?.id).isEqualTo(MOTIVATION_PROMOTION_ID)
    assertThat(postRejected.seenPromotions[MOTIVATION_PROMOTION_ID]?.datePromoted).isBetween(
      postBlockedTime,
      postRejectedTime
    )

    promotionCallback(PromotionResults(PromotionStatus.ACCEPTED))

    val postAcceptedTime = Instant.now()
    val postAccepted = LedgerMaster.readLedger()
    assertThat(postAccepted.user).isEqualTo(currentLedger.user)
    assertThat(postAccepted.versionInstallDates).isEqualTo(currentLedger.versionInstallDates)
    assertThat(postAccepted.allowedToPromote).isTrue()
    assertThat(postAccepted.seenPromotions[MOTIVATION_PROMOTION_ID]?.result).isEqualTo(PromotionStatus.ACCEPTED)
    assertThat(postAccepted.seenPromotions[MOTIVATION_PROMOTION_ID]?.id).isEqualTo(MOTIVATION_PROMOTION_ID)
    assertThat(postAccepted.seenPromotions[MOTIVATION_PROMOTION_ID]?.datePromoted).isBetween(
      postRejectedTime,
      postAcceptedTime
    )
  }
}
