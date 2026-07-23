package io.unthrottled.doki.util

import com.intellij.ui.ColorUtil
import org.apache.commons.io.IOUtils
import java.awt.Color
import java.io.InputStream
import java.util.Optional

fun runSafely(
  runner: () -> Unit,
  onError: (Throwable) -> Unit = {},
): Unit =
  try {
    runner()
  } catch (e: Throwable) {
    onError(e)
  }

fun <T> runSafelyWithResult(
  runner: () -> T,
  onError: (Throwable) -> T,
): T =
  try {
    runner()
  } catch (e: Throwable) {
    onError(e)
  }

fun <T> T?.toOptional() = Optional.ofNullable(this)

fun <T> Optional<T>.doOrElse(
  present: (T) -> Unit,
  notThere: () -> Unit,
) = this.map {
  it to true
}.map {
  it.toOptional()
}.orElseGet {
  (null to false).toOptional()
}.ifPresent {
  if (it.second) {
    present(it.first)
  } else {
    notThere()
  }
}

fun Color.toHexString() = "#${ColorUtil.toHex(this)}"

fun String.toColor() = ColorUtil.fromHex(this)

fun InputStream.readAllTheBytes(): ByteArray = IOUtils.toByteArray(this)
