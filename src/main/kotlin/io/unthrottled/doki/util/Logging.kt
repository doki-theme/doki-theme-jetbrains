package io.unthrottled.doki.util

import com.intellij.openapi.diagnostic.Logger

interface Logging

fun Logging.logger(): Logger = Logger.getInstance(this::class.java)
