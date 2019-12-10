package io.acari.DDLC.themes

data class ThemeChangedInformation(val isDark: Boolean,
                                   val accentColor: String,
                                   val contrastColor: String,
                                   val treeSelectionBackground: String,
                                   val foregroundColor: String)

data class AccentChangedInformation(val accentColor: String)