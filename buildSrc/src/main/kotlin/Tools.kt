

private val nameMapping =
  mapOf(
    "Kill la Kill" to "KillLaKill: ",
    "Re Zero" to "Re:Zero: ",
    "Literature Club" to "DDLC: ",
    "KonoSuba" to "KonoSuba: ",
    "Mistress" to "Miss: "
  )

fun getLafNamePrefix(groupName: String): String =
  nameMapping.getOrDefault(groupName, "")