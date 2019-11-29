package io.acari.doki.util

import com.intellij.ui.ColorUtil
import java.awt.Color
import java.util.*
import java.util.concurrent.Callable
import java.util.stream.Stream

fun <T> getSafely(callable: Callable<T>): Optional<T> =
    try {
      callable.call().toOptional()
    } catch (e: Throwable){
      Optional.empty()
    }

fun runSafely(runner: Runner): Unit =
    try {
      runner.run()
    } catch (e: Throwable){
    }


fun <T> T?.toOptional() = Optional.ofNullable(this)

fun <T> T?.toStream(): Stream<T> = Stream.of(this)

interface Runner {
    fun run()
}

fun Color.toHexString() = "#${ColorUtil.toHex(this)}"