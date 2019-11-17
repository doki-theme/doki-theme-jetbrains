package io.acari.doki.chibi

import java.security.MessageDigest

object ChibiOrchestrator {
    private val messageDigest: MessageDigest = MessageDigest.getInstance("MD5")
    const val DOKI_CHIBI_PROP: String = "io.acari.doki.chibi"
    private const val CLUB_MEMBER_ON = "CLUB_MEMBER_ON"
    const val DOKI_BACKGROUND_PROP: String = "io.acari.doki.background"
    const val SAVED_THEME: String = "SAVED_THEME_PROPERTY"
    private const val RESOURCES_DIRECTORY =
        "https://raw.githubusercontent.com/cyclic-reference/ddlc-jetbrains-theme/master/src/main/resources"

    private var chibiLevel = ChibiLevel.ON
}