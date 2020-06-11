
const val DDLC_THEME_NAME_PREFIX = "DDLC: "
private val nameMapping =
  mapOf(
    "Kill la Kill" to "KillLaKill: ",
    "Re Zero" to "Re:Zero: ",
    "Literature Club" to DDLC_THEME_NAME_PREFIX,
    "KonoSuba" to "KonoSuba: ",
    "Mistress" to "Miss: ",
    "DanganRonpa" to "DR: ",
    "High School DxD" to "DxD: ",
    "Sword Art Online" to "SAO: ",
    "Lucky Star" to "LS: ",
    "Miscellaneous" to "Misc: "
  )

fun getLafNamePrefix(groupName: String): String =
  nameMapping.getOrDefault(groupName, "")