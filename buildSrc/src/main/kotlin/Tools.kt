
const val DDLC_THEME_NAME_PREFIX = "DDLC: "
private val nameMapping =
  mapOf(
    "Kill la Kill" to "KillLaKill: ",
    "Re Zero" to "Re:Zero: ",
    "Literature Club" to DDLC_THEME_NAME_PREFIX,
    "KonoSuba" to "KonoSuba: ",
    "Fate" to "Fate: ",
    "Gate" to "Gate: ",
    "DanganRonpa" to "DR: ",
    "High School DxD" to "DxD: ",
    "Sword Art Online" to "SAO: ",
    "Lucky Star" to "LS: ",
    "Evangelion" to "EVA: ",
    "Miss Kobayashi's Dragon Maid" to "DM: ",
    "Miscellaneous" to "Misc: "
  )

fun getLafNamePrefix(groupName: String): String =
  nameMapping.getOrDefault(groupName, "")