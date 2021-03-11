package io.unthrottled.doki.icon.provider.associations

import com.google.gson.Gson
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.ResourceUtil
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object IconAssociatiorFactory {
  private val gson = Gson()
  private val log = Logger.getInstance(javaClass)
  fun create(association: Associations): IconAssociatior {
    return try {
      ResourceUtil.getResourceAsStream(
        IconAssociation::class.java.classLoader,
        "/associations/",
        association.fileName
      ).use {
        val def = gson.fromJson(
          InputStreamReader(it, StandardCharsets.UTF_8),
          AssociationModels::class.java
        )
        IconAssociatior(
          def.associations.map { associationDefinition ->
            IconAssociation(
              Regex(associationDefinition.associationPattern),
              associationDefinition.iconPath
            )
          }
        )
      }
    } catch (t: Throwable) {
      log.warn("Unable to create icon associator with defs", t)
      IconAssociatior(listOf())
    }
  }
}
