package io.acari.DDLC

import io.acari.DDLC.legacy.Runner
import java.util.*
import java.util.concurrent.Callable

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