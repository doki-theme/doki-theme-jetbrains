package io.unthrottled.doki.hax

import org.jetbrains.annotations.NonNls
import java.lang.reflect.Field
import java.lang.reflect.Modifier

object FeildHacker {

  fun setFinalStatic(
    cls: Class<*>,
    @NonNls fieldName: String,
    newValue: Any?
  ) {
    val fields = cls.declaredFields
    for (field in fields) {
      if (field.name == fieldName) {
        setFinalStatic(field, newValue)
        return
      }
    }
  }

  fun setFieldValue(
    objectToHack: Any,
    fieldName: String?,
    value: Any?
  ) {
    try {
      val field = objectToHack.javaClass.getDeclaredField(fieldName)
      field.isAccessible = true
      field[objectToHack] = value
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun setFinalStatic(field: Field, newValue: Any?) {
    field.isAccessible = true
    val modifiersField = Field::class.java.getDeclaredField("modifiers")
    modifiersField.isAccessible = true
    modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
    field[null] = newValue
    modifiersField.setInt(field, field.modifiers or Modifier.FINAL)
    modifiersField.isAccessible = false
    field.isAccessible = false
  }
}
