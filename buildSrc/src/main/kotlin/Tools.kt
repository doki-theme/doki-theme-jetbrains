const val DDLC_THEME_NAME_PREFIX = "DDLC: "
private val nameMapping =
  mapOf(
    "Kill la Kill" to "KillLaKill: ",
    "Blend S" to "BlendS: ",
    "Re Zero" to "Re:Zero: ",
    "Love Live" to "LoveLive: ",
    "Literature Club" to DDLC_THEME_NAME_PREFIX,
    "KonoSuba" to "KonoSuba: ",
    "Darling in the Franxx" to "Franxx: ",
    "Bunny Senpai" to "BunnySenpai: ",
    "Steins Gate" to "SG: ",
    "Gate" to "Gate: ",
    "Quintessential Quintuplets" to "QQ: ",
    "Fate" to "TypeMoon: ",
    "Type-Moon" to "TypeMoon: ",
    "Daily Life With A Monster Girl" to "MonsterMusume: ",
    "Vocaloid" to "Vocaloid: ",
    "DanganRonpa" to "DR: ",
    "High School DxD" to "DxD: ",
    "Sword Art Online" to "SAO: ",
    "Lucky Star" to "LS: ",
    "Evangelion" to "EVA: ",
    "EroManga Sensei" to "EroManga: ",
    "Miss Kobayashi's Dragon Maid" to "DM: ",
    "OreGairu" to "OreGairu: ",
    "OreImo" to "OreImo: ",
    "The Great Jahy Will Not Be Defeated." to "JahySama: ",
    "Future Diary" to "FutureDiary: ",
    "Kakegurui" to "Kakegurui: ",
    "Monogatari" to "Monogatari: ",
    "Don't Toy with me Miss Nagatoro" to "DTWMMN: ",
    "Miscellaneous" to "Misc: ",
    "Yuru Camp" to "YuruCamp: ",
    "NekoPara" to "NekoPara: ",
  )

fun getLafNamePrefix(groupName: String): String =
  nameMapping.getOrDefault(groupName, "")