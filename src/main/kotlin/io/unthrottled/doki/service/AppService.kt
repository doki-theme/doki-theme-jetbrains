package io.unthrottled.doki.service

import com.intellij.openapi.application.ApplicationNamesInfo

object AppService {
  fun getApplicationName(): String =
    ApplicationNamesInfo.getInstance().fullProductNameWithEdition
}