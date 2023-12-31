package io.unthrottled.doki.util

import com.intellij.ui.ColorUtil
import org.apache.commons.io.IOUtils
import java.awt.Color
import java.io.InputStream
import java.util.Optional
import java.util.concurrent.Callable
import java.util.stream.Stream

fun <T> getSafely(callable: Callable<T>): Optional<T & Any> =
  try {
    callable.call().toOptional()
  } catch (e: Throwable) {
    Optional.empty()
  }

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

fun <T> T?.toOptional() = Optional.ofNullable<T>(this)

fun <T> T?.toStream(): Stream<T> = Stream.of(this)

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

interface Runner {
  fun run()
}

fun Color.toHexString() = "#${ColorUtil.toHex(this)}"

fun String.toColor() = ColorUtil.fromHex(this)

fun InputStream.readAllTheBytes(): ByteArray = IOUtils.toByteArray(this)
