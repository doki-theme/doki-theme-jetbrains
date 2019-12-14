package io.acari.doki.icon.provider.associations

import com.google.gson.Gson
import com.intellij.util.ResourceUtil
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object IconAssociatiorFactory {
  private val gson = Gson()
  fun create(association: Associations): IconAssociatior {
    return try {
      ResourceUtil.getResource(
        IconAssociation::class.java,
        "/associations/", association.fileName
      ).openStream().use {
        val def = gson.fromJson(
          InputStreamReader(it, StandardCharsets.UTF_8),
          AssociationModels::class.java
        )
        IconAssociatior(def.associations.map { iad ->
          IconAssociation(
            Regex(iad.associationPattern),
            iad.iconPath
          )
        })
      }
    } catch (t: Throwable) {
      t.printStackTrace()
      IconAssociatior(listOf())
    }
  }
}